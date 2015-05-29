package com.example.stylo.bwyath;

import java.util.ArrayList;

/**
 * Created by Stylo on 29/05/2015.
 */
public class Page {

    private int number;
    private String title;
    private String content;
    private String question;
    private String  choise_type;
    private ArrayList<Choise> choise;

    public Page(int number, String title, String content, String question, String choise_type){
        this.setNumber(number);
        this.setTitle(title);
        this.setContent(content);
        this.setQuestion(question);
        this.setChoise_type(choise_type);
        this.choise = new ArrayList<Choise>();
    }

    public void addChoise(Choise choise){
        this.choise.add(choise);
    }

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

    public Choise getChoise(int id) {
        return choise.get(id);
    }

    public void setChoise(ArrayList<Choise> choise) {
        this.choise = choise;
    }

}
