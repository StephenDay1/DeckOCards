package main.java;

import java.util.ArrayList;
import java.util.List;

public class Container extends Placeable {
    private ArrayList<Placeable> buttons = new ArrayList<>();
    public Container(float x, float y, float width, float height, Placeable ... buttons) {
        super(x,y,width,height);
        this.buttons.addAll(List.of(buttons));
    }

    public Container(Placeable ... buttons) {
        super(0,0,0,0);
        this.buttons.addAll(List.of(buttons));
    }

    @Override
    void show() {
        if (width != 0 && height != 0) {
            Screen scr = Screen.getInstance();
            scr.stroke(255);
            scr.fill(0, 59, 15);
            scr.rect(x, y, width, height);
        }
        for (Placeable button : buttons) {
            button.show();
        }
    }

    @Override
    void update() {
        for (Placeable button : buttons) {
            button.update();
        }
    }

    @Override
    public boolean inBounds(float targetX, float targetY) {
        if (width != 0 && height != 0) {
            return super.inBounds(targetX, targetY);
        } else {
            for (Placeable button : buttons) {
                if (button.inBounds(targetX, targetY)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean onLeftClick() {
        for (Placeable button : buttons) {
            Screen scr = Screen.getInstance();
            if (button.inBounds(scr.mouseX, scr.mouseY)) {
                button.onLeftClick();
            }
        }
        return super.onLeftClick();
    }
}
