package com.dump501.bulkupload.service;

import com.dump501.bulkupload.domain.Student;
import com.dump501.bulkupload.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {
    private StudentRepository studentRepository;

    public void saveStudents(MultipartFile file){
        if (ExcelUploadService.isValidExcelFile(file)){
            try {
                List<Student> students = ExcelUploadService.getStudentsDataFromExcel(file.getInputStream());
                this.studentRepository.saveAll(students);
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
    }

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }
}
