## Bulk upload & pdf generation using itext
### A spring boot project to test Excel bulk upload and pdf generation

This project is developed using Spring boot V3

### Documentation
* Make a get request at `http://localhost:8080/students` to get students list
* Make a post request containing an Excel file at `http://localhost:8080/students/bulk-upload` to upload excel file containing the students list
* Make a get request at `http://localhost:8080/students/pdf` to generate students list pdf.

Note:  
* generated pdf is stored at `src/main/resources/students.pdf`
* a sample students list is stored at `"src/main/resources/students list.xlsx"`

made with ❤️ by Fritz <tsafack07albin@gmail.com> for SunbaseData intership application.