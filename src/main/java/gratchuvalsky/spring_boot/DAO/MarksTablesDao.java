package gratchuvalsky.spring_boot.DAO;

import gratchuvalsky.spring_boot.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class MarksTablesDao {

    private JdbcTemplate jdbcTemplate;
    private SchoolForm schoolForm;
    private static int formId = 0;
    private Student student;
    private static int studentId = 0;
    private Subject subject;
    private static int subjectId = 0;

    private List<SchoolForm> formsList;


    @Autowired
    public MarksTablesDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        schoolForm = null;
        student = null;
        subject = null;
        formsList = new ArrayList<>();
    }

    public String getRoleById(int id){
        String res = jdbcTemplate.queryForObject("select role_type from" +
                "role where id = ?", new Object[]{id}, String.class);
        return res;
    }

    public int getIdByRole(String role){
        int res = jdbcTemplate.queryForObject("select id from role" +
                "where role_type = ?", new Object[]{role}, Integer.class);
        return res;
    }
    public void addForm(String form_name) {
        jdbcTemplate.update("insert into forms(form_name) values(?)", form_name);
    }

    public void deleteForm(int form_id){
        jdbcTemplate.update("delete from forms where id = ?", form_id);
    }

    public void editForm(int form_id, String name){
        jdbcTemplate.update("update forms set form_name = ? where id = ?", name, form_id);
    }

    public void addStudent(String name, String surname, int form_id, int account_id) {
        jdbcTemplate.update("insert into students(name, surname, form_id, account_id)" +
                " VALUES(?,?,?, ?) ", name, surname, form_id, account_id);
    }

    public void editStudentNameSurname(String name, String surname, int student_id){
        jdbcTemplate.update("update students set name = ?, surname = ? where id = ?", name, surname, student_id);
    }

    public void deleteStudent(int student_id){
        jdbcTemplate.update("delete from students where id = ?", student_id);
    }

    public int getStudentAccountId(int student_id){
        int res = jdbcTemplate.queryForObject("select account_id" +
                " from students where id = ?",
                new Object[]{student_id}, Integer.class);
        return res;
    }
    public List<Integer> getStudentIdAndFormIdByAuthId(int authId){
        List<Integer> res = new ArrayList<>();
        int id = jdbcTemplate.queryForObject("select id from students " +
                "where account_id = ?", new Object[]{authId}, Integer.class);
        int form_id = jdbcTemplate.queryForObject("select form_id from students " +
                "where account_id = ?", new Object[]{authId}, Integer.class);
        res.add(id);
        res.add(form_id);
        return res;
    }

    public void addSubject(String subject_name, int form_id) {
        jdbcTemplate.update("insert into subjects(subject_name, form_id) values(?, ?)", subject_name, form_id);
    }

    public String getSubjectName(int subjectId){
        String res  = jdbcTemplate.queryForObject("select subject_name from subjects " +
                "where id = ?", new Object[]{subjectId}, String.class);

        return res;
    }
    public void editSubject(int subjectId, String subject_name){
        jdbcTemplate.update("update subjects set subject_name = ? where id = ?",
                subject_name, subjectId);

    }

    public void deleteSubject(int subject_id){
        jdbcTemplate.update("delete from subjects where id = ?", new Object[]{subject_id});
    }

    public void editMark(int mark_id, int mark_value){
        jdbcTemplate.update("update marks set mark_value = ? where id = ?", mark_value, mark_id);
    }
    public void add_mark(Mark mark){
       jdbcTemplate.update("insert into marks(mark_value, date, student_id, subject_id) values(?, ?, ?, ?)",
               mark.getMark_value(), mark.getDate(), mark.getStudent_id(), mark.getSubject_id());

    }


    public void getSchoolFormDataFromDB(int form_id) {
        List<Student> students = jdbcTemplate.query("select student_id, name, surname,  form_id from  students where form_id = ?", new Object[]{form_id}, new BeanPropertyRowMapper<Student>(Student.class));
        String form_name = jdbcTemplate.queryForObject("select form_name from forms where form_id = ?", String.class, form_id);
        SchoolForm form = new SchoolForm(form_id, form_name);
        form.setStudents(students);
        formsList.add(form);
    }

    public List<FormAndId> getSchoolFormsList() {
        List<FormAndId> forms_list = jdbcTemplate.query("select id, form_name from forms", new BeanPropertyRowMapper(FormAndId.class));
        return forms_list;
    }

    public StudentNameAndSurname getStudent(int student_id){
        List<StudentNameAndSurname> students = jdbcTemplate.query("select name, surname from students where id = ?",
                new Object[]{student_id}, new BeanPropertyRowMapper(StudentNameAndSurname.class));

        return students.get(0);
    }
    public List<StudentNameAndSurname> getStudentsList(int form_id) {

        List<StudentNameAndSurname> students_list = jdbcTemplate.query("select id, form_id, name, surname " +
                "from students where form_id = ? order by surname",
                new Object[]{form_id}, new BeanPropertyRowMapper(StudentNameAndSurname.class));

        return students_list;
    }

    public StudentSubjectsAndIds getFormSubjects(int form_id){
        StudentSubjectsAndIds res = jdbcTemplate.query("select id, subject_name from subjects  " +
                "where form_id = ? order by subject_name", new Object[]{form_id},
                new SubjectsAndIdsMapper()).stream().findAny().orElse(new StudentSubjectsAndIds());
        res.setForm_id(form_id);

        return res;

    }
    public StudentSubjectsAndIds getStudentSubjects(int student_id, int form_id) {

        StudentSubjectsAndIds res = getFormSubjects(form_id);
        List<StudentNameAndSurname> students_list =  jdbcTemplate.query("select name, surname from students where id = ?",
                new Object []{student_id}, new BeanPropertyRowMapper(StudentNameAndSurname.class));

        res.setName(students_list.get(0).getName());
        res.setSurname(students_list.get(0).getSurname());
        res.setStudent_id(student_id);
        return res;
    }

    public StudentSubjectsList getStudentSubjectsAndMarks(int formId, int studentId){

        StudentSubjectsList list = new StudentSubjectsList();
        StudentSubjectsAndIds subAndIds = getFormSubjects(formId);
        for(int i = 0; i < subAndIds.getSubject_ids().size(); i++){
            Subject tmp = getSubjectMarks(studentId, subAndIds.getSubject_ids().get(i));
            list.addSubject(tmp);
        }
        /*StudentSubjectsList list = jdbcTemplate.query("select subject_id, " +
                "subject_name, mark_value, m.id, date from subjects sb join marks m on sb.id = m.subject_id " +
                "where m.student_id = ? order by subject_name, date",
                new Object[]{studentId}, new SubjectsAndMarksMapper()).stream().findAny().orElse(null);*/

        StudentNameAndSurname student = getStudent(studentId);
        list.setStudentName(student.getName());
        list.setStudentSurname(student.getSurname());
        list.setStudent_id(studentId);

        System.out.println(list.getSubjectsList().size());
        for(Subject subject : list.getSubjectsList()){
            System.out.print(subject.getName() +  " id = " + subject.getId() +": ");
            for(int j = 0; j < subject.getDates().size(); j++){
                System.out.print(subject.getDates().get(j) + " ");
            }
            System.out.println(":");
            for(int id : subject.getMarks().keySet())
                System.out.print(subject.getMarks().get(id) + " ");
            System.out.println("\n");
        }
        return list;
    }

    public Subject getSubjectMarks(int student_id, int subject_id){

        Subject subject = jdbcTemplate.query("select mark_value, date, id from marks m " +
                        " where student_id = ? and subject_id = ? order by m.date ",
                new Object[]{student_id, subject_id}, new SubjectMapper()).stream().findAny().orElse(null);

        if (subject == null) {
            subject = new Subject();
        }
        String name = jdbcTemplate.queryForObject("select subject_name from subjects where id = ?", new Object[]{subject_id}, String.class);
        subject.setName(name);
        subject.setId(subject_id);
        subject.setStudent_id(student_id);
        return subject;
    }

    public void deleteMark(int mark_id){
        jdbcTemplate.update("delete from marks where id = ?", mark_id);
    }

    public String getMarkDate(int mark_id){
        Date date = jdbcTemplate.queryForObject("select date from marks where id = ?",
                new Object[]{mark_id}, Date.class);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
        String res = sdf.format(date);
        return res;
    }
}


