package com.example.stylo.bwyath;

/**
 * Created by Stylo on 29/05/2015.
 * Choose to create a combined response to target a page number
 */
public class Choise  {

    // Choise content
    private String content;
    // Choise target page number
    private int target;

    /**
     * Choise constructor
     * @param content choise content
     * @param target choise target
     */
    public Choise(String content, int target){
       this.setContent(content);
       this.setTarget(target);
    }

    /**
     * Return the choise content
     * @return content : page content
     */
    public String getContent() {
        return content;
    }

    /**
     * Edit page content
     * @param content : new page content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Return the target page number
     * @return target : taget page number
     */
    public int getTarget() {
        return target;
    }

    /**
     * Edit the target page number
     * @param target : new target page number
     */
    public void setTarget(int target) {
        this.target = target;
    }

}
