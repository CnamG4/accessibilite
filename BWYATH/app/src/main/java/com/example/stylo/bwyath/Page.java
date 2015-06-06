package com.example.stylo.bwyath;

import java.util.ArrayList;

/**
 * Created by Stylo on 29/05/2015.
 * Story book page
 */
public class Page {

    // Page number
    private int number;
    // Page type (begin, end, normal)
    private String type;
    // Page title
    private String title;
    // Page content
    private String content;
    // Page question
    private String question;
    // Answer type (radiobutton, button, listgroup)
    private String  choise_type;
    // Choise list
    private ArrayList<Choise> choise;

    /**
     * Page constructor without the choise list
     * @param number Page number
     * @param title Page title
     * @param content Page content
     * @param question Question ask to user
     * @param choise_type Choise type ask to user
     */
    public Page(int number, String type, String title, String content, String question, String choise_type){
        this.setNumber(number);
        this.setType(type);
        this.setTitle(title);
        this.setContent(content);
        this.setQuestion(question);
        this.setChoise_type(choise_type);
        this.choise = new ArrayList<Choise>();
    }

    public Page(int i, String s, String s1, String s2, String radiobutton) {
        this.setNumber(number);
        this.setType("mdr");
        this.setTitle(title);
        this.setContent(content);
        this.setQuestion(question);
        this.setChoise_type(choise_type);
        this.choise = new ArrayList<Choise>();
    }

    /**
     * Add a choise in the choise list
     * @param choise choise to add
     */
    public void addChoise(Choise choise){
        this.choise.add(choise);
    }

    /**
     * Create and add a choise in the choise list
     * @param content Choise content
     * @param target Page targeted by the choice
     */
    public void addChoise(String content, int target){
        Choise choise = new Choise(content, target);
        this.choise.add(this.choise.size(),choise);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoise_type() {
        return choise_type;
    }

    public void setChoise_type(String choise_type) {
        this.choise_type = choise_type;
    }

    public ArrayList<Choise> getChoise() {
        return choise;
    }

    public Choise getChoise(int id) { return choise.get(id); }

    public void setChoise(ArrayList<Choise> choise) {
        this.choise = choise;
    }

}
