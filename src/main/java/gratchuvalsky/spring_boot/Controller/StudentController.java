package gratchuvalsky.spring_boot.Controller;

import gratchuvalsky.spring_boot.DAO.MarksTablesDao;
import gratchuvalsky.spring_boot.Model.Mark;
import gratchuvalsky.spring_boot.Model.StudentSubjectsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/SubMarks")
public class StudentController {

    private final MarksTablesDao dao;
    @Autowired
    public StudentController(MarksTablesDao dao) {
        this.dao = dao;
    }

    @GetMapping("/form={form_id}:student={student_id}")
    public String getStudentsSubjectsAndMarks(@PathVariable("student_id") int student_id,
                                              @PathVariable("form_id") int form_id,
                                              Model model){
        StudentSubjectsList subjectsAndMarks =
                dao.getStudentSubjectsAndMarks(form_id, student_id);
        model.addAttribute("subjectsAndMarks", subjectsAndMarks);
        model.addAttribute("form_id", form_id);

        return "StudentSubjectsMarks";
    }

    @GetMapping("/form={form_id}/student={student_id}/mark={mark_id}")
    public String getMarkInfo(@PathVariable("form_id") String form_id,
                              @PathVariable("mark_id") int mark_id,
                              @PathVariable("student_id") String student_id,
                              Model model){
        Mark mark = dao.getMark(mark_id);
        String date = dao.getMarkDate(mark_id);
        model.addAttribute("mark", mark);
        model.addAttribute("form_id", form_id);
        model.addAttribute("date", date);

        return "MarkInfo";
    }
}
