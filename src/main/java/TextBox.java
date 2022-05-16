package main.java;

import java.awt.*;

public class TextBox extends Placeable {
    String text;

    Color color = new Color(220,220,220);

    public TextBox(String text, float x, float y, float width, float height) {
        super(x,y,width,height);
        this.text = text;
    }

    @Override
    void show() {
        Screen scr = Screen.getInstance();
        scr.stroke(180);
        scr.fill(color.getRed(), color.getGreen(), color.getBlue());
        scr.rect(x,y,width,height);
        scr.fill(0);
        scr.text(text, x+width/2f, y+height/2f);
    }

    @Override
    void update() {}
}
