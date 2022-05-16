package main.java;

import java.awt.*;

public class Button extends TextBox {
    Runnable execute;

    public Button(String text, Runnable execute, float x, float y, float width, float height) {
        super(text, x,y,width,height);
        this.execute = execute;
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
