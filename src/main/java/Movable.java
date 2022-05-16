package main.java;

public abstract class Movable extends Placeable {
    private boolean isMoving = false;
    public float mouseRelativeX = -1, mouseRelativeY = -1;

    public Movable(float x, float y, float width, float height) {
        super(x,y,width,height);
    }

    @Override
    public void update() {
        if (isMoving) {
            Screen scr = Screen.getInstance();
            float prevX = x;
            float prevY = y;
            x = mouseRelativeX + scr.mouseX;
            y = mouseRelativeY + scr.mouseY;
//            if ((prevX != x || prevY != y) && !isMoving) {
//                isMoving = true;
//            }
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean onLeftClick() {
        Screen scr = Screen.getInstance();
        if (scr.moving == null) {
            scr.moving = this;
            if (!isMoving) {
                scr.items.remove(this);
                scr.items.add(this);
                move();
            }
        } else {
            if (scr.moving == this) {
                scr.moving = null;
                stopMoving();
                return false;
            }
        }
        return true;
    }

    public void move() {
        isMoving = true;

        Screen scr = Screen.getInstance();
        mouseRelativeX = x - scr.mouseX;
        mouseRelativeY = y - scr.mouseY;
    }

    public void stopMoving() {
        isMoving = false;
    }

    public boolean isMoving() {
        return isMoving;
    }
}
