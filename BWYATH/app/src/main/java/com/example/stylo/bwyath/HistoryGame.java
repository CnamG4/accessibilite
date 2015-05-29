package com.example.stylo.bwyath;

import java.util.ArrayList;

/**
 * Created by Stylo on 29/05/2015.
 */
public class HistoryGame {

    private ArrayList<Page> history;
    private Page current_page;

    public HistoryGame(){
        this.history = new ArrayList<Page>();
        this.current_page = null;
    }

    public void initHistory(){
        Page p = new Page(
                1,
                "Page 1 - Introduction",
                "Contenu de la page d'introduction",
                "Quelle porte voulez-vous prendre ?",
                "radiobutton"
        );
        p.addChoise("La porte de droite",2);
        p.addChoise("La porte de gauche",3);
        p.addChoise("La cachette mystère !",4);
        this.history.add(0,p);

        this.current_page = history.get(0);

        p = new Page(
                2,
                "Page 2 - La porte de droite",
                "La porte de droite est en fait un cul de sac et vous retournez à la salle précédente... ",
                "Quel porte voulez vou prendre ?",
                "radiobutton"
        );
        p.addChoise("La porte de gauche",2);
        p.addChoise("La porte de droite",3);
        p.addChoise("La cachette mystère !",4);
        this.history.add(1,p);

        p = new Page(
                3,
                "Page 3 - La porte de gauche",
                "Vous rencontrez un rat qui enquête sur la mort d'une souris sur un terrain de tennis. " +
                "Ce dernier vous raquette votre raquette pour passer inconito.",

                "Que faisez-vous ?",
                "radiobutton"
        );
        p.addChoise("Je suis dans un rêve car c'est impossible",6);
        p.addChoise("Je lui donne",4);
        p.addChoise("je le tappe et je fais des frittes",5);
        this.history.add(2,p);

        p = new Page(
                4,
                "Page 4 - La salle mystérieuse",
                "La fameuse salle mystère ... ",
                "Quel potion choisissez-vous d'engloutir ?",
                "radiobutton"
        );
        p.addChoise("La potion verdatre",5);
        p.addChoise("La potion noire",6);
        p.addChoise("La potion mystèrieusement bizare !",4);
        this.history.add(3,p);

        p = new Page(
                5,
                "Page 5 - Le fou du roi",
                "Vos choix sont mauvais et vous etes en echec et mathématiques. Il est temps de prendre du ragout. ",
                "Comment trouvez-vous le ragout ?",
                "radiobutton"
        );
        p.addChoise("Il est bon",6);
        p.addChoise("En le cherchant",6);
        p.addChoise("Je n'aime pas le ragout",4);
        this.history.add(4,p);

        p = new Page(
                6,
                "Page 6 - Fin du rêve",
                "Il était temps de reprendre le travail mon vieux Lopez. La castagne te monte à la tête et tu halucines d'étranges choix ",
                "Comment trouvez-vous le ragout ?",
                null
        );
        this.history.add(5,p);

    }

    public int getNbPage(){
        return this.history.size();
    }

    public void addPage(Page page){
        this.history.add(page);
    }

    public void addPage(int number, String title, String content, String question, String choise_type, ArrayList<Choise> choise){
        Page page = new Page(number,title,content,question,choise_type);
        for(int i = 0; i<choise.size(); i++){
            page.addChoise(choise.get(i));
        }
        this.history.add(page);
    }

    public ArrayList<Page> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<Page> history) {
        this.history = history;
    }

    public Page getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(Page current_page) {
        this.current_page = current_page;
    }

}
