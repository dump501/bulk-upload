package com.dump501.bulkupload.controller;

import com.dump501.bulkupload.domain.Student;
import com.dump501.bulkupload.service.StudentService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/students")
@AllArgsConstructor
public class StudentController {
    private StudentService studentService;

    @PostMapping("bulk-upload")
    public ResponseEntity<Map<String, String>> uploadStudentsData(@RequestParam("file")MultipartFile file){
        this.studentService.saveStudents(file);
        return ResponseEntity
                .ok(Map.of("Message", "Students data uploaded and saved to database successfully"));
    }

    @GetMapping("")
    public ResponseEntity<List<Student>> getStudents(){
        return new ResponseEntity<>(studentService.getStudents(), HttpStatus.FOUND);
    }

    @GetMapping("pdf")
    public ResponseEntity<Map<String, String>> generatePdf(){
        try {
            //init
            Path resourcesPath = Paths.get("src/main/resources/static/Api.png");
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/hello.pdf"));

            // open the document
            document.open();

            // configure elements to be added to document

            // config text example
            Font font = FontFactory.getFont(FontFactory.TIMES_BOLD, 22);
            Chunk chunk = new Chunk("List of students", font);

            // paragraph example
            Paragraph paragraph = new Paragraph(chunk);

            // image config example
            Image image = Image.getInstance(resourcesPath.toAbsolutePath().toString());
            image.setAlignment(Element.ALIGN_RIGHT);

            // table config examle
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            // set table headers
            Stream.of("First name", "Middle name", "Last name", "Birth date", "Father phone", "Mother phone", "Sex")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();

                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(columnTitle));
                        table.addCell(header);
                    });

            addStudentsListRows(table);

            // add the configured elements in the document
            document.add(image);
            document.add(paragraph);
            document.add(new Paragraph("\n")); // for line break 😒
            document.add(table);

            // close the document
            document.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
        return ResponseEntity
                .ok(Map.of("Message", "Students pdf was generated successfully"));
    }

    private void addStudentsListRows(PdfPTable table){
        List<Student> students = studentService.getStudents();
        for (Student student :
                students) {
            table.addCell(student.getFirstName());
            table.addCell(student.getMiddleName());
            table.addCell(student.getLastName());
            table.addCell(String.valueOf(student.getBirthDate()));
            table.addCell(student.getFatherPhone());
            table.addCell(student.getMotherPhone());
            table.addCell(student.getSex());
        }
    }
}
