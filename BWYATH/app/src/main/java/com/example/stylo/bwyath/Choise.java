package com.example.stylo.bwyath;

/**
 * Created by Stylo on 29/05/2015.
 * Choix permettant de créer une réponse associé au numéro de page cible de cette dernière
 */
public class Choise  {

    // Contenu de la réponse
    private String content;
    // Page ciblée par ce choix
    private int target;

    /**
     * Constructeur de choix
     * @param content Contenu de la réponse
     * @param target Page ciblée par ce choix
     */
    public Choise(String content, int target){
       this.setContent(content);
       this.setTarget(target);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

}
