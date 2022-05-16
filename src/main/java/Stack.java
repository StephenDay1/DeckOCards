package main.java;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

public class Stack extends Movable {
    private ArrayList<Card> cards = new ArrayList<>();

    private Container buttons = new Container(
        new Button("Shuffle", this::shuffle, Screen.getInstance().width-110, Screen.getInstance().height-40, 100, 30),
        new Button("Face Up", this::flipUp, Screen.getInstance().width-220, Screen.getInstance().height-40, 100, 30),
        new Button("Face Down", this::flipDown, Screen.getInstance().width-330, Screen.getInstance().height-40, 100, 30)
    );

    public Stack(float x, float y) {
        super(x,y,Card.width,Card.height);
    }

    public static Stack createDeck(float x, float y) {
        Stack stack = new Stack(x,y);
        for (int suit = 0; suit < 4; suit++) {
            for (int i = 0; i < 14; i++) {
                Card card = new Card(i, Card.Suit.valueOf(suit), 0, 0);
                stack.addTop(card);
            }
        }
        return stack;
    }

    public void addTop(Card card) {
        card.setPosition(this.x, this.y);
        cards.add(card);
    }

    public void addBottom(Card card) {
        card.setPosition(this.x, this.y);
        cards.add(0,card);
    }

    public void addRandom(Card card) {
        card.setPosition(this.x, this.y);
        int r = (int) (Math.random()*cards.size());
        cards.add(r, card);
    }

    private Card getTop() {
        return cards.get(cards.size()-1);
    }

    private Card getBottom() {
        return cards.get(0);
    }

    public Card pullTop() {
        Card ret = getTop();
        cards.remove(ret);
        return ret;
    }

    public Card pullBottom() {
        Card ret = getBottom();
        cards.remove(ret);
        return ret;
    }

    public void shuffle() {
//        // Randomizing
//        for (int i = 0; i < (int) (Math.random()*cards.size()*2)+1; i++) {
//            int a = (int) (Math.random()*cards.size());
//            int b = (int) (Math.random()*cards.size());
//            Card A = cards.get(a);
//            Card B = cards.get(b);
//            cards.set(a,B);
//            cards.set(b,A);
//        }
        
        // Cut the Deck
        for (int i = 0; i < (int) (Math.random()*size()); i++) {
            cards.add(cards.remove(i));
        }

        // Real World Ideal Shuffling
        ArrayList<Card> bottom = new ArrayList<>();
        int half = cards.size()/2;
        for (int i = cards.size()-1; i >= half; i--) {
            bottom.add(cards.remove(i));
        }
        ArrayList<Card> top = new ArrayList<>();
        for (int i = cards.size()-1; i >= 0; i--) {
            top.add(cards.remove(i));
        }

        for (int i = top.size()-1; i >= 0; i--) {
            cards.add(top.get(i));
            cards.add(bottom.get(i));
        }
        if (bottom.size() != top.size()) {
            cards.add((bottom.get(bottom.size()-1)));
        }
    }

    public void flip() {
        // Reverse deck
        for (int i = 0; i < size(); i++) {
            cards.get(i).flip();
            cards.add(0,cards.remove(i));
        }
    }

    public void flipUp() {
        for (Card card : cards) {
            card.flipUp();
        }
    }

    public void flipDown() {
        for (Card card : cards) {
            card.flipDown();
        }
    }

    public int size() {
        return cards.size();
    }

    public void decompose() {
        Screen scr = Screen.getInstance();
        scr.items.add(pullTop());
        scr.items.remove(this);
    }

    @Override
    public void show() {
//        // SHOW TOP AT TOP COMPACTED
//        PApplet scr = Screen.getInstance();
//        if (isMoving()) {
//            scr.stroke(255,255,0);
//        } else {
//            scr.stroke(255);
//        }
//        scr.fill(200);
//        scr.rect(x,y,Card.width,Card.height*1.1f);
//
//        getTop().show();

//        // SHOW TOP AT TOP
//        float cardY = y+cards.size()*10;
//        for (Card card : cards) {
//            card.setPosition(x, cardY);
//            card.show();
//            cardY -= 10;
//        }

        // SHOW TOP AT BOTTOM
        float cardX = x;
        for (Card card : cards) {
            card.setPosition(cardX, y);
            card.show();
            cardX += 25;
        }
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
                    scr.moving.stopMoving();
                    addTop((Card) scr.moving);
                    scr.items.remove(scr.moving);
                    scr.moving = null;
                } else if (scr.moving.getClass() == Stack.class) {
                    Stack stack = (Stack) scr.moving;
                    for (int i = stack.size()-1; i >= 0; i--) {
                        addTop(stack.pullBottom());
                    }
                    scr.items.remove(stack);
                    scr.moving = null;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onRightClick() {
        Screen scr = Screen.getInstance();
        if (scr.moving == null) {
            scr.moving = pullTop();
            scr.moving.setPosition(x,y);
            if (size() < 2) {
                decompose();
            }
            scr.items.add(scr.moving);
            scr.moving.move();
        } else {
            return onLeftClick();
        }
        return true;
    }

    @Override
    public boolean onShiftLeftClick() {
        Screen scr = Screen.getInstance();
        if (!scr.buttons.contains(buttons)) {
            scr.buttons.add(buttons);
        }
        return super.onShiftLeftClick();
    }

    @Override
    public boolean onShiftRightClick() {
        Screen scr = Screen.getInstance();
        if (scr.moving == null ) {
            flip();
        }
        return super.onShiftRightClick();
    }

    @Override
    public void onMissClick() {
        Screen scr = Screen.getInstance();
        if (!buttons.inBounds(scr.mouseX, scr.mouseY)) {
            scr.buttons.remove(buttons);
        }
    }

    @Override
    public void update() {
        super.update();
        for (Card card : cards) {
            card.setPosition(x, y);
        }
    }

    @Override
    public void move() {
        super.move();
        for (Card card : cards) {
            card.move();
        }
    }

    @Override
    public void stopMoving() {
        super.stopMoving();
        for (Card card : cards) {
            card.stopMoving();
        }
    }
}
