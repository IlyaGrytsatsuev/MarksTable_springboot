package gratchuvalsky.spring_boot.Model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Subject {

    private int id ;
    private boolean isEmpty = true;
    private String name;

    private Map<Integer, Integer> marks;
    private List<Date> dates;

    private int student_id;
    private int medium_mark;

    public int getMedium_mark() {
        return medium_mark;
    }
    public void countMediumMark(){
        double sum = 0;
        for(int key : marks.keySet())
            sum+=(double) marks.get(key);
        sum/=(double)marks.keySet().size();
        this.medium_mark = (int) Math.round(sum);
    }

    public void setMedium_mark(int medium_mark) {
        this.medium_mark = medium_mark;
    }

    public Subject(){
        name = null;
        marks = new LinkedHashMap<>();
        dates = new ArrayList<>();
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }
    /* public Subject(int id, String name, List<Integer> marks, List<Date> dates){
        this.id = id;
        this.name = name;
        this.marks = marks;
        this.dates = dates;
    }*/

    public void setId(int id) {
        this.id = id;
        setEmpty(false);
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void addMark(int id, Date date, int mark){
        marks.put(id, mark);
        dates.add(date);
    }

    public void removeMark(Date date, int mark){
        marks.remove(date);
    }


    public Map<Integer, Integer> getMarks() {
        return marks;
    }
    public List<Date> getDates(){
        return dates;
    }
}
