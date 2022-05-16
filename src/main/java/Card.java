package main.java;

import processing.core.PApplet;
import processing.core.PImage;

import java.awt.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Card extends Movable {
//    private int aceValue =
    private float mouseRelativeX = -1, mouseRelativeY = -1;
    public static float width = 55.9f, height = 87.1f;
    private final String name;
    private final int value;
    private final Suit suit;
    private Face face;

    public static final Color backColor = new Color(23, 95, 163);

    public enum Suit {
        SPADES (0) {
            @Override
            public Color getColor() {
                return new Color(0);
            }
        },
        CLUBS (1) {
            @Override
            public Color getColor() {
                return new Color(0);
            }
        },
        HEARTS (2),
        DIAMONDS (3);

        private static Map<Integer, Suit> map = new HashMap<>();
        static {
            for (Suit suit : Suit.values()) {
                map.put(suit.value, suit);
            }
        }

        public Color getColor() {
            return new Color(200,30,30);
        }

        private int value;
        Suit(int i) {
            value = i;
        }

        public static Suit valueOf(int i) {
            return map.get(i);
        }
    }

    public enum Face {
        UP,
        DOWN
    }

    public static HashMap<Suit, PImage> images = new HashMap<>();
    static {
        Screen scr = Screen.getInstance();
        images.put(Suit.SPADES, scr.loadImage("main/java/resources/spades2.png"));
        images.put(Suit.CLUBS, scr.loadImage("main/java/resources/clubs2.png"));
        images.put(Suit.HEARTS, scr.loadImage("main/java/resources/hearts2.png"));
        images.put(Suit.DIAMONDS, scr.loadImage("main/java/resources/diamonds2.png"));
    }

    public Card(int value, Suit suit, float x, float y) {
        super(x,y,width,height);
        if (0 < value && value <= 10) {
            this.name = String.valueOf(value);
        } else {
            switch (value) {
                case 11 -> this.name = "J";
                case 12 -> this.name = "Q";
                case 13 -> this.name = "K";
                case 14, 0 -> this.name = "A";
                default -> this.name = "-1";
            }
        }
        this.value = value;
        this.suit = suit;
        this.face = Face.UP;
    }

    public Suit getHouse() {
        return suit;
    }

    public Face getFace() {
        return face;
    }

    @Override
    public void show() {
        PApplet scr = Screen.getInstance();
        if (isMoving()) {
            scr.stroke(255,255,0);
        } else {
            scr.stroke(255);
        }
        if (face == Face.UP) {
            scr.fill(255);
            scr.rect(x, y, width, height);
            scr.fill(suit.getColor().getRed(), suit.getColor().getGreen(), suit.getColor().getBlue());
            scr.image(images.get(suit), x + width / 2f, y + height / 2f);
            scr.text(name, x + width / 5f, y + height / 7f);
            scr.text(name, x + width * 4f / 5f, y + height * 6f / 7f);
//        }else{
//            scr.fill(suit.getColor().getRed(), suit.getColor().getGreen(), suit.getColor().getBlue());
//            scr.rect(x, y, width, height);
//            scr.fill(255);
//            scr.text(name, x + width / 5f, y + width / 6f);
        } else {
            scr.fill(backColor.getRed(), backColor.getGreen(), backColor.getBlue());
            scr.rect(x, y, width, height);
        }
    }

    public void flip() {
        if (face == Face.DOWN) {
            face = Face.UP;
        } else if (face == Face.UP) {
            face = Face.DOWN;
        }
    }

    public void flipUp() {
        face = Face.UP;
    }

    public void flipDown() {
        face = Face.DOWN;
    }

    @Override
    public boolean inBounds(float targetX, float targetY) {
        return (x < targetX && targetX < x+Card.width) &&
                (y < targetY && targetY < y+Card.height);
    }

    @Override
    public boolean onLeftClick() {
        Screen scr = Screen.getInstance();
        if (scr.moving == null) {
            scr.moving = this;
            if (!isMoving()) {
                scr.items.remove(this);
                scr.items.add(this);
                move();
            }
        } else {
            if (scr.moving == this) {
//                scr.moving = null;
                stopMoving();
                return false;
            } else {
                if (scr.moving.getClass() == Card.class) {
                    Stack stack = new Stack(this.x, this.y);
                    stack.addTop(this);
                    scr.items.remove(this);
                    stack.addTop((Card) scr.moving);
                    scr.items.remove(scr.moving);
                    scr.moving.stopMoving();
                    scr.moving = null;
                    scr.items.add(stack);
                } else if (scr.moving.getClass() == Stack.class) {
                    Stack stack = (Stack) scr.moving;
                    stack.setPosition(this.x, this.y);
                    stack.addBottom(this);
                    stack.stopMoving();
                    scr.items.remove(this);
                    scr.moving = null;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onRightClick() {
        if (Screen.getInstance().moving == null) {
            flip();
        } else {
            return onLeftClick();
        }
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + ": " + this.name + " of " + this.suit.name().toLowerCase();
    }
}
