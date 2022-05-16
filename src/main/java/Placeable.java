package main.java;

public abstract class Placeable {
    public float x,y,width,height;
//    private boolean isMoving = false;
//    public float mouseRelativeX = -1, mouseRelativeY = -1;
    public Placeable(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    abstract void show();

    abstract void update();
//    public void update() {
//        if (isMoving) {
//            Screen scr = Screen.getInstance();
//            float prevX = x;
//            float prevY = y;
//            x = mouseRelativeX + scr.mouseX;
//            y = mouseRelativeY + scr.mouseY;
////            if ((prevX != x || prevY != y) && !isMoving) {
////                isMoving = true;
////            }
//        }
//    }

//    public void setPosition(float x, float y) {
//        this.x = x;
//        this.y = y;
//    }

    public boolean inBounds(float targetX, float targetY) {
        return (x < targetX && targetX < x+width) &&
                (y < targetY && targetY < y+height);
    }

    public boolean onLeftClick() {
//        Screen scr = Screen.getInstance();
//        if (scr.moving == null) {
//            scr.moving = this;
//            if (!isMoving) {
//                scr.items.remove(this);
//                scr.items.add(this);
//                move();
//            }
//        } else {
//            if (scr.moving == this) {
//                scr.moving = null;
//                stopMoving();
//                return false;
//            }
//        }
        return true;
    }

    public boolean onRightClick() {
        return true;
    }

    public boolean onShiftLeftClick() {
        return true;
    }

    public boolean onShiftRightClick() {
        return true;
    }

    public void onMissClick() {}

//    public void move() {
//        isMoving = true;
//
//        Screen scr = Screen.getInstance();
//        mouseRelativeX = x - scr.mouseX;
//        mouseRelativeY = y - scr.mouseY;
//    }
//
//    public void stopMoving() {
//        isMoving = false;
//    }
//
//    public boolean isMoving() {
//        return isMoving;
//    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
