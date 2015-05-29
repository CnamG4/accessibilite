package com.example.stylo.bwyath;

/**
 * Created by Stylo on 29/05/2015.
 */
public class Choise  {

    private String content;
    private int target;

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
