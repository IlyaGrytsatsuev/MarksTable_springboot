package gratchuvalsky.spring_boot.Controller;

import gratchuvalsky.spring_boot.DAO.MarksTablesDao;
import gratchuvalsky.spring_boot.Model.StudentSubjectsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class StudentController {

    private final MarksTablesDao dao;
    @Autowired
    public StudentController(MarksTablesDao dao) {
        this.dao = dao;
    }

    @GetMapping("/SubMarks/form={form_id}:student={student_id}")
    public String getStudentsSubjectsAndMarks(@PathVariable("student_id") int student_id,
                                              @PathVariable("form_id") int form_id,
                                              Model model){
        StudentSubjectsList subjectsAndMarks =
                dao.getStudentSubjectsAndMarks(form_id, student_id);
        model.addAttribute("subjectsAndMarks", subjectsAndMarks);
        model.addAttribute("form_id", form_id);

        return "StudentSubjectsMarks";

    }
}
