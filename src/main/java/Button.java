package main.java;

import java.awt.*;

public class Button extends Placeable {
    String text;
    Runnable execute;

    Color color = new Color(220,220,220);

    public Button(String text, Runnable execute, float x, float y, float width, float height) {
        super(x,y,width,height);
        this.text = text;
        this.execute = execute;
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
    void update() {
        Screen scr = Screen.getInstance();
        if (inBounds(scr.mouseX, scr.mouseY)) {
            if (!color.equals(Color.WHITE)) {
                color = Color.WHITE;
            }
        } else {
            if (color.equals(Color.WHITE)) {
                color = new Color(220,220,220);
            }
        }
    }

    @Override
    public boolean onLeftClick() {
        execute.run();
        return super.onLeftClick();
    }
}
