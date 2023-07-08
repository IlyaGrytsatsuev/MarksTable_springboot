package gratchuvalsky.spring_boot.Controller;

import gratchuvalsky.spring_boot.DAO.MarksTablesDao;
import gratchuvalsky.spring_boot.Model.*;
import gratchuvalsky.spring_boot.service.PersonDetailsService;
import gratchuvalsky.spring_boot.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RequestMapping("/Forms")
@Controller
public class  editDBController {
    private final MarksTablesDao dao;
    private final PersonDetailsService personDetailsService;
    private final RegistrationService registrationService;

    @Autowired
    public editDBController(MarksTablesDao dao, PersonDetailsService service, PersonDetailsService personDetailsService, RegistrationService registrationService){
        this.dao = dao;
        this.personDetailsService = personDetailsService;
        this.registrationService = registrationService;
    }


    @GetMapping("/addMark:form={form_id}:subject={subject_id}:student={student_id}")
    public String addMarkForm(@PathVariable("form_id") int form_id,
                              @PathVariable("subject_id") int subject_id,
                              @PathVariable("student_id") int student_id,
                              Model model){
        Mark mark = new Mark();
        mark.setForm_id(form_id);
        mark.setSubject_id(subject_id);
        mark.setStudent_id(student_id);
        model.addAttribute("mark", mark);
        return "addMark";
    }

    @PostMapping("/addMark:form={form_id}:subject={subject_id}:student={student_id}")
    public String addMark(@ModelAttribute("mark") Mark mark,
                          @PathVariable("form_id") int form_id) {
        dao.add_mark(mark);
        return "redirect:/Forms/" + "Students:form=" + form_id +
                "/Subjects:student="+ mark.getStudent_id() ;
    }

    @GetMapping("")
    public String getFormsPage(Model model){
        List<FormAndId> forms_list= dao.getSchoolFormsList();
        model.addAttribute("list", forms_list);
        return "FormsList";
    }

    @GetMapping("/Students:form={form_id}")
    public String getStudentsList(@PathVariable("form_id") int form_id,
                                  Model model){
        model.addAttribute("students", dao.getStudentsList(form_id));
        model.addAttribute("form_id", form_id);
        return "StudentsList";
    }

    @GetMapping("/Students:form={form_id}/Subjects:student={student_id}")
    public String getSubjectsAndMarks(@PathVariable("form_id") int form_id,
                                      @PathVariable("student_id") int student_id,
                                      Model model){
        StudentSubjectsList subjectsAndMarks =
                dao.getStudentSubjectsAndMarks(form_id, student_id);

        int acccount_id = dao.getStudentAccountId(student_id);
        model.addAttribute("auth",
                        personDetailsService.getStudentLoginAndPassword(acccount_id));
        model.addAttribute("subjectsAndMarks", subjectsAndMarks);
        model.addAttribute("form_id", form_id);
        return "SubjectsList";
    }

    @GetMapping("/editMark:form={form_id}:" +
            "subject={subject_id}:student={student_id}:mark={mark_id}")
    public String getEditMarkForm(@PathVariable("form_id") int form_id,
                                  @PathVariable("subject_id") int subject_id,
                                  @PathVariable("student_id") int student_id,
                                  @PathVariable("mark_id") int mark_id,
                                  Model model){
        String date = dao.getMarkDate(mark_id);
        Mark mark = dao.getMark(mark_id);
        model.addAttribute("mark", mark);
        model.addAttribute("form_id", form_id);
//        model.addAttribute("subject_id", subject_id);
//        model.addAttribute("student_id", student_id);
//        model.addAttribute("mark_id", mark_id);
        model.addAttribute("date", date);
        return "editMark";
    }

    @PatchMapping("/editMark:form={form_id}:" +
            "subject={subject_id}:student={student_id}:mark={mark_id}")
    public String editMark(@PathVariable("form_id") String form_id,
                           @PathVariable("mark_id") int mark_id,
                           @ModelAttribute("mark") Mark mark){
        dao.editMark(mark_id, mark.getMark_value(), mark.getWork_name());
        return "redirect:/Forms" + "/Students:form=" + form_id
                +"/Subjects:student=" +
                mark.getStudent_id() ;
    }

    @DeleteMapping("/deleteMark:form={form_id}:" +
            "subject={subject_id}:student={student_id}:mark={mark_id}")
    public String deleteMark(@PathVariable("mark_id") int mark_id,
                             @PathVariable("form_id") String form_id,
                             @PathVariable("student_id") String student_id,
                             @PathVariable("subject_id") String subject_id){

        dao.deleteMark(mark_id);

        return "redirect:/Forms" + "/Students:form=" + form_id
                +"/Subjects:student=" + student_id ;
    }

