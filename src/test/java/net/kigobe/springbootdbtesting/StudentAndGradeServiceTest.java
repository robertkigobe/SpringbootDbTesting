package net.kigobe.springbootdbtesting;

import net.kigobe.springbootdbtesting.model.CollegeStudent;
import net.kigobe.springbootdbtesting.repository.StudentDao;
import net.kigobe.springbootdbtesting.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentAndGradeServiceTest {
    @Autowired
    StudentAndGradeService studentService;
    @Autowired
    StudentDao studentDao;

    @Autowired
    JdbcTemplate jdbc;

    @BeforeEach
    public void setUpDatabase() {
        jdbc.execute("Insert into student(id,firstName,lastName,email_address)" +
                "Values (2, 'Augustine', 'Kanyike', 'augustineKanyike@yahoo.com')");
    }

    @Test
    public void createStudentService() {
        studentService.createStudent("Robert", "Kigobe", "robertkigobe@yahoo.com");
        CollegeStudent student = studentDao.findByEmailAddress("robertkigobe@yahoo.com");

        assertEquals("robertkigobe@yahoo.com", student.getEmailAddress(), "findByEmail");
    }


    @Test
    public void deleteStudentService() {
        Optional<CollegeStudent> deletedCollegeStudent = studentDao.findById(2);
        assertTrue(deletedCollegeStudent.isPresent(), "Return true");
    }

    @Test
    public void isStudentNullCheck() {

        assertTrue(studentService.checkIfStudentIsNull(2));

        assertFalse(studentService.checkIfStudentIsNull(0));
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM student");
    }

    @Sql("/insertData.sql")
    @Test
    public void getGradebookService() {
        Iterable<CollegeStudent> interableCollegeStudents = studentService.getGradebook();
        List<CollegeStudent> collegeStudents = new ArrayList<>();

        for (CollegeStudent collegeStudent : interableCollegeStudents) {
            collegeStudents.add(collegeStudent);
        }
        assertEquals(5, collegeStudents.size());
    }

    @AfterEach
    public void setUpAfterTransaction() {

        jdbc.execute("Delete from Student");

    }


}
