package com.example.stylo.bwyath;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Stylo on 29/05/2015.
 */
public class StoryGame {

    // Liste des différentes page de l'histoire
    private ArrayList<Page> story;
    // Page courrante de l'histoire
    private Page current_page;
    // Historique des pages précédentes
    private Stack<Integer> history_page;

    /**
     * Constructeur d'histoire
     */
    public StoryGame(){
        this.story = new ArrayList<Page>();
        this.current_page = null;
        this.history_page = new Stack<Integer>();
    }

    /**
     * Initialise l'histoire et les pages qui lui sont attribuées
     */
    public void initHistory(){
        Page p = new Page(
                1,
                "begin",
                "Page 1 - Introduction.",
                "Nous sommes en quinze-cents-quinze avant Marignan. Vous venez d'éviter un pavé de saumon et il fait toujours beau dans votre frigo. Demain matin, il était temps pour vous de quitter votre travail et donc de prendre la porte.",
                "Quelle porte voulez-vous prendre ?",
                "radiobutton"
        );
        p.addChoise("La porte de droite.",2);
        p.addChoise("La porte de gauche.",3);
        p.addChoise("La cachette mystère !.",4);
        this.story.add(0,p);

        p = new Page(
                2,
                "simple",
                "Page 2 - La porte de droite.",
                "La porte de droite était à coup sur un cul de sac de Bilbon saké et vous retournez à la salle précédente... ",
                "Quel porte voulez-vous prendre ?",
                "radiobutton"
        );
        p.addChoise("La porte de gauche", 2);
        p.addChoise("La porte de droite",3);
        p.addChoise("La cachette mystère !",4);
        this.story.add(1, p);

        p = new Page(
                3,
                "simple",
                "Page 3 - La porte de gauche.",
                "Vous rencontrez un rat qui enquête sur la mort d'une souris sur un terrain de tennis. " +
                "Ce dernier vous raquette votre raquette pour passer inconito.",

                "Que faites-vous ?",
                "radiobutton"
        );
        p.addChoise("Je suis dans un rêve car c'est impossible",6);
        p.addChoise("Je lui donne.", 4);
        p.addChoise("je le tappe et je fais des frites.",5);
        this.story.add(2, p);

        p = new Page(
                4,
                "simple",
                "Page 4 - La salle mystérieuse.",
                "La fameuse salle mystère ... ",
                "Quelle potion choisissez-vous d'engloutir ?",
                "radiobutton"
        );
        p.addChoise("La potion verdatre.",5);
        p.addChoise("La potion noire.",6);
        p.addChoise("La potion mystèrieusement bizare !", 4);
        this.story.add(3,p);

        p = new Page(
                5,
                "simple",
                "Page 5 - Le fou du roi.",
                "Vos choix sont mauvais et vous-êtes en echec et mathématiques. Il est temps de prendre du ragoût. ",
                "Comment trouvez-vous le ragoût ?",
                "radiobutton"
        );
        p.addChoise("Il est bon",6);
        p.addChoise("En le cherchant",6);
        p.addChoise("Je n'aime pas le ragout",4);
        this.story.add(4, p);

        p = new Page(
                6,
                "end",
                "Page 6 - Fin du rêve.",
                "Il était temps de reprendre le travail mon vieux Lopez. La castagne te monte à la tête et tu halucines d'étranges choix.",
                "",
                ""
        );
        this.story.add(5, p);

        this.current_page = story.get(0);
    }

    public int getNbPage(){
        return this.story.size();
    }

    public void addPage(Page page){
        this.story.add(page);
    }

    public void addPage(int number, String type, String title, String content, String question, String choise_type, ArrayList<Choise> choise){
        Page page = new Page(number, type, title ,content,question,choise_type);
        for(int i = 0; i<choise.size(); i++){
            page.addChoise(choise.get(i));
        }
        this.story.add(page);
    }

    public ArrayList<Page> getStory() {
        return story;
    }

    public void setStory(ArrayList<Page> story) {
        this.story = story;
    }

    public Page getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(Page current_page) {
        this.current_page = current_page;
    }

    public Stack<Integer> getHistory_page() {
        return history_page;
    }

    public void setHistory_page(Stack<Integer> history_page) {
        this.history_page = history_page;
    }

    public int getLast_page(){
        return history_page.get(history_page.size()-1);
    }

    public void addLast_page(int page_id){
        this.history_page.push(page_id);
    }

    public int removeLast_page(){
        return this.history_page.pop();
    }

}