    @DeleteMapping("/deleteForm:form={form_id}")
    public String DeleteForm(@PathVariable("form_id") int form_id){
        dao.deleteForm(form_id);
        return "redirect:/Forms";
    }

    @GetMapping("/editForm:form={form_id}")
    public  String getEditFormName(@PathVariable("form_id") int form_id,
                                   Model model){
        model.addAttribute("form_id", form_id);
        return "editForm";
    }

    @PatchMapping("/editForm:form={form_id}")
    public String editFormName(@PathVariable("form_id") int form_id,
                               @RequestParam("form_name") String form_name){
        dao.editForm(form_id, form_name);
        return "redirect:/Forms";
    }

    @GetMapping("/addForm")
    public String getAddForm(){
        return "addForm";
    }

    @PostMapping("/addForm")
    public String addForm(@RequestParam("form_name") String form_name){
        dao.addForm(form_name);
        return "redirect:/Forms";
    }

    @GetMapping("/addStudent:form={form_id}")
    public String getAddStudentForm(@PathVariable("form_id") int form_id,
                                    Model model){
        model.addAttribute("form_id", form_id);
        return "addStudent";
    }

    @PostMapping("/addStudent:form={form_id}")
    public String addStudent(@PathVariable("form_id") int form_id,
                             @RequestParam("name") String name,
                             @RequestParam("surname") String surname){
        int account_id = registrationService.register();
        dao.addStudent(name, surname, form_id, account_id);
        return "redirect:/Forms/Students:form=" + form_id;
    }

    @DeleteMapping("/deleteStudent:form={form_id}:student={student_id}")
    public String deleteStudent(@PathVariable("form_id") int form_id,
                                @PathVariable("student_id") int student_id ){
        int account_id = dao.getStudentAccountId(student_id);
        dao.deleteStudent(student_id);
        personDetailsService.deletePerson(account_id);
        return "redirect:/Forms/Students:form=" + form_id ;
    }
    @GetMapping("/editStudent:form={form_id}:student={student_id}")
    public String getEditStudent(@PathVariable("form_id") int form_id,
                                 @PathVariable("student_id") int student_id,
                                 Model model ){
        StudentNameAndSurname student  = dao.getStudent(student_id);
        model.addAttribute("form_id", form_id);
        model.addAttribute("student_id", student_id);
        model.addAttribute("student", student);

        return "editStudent" ;
    }
    @PatchMapping("editStudent:form={form_id}:student={student_id}")
    public String editStudent(@PathVariable("form_id") int form_id,
                              @PathVariable("student_id") int student_id,
                              @RequestParam("name") String name,
                              @RequestParam("surname") String surname){
        dao.editStudentNameSurname(name, surname, student_id);

        return "redirect:/Forms/Students:form=" + form_id ;
    }

    @GetMapping("/addSubject:form={form_id}")
    public String getAddSubject(@PathVariable("form_id") int form_id,
                                Model model){
        model.addAttribute("form_id", form_id);
        return "addSubject";
    }

    @GetMapping("/form={form_id}/Subjects")
    public String FormSubjects(@PathVariable("form_id") int form_id,
                               Model model){
        StudentSubjectsAndIds res = dao.getFormSubjects(form_id);
        model.addAttribute("subjects_names", res.getSubject_names());
        model.addAttribute("subjects_ids", res.getSubject_ids());
        model.addAttribute("res", res);
        return "FormSubjects";
    }

    @PostMapping("/addSubject:form={form_id}")
    public String addSubject(@PathVariable("form_id") int form_id,
                             @RequestParam("subject_name") String subject_name,
                             Model model) {
        dao.addSubject(subject_name, form_id);
        return "redirect:/Forms/form=" + form_id + "/Subjects";
    }

    @DeleteMapping("/deleteSubject:form={form_id}:subject={subject_id}")
    public String deleteSubject(@PathVariable("subject_id") int subject_id,
                                @PathVariable("form_id") int form_id){
        dao.deleteSubject(subject_id);
        return "redirect:/Forms/form=" + form_id + "/Subjects";
    }

    @GetMapping("/editSubject:form={form_id}:subject={subject_id}")
    public String getEditSubject(@PathVariable("subject_id") int subject_id,
                                 @PathVariable("form_id") int form_id,
                                 Model model){

        String subject_name = dao.getSubjectName(subject_id);
        model.addAttribute("subject_id", subject_id);
        model.addAttribute("form_id", form_id);
        model.addAttribute("subject_name", subject_name);
        return "editSubject";
    }

    @PatchMapping("/editSubject:form={form_id}:subject={subject_id}")
    public String editSubject(@PathVariable("subject_id") int subject_id,
                              @PathVariable("form_id") int form_id,
                              @RequestParam("subject_name") String subject_name){

        dao.editSubject(subject_id, subject_name);

        return "redirect:/Forms/form=" + form_id + "/Subjects";
    }




    }
