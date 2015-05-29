package com.example.stylo.bwyath;

import java.util.ArrayList;

/**
 * Created by Stylo on 29/05/2015.
 * Classe contenant chaque page du livre d'histoire
 */
public class Page {

    // Numéro de la page
    private int number;
    // Titre de la page
    private String title;
    // Contenu de la page
    private String content;
    // Question de la  page
    private String question;
    // Type de choix permettant de répondre à la question
    private String  choise_type;
    // Liste des différents choix posibles
    private ArrayList<Choise> choise;

    /**
     * Constructeur de Page sans soumissions des choix
     * @param number Numéro de la page en entier
     * @param title Titre de la page
     * @param content COntenu texte de la page
     * @param question Question posée à l'utilisateur
     * @param choise_type Type de choix demandé à l'utilisateur
     */
    public Page(int number, String title, String content, String question, String choise_type){
        this.setNumber(number);
        this.setTitle(title);
        this.setContent(content);
        this.setQuestion(question);
        this.setChoise_type(choise_type);
        this.choise = new ArrayList<Choise>();
    }

    /**
     * Ajouter un choix dans la liste de choix de la page
     * @param choise Le choix à ajouter
     */
    public void addChoise(Choise choise){
        this.choise.add(choise);
    }

    /**
     * Créer et ajouter un choix dans la liste de choix de la page
     * @param content Contenu du choix
     * @param target Numéro de page ciblé par le choix
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
