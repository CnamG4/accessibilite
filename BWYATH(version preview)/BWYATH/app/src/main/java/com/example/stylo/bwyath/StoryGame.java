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
                "Page 1 - Il était une fois.",
                "Vous êtes un chevalier appelé par le roi. Il vous demande d'aller rechercher le dernier artefact manquant sur sa couronne.",
                "Par où commencer vous votre quête ? ",
                "radiobutton"
        );
        p.addChoise("Partir dans la forêt.",2);
        p.addChoise("Partir au marché.",21);
        p.addChoise("Dire au roi: Euh.. Non.",12);
        this.story.add(0,p);

        p = new Page(
                2,
                "simple",
                "Page 2 - La forêt de Kilwald.",
                "Parti en quête de l'artefact, vous voilà à l'orée de la forêt, le soleil se couche. Devant vous se dresse un animal humanoïde étrange. Il semble grogner.",
                "Que souhaitez vous faire ?",
                "radiobutton"
        );
        p.addChoise("Le contourner.", 6);
        p.addChoise("Tenter de lui parler.",3);
        p.addChoise("Lui jeter à manger.",13);
        this.story.add(1, p);

        p = new Page(
                3,
                "simple",
                "Page 3 - Le loup.",
                "Vous avez essayé d'établir le contact avec le loup. Il vous a répondu par un grognement mais parle votre langue. Après lui avoir expliqué votre quête, il vous indique quel chemin suivre.",
                "Que faites-vous ?",
                "radiobutton"
        );
        p.addChoise("Lui faire confiance.",4);
        p.addChoise("Suivre le chemin opposé.",5);
        p.addChoise("Lui proposer un morceau de viande.",13);
        this.story.add(2, p);

        p = new Page(
                4,
                "simple",
                "Page 4 - Le chemin verdoyant.",
                "Vous avez fait confiance au loup. Le chemin est dégagé, vous avancez sans difficulté vers le cœur de la forêt. La route se coupe en plusieurs chemins.",
                "Que faites vous ?",
                "radiobutton"
        );
        p.addChoise("Partir à gauche.",7);
        p.addChoise("Partir à droite.",5);
        p.addChoise("Continuer tout droit.",5);
        this.story.add(3,p);

        p = new Page(
                5,
                "simple",
                "Page 5 - Le chemin sanglant.",
                "Bien qu'il ai semblé amical, le loup reste une bête. Vous avez donc pris un autre chemin et vous voilà dans une zone bien moins amicale de la forêt.",
                "Que faites vous ?",
                "radiobutton"
        );
        p.addChoise("Faire demi-tour.",14);
        p.addChoise("Continuer tout droit.",7);
        p.addChoise("Appeler à l'aide.",16);
        this.story.add(4, p);

        p = new Page(
                6,
                "simple",
                "Page 6 - Le chemin verdoyant.",
                "Vous avez contourné l'animal. Vous arrivez sur un chemin dégagé, vert, magnifique. Vous avancez sans difficulté. La route se coupe en plusieurs chemins.",
                "Que faites vous ?",
                "radiobutton"
        );
        p.addChoise("Partir à gauche.",7);
        p.addChoise("Partir à droite.",5);
        p.addChoise("Continuer tout droit.",7);
        this.story.add(5, p);

        p = new Page(
                7,
                "simple",
                "Page 7 - Le tour du mage",
                "Vous avez traversé la forêt sans encombre. Vous arrivez maintenant au abord d'une tour relativement haute. Une lumière étrange en émane. Il semble que ce soit la demeure d'un magicien.",
                "Que faites vous ?",
                "radiobutton"
        );
        p.addChoise("Toquer à la porte.",8);
        p.addChoise("Entrer sans frappez.",15);
        p.addChoise("Hurler du bas de la tour.",8);
        this.story.add(6, p);

        p = new Page(
                8,
                "simple",
                "Page 8 - Le salon du mage",
                "Après vous être manifesté, le mage vous ouvre la porte. Après avoir discuté avec lui de votre quête il vous propose de l'aide.",
                "Que faites vous ?",
                "radiobutton"
        );
        p.addChoise("Accepter.",9);
        p.addChoise("Décliner gentiment et rire.",17);
        p.addChoise("Rire bruyament.",17);
        this.story.add(7, p);

        p = new Page(
                9,
                "simple",
                "Page 9 - Le temple de Kilroy.",
                "Vous avez accepté l'aide du mage et vous avez bien fait. Il vient de vous téléporter devant une stèle. Sur cette dernière, une gemme semble flotter.",
                "Que faites vous ?.",
                "radiobutton"
        );
        p.addChoise("Ramasser la gemme.",11);
        p.addChoise("Regarder attentivement.",10);
        p.addChoise("Fêter votre victoire.",10);
        this.story.add(8, p);

        p = new Page(
                10,
                "simple",
                "Page 10 - Le temple de Kilroy.",
                "Rien ne semble se passer et rien ne vous indique que vous êtes en danger.",
                "Que faites vous ?",
                "radiobutton"
        );
        p.addChoise("Ramasser la gemme.",11);
        p.addChoise("Ramasser la gemme et sortir.",11);
        p.addChoise("Ramasser la gemme, faire une roulade.",11);
        this.story.add(9, p);

        p = new Page(
                11,
                "simple",
                "Page 11 - Devant le temple.",
                "Vous êtes sorti, vous avez l'artefact demandé par le roi. Vous pourriez lui rapporter... Ou disparaître avec..",
                "Que vous dicte votre conscience ?",
                "radiobutton"
        );
        p.addChoise("La ramener au roi.",19);
        p.addChoise("M'enfuir avec.",20);
        p.addChoise("La manger.",18);
        this.story.add(10, p);

        p = new Page(
                12,
                "end",
                "Page 12 - MORT !",
                "Suite à votre refus, le roi vous a fait tuer.",
                "",
                ""
        );
        this.story.add(11, p);

        p = new Page(
                13,
                "end",
                "Page 13 - MORT !.",
                "En jetant à manger au loup-garou, vous lui avez ouvert l'appetit… On vous laisse deviner ce qu'il a préféré entre votre petit sandwich et vous.",
                "",
                ""
        );
        this.story.add(12, p);

        p = new Page(
                14,
                "end",
                "Page 14 - MORT !.",
                "La présence que vous sentiez derrière vous sur le sentier n'était autre que les lianes d'une plante carnivore. Vous êtes pris au piège",
                "",
                ""
        );
        this.story.add(13, p);

        p = new Page(
                15,
                "end",
                "Page 15 - MORT !.",
                "Vous êtes rentré chez le mage sans frapper. Bien sur, un puissant sortillège protegez les lieux. Vous avez été réduit en cendre.",
                "",
                ""
        );
        this.story.add(14, p);

        p = new Page(
                16,
                "end",
                "Page 16 - MORT !.",
                "On ne crie pas comme ça dans la forêt vindiou ! Vous avez attiré un bon nombre de créatures hostiles. L'une d'entre elle n'a eu qu'à ouvrir la bouche pour vous y faire entrer tout entier.",
                "",
                ""
        );
        this.story.add(15, p);

        p = new Page(
                17,
                "end",
                "Page 17 - MORT !.",
                "On ne rit pas à la barbe d'un magicien … Il vous transforme en pâté pour son fidèle dragon.",
                "",
                ""
        );
        this.story.add(16, p);

        p = new Page(
                18,
                "end",
                "Page 18 - MORT !.",
                "Vous venez de manger la gemme. Après avoir essayé de la macher, vous avez tenté de l'avaler d'une traite. Vous mourrez étouffé.",
                "",
                ""
        );
        this.story.add(17, p);

        p = new Page(
                19,
                "end",
                "Page 19 - Félicitations !.",
                "Vous avez apporté au roi la gemme qu'il attendait et plus encore. Il vous couvre d'or, vous offre une aile de son château pour que vous puissez finir vos jours tranquilles…",
                "",
                ""
        );
        this.story.add(18, p);

        p = new Page(
                20,
                "end",
                "Page 20 - Félicitations !",
                "Vous avez décider de garder la gemme pour vous. Ce n'est pas vraiment ce qui était prévu.",
                "",
                ""
        );
        this.story.add(19, p);


        p = new Page(
                21,
                "simple",
                "Page 21 - Le marché.",
                "Félicitations, vous vous êtes bien équipé avant de partir à l'aventure. Votre préparation est terminée.",
                "Que faites vous ?",
                "radiobutton"
        );
        p.addChoise("Partir dans la forêt.",2);
        p.addChoise("Dire au roi: Euh.. Non.",12);
        p.addChoise("Bon, vous y aller dans la forêt ?",2);
        this.story.add(20, p);

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
