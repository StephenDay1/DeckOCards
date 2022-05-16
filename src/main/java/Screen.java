package main.java;

import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.util.ArrayList;

public class Screen extends PApplet {
    private static Screen m_instance;
    public static Screen getInstance() {
        if (m_instance == null) {
            m_instance = new Screen();
        }
        return m_instance;
    }
    private static void setInstance(Screen instance) {
        m_instance = instance;
    }
    public void startInstance() {
        PApplet.runSketch(new String[] {"--display=1",
                "--location=150,50",
                "--sketch-path=" + sketchPath(""),
                ""}, this);
        setInstance(this);
    }
    public static void main(String[] args) {
        Screen start = getInstance();
        start.startInstance();
    }

    @Override
    public void settings() {
//        size(800,600);
        fullScreen();
    }

    ArrayList<Movable> items = new ArrayList<>();
    ArrayList<Placeable> buttons = new ArrayList<>();
    Movable moving;
    Button settingsButton;
    Container settings;


    @Override
    public void setup() {
        stroke(255);
        strokeWeight(3);
        textAlign(CENTER, CENTER);
        textSize(15);
        imageMode(CENTER);

//        int y = 10;
//        for (int suit = 0; suit < 4; suit++) {
//            for (int i = 0; i < 14; i++) {
//                Card card = new Card(i, Card.Suit.valueOf(suit), i*50+20, y);
//                items.add(card);
//                y+=3;
//            }
//            y+=60;
//        }
        settingsButton = new Button("Settings", () -> {
            buttons.remove(settingsButton);
            buttons.add(settings);
        }, width - 80, 10, 70, 30);

        settings = new Container(width - 230, 10, 220, 130,
                new Button("Change Deck Color", ()->{
                    // TODO
                    buttons.remove(settings);
                    buttons.add(settingsButton);
                }, width - 220, 20, 200, 30),
                new Button("Change Background Color", ()->{
                    // TODO
                    buttons.remove(settings);
                    buttons.add(settingsButton);
                }, width - 220, 60, 200, 30),
                new Button("Change Stack Mode", ()->{
                    // TODO
                    buttons.remove(settings);
                    buttons.add(settingsButton);
                }, width - 220, 100, 200, 30)
        ) {
            @Override
            public void onMissClick() {
                buttons.remove(settings);
                buttons.add(settingsButton);
            }
        };


        items.add(Stack.createDeck(width/2f-Card.width/2f, height/2f-Card.height/2f));
        buttons.add(settingsButton);
    }

    @Override
    public void draw() {
        background(5, 89, 25);
        for (int i = 0; i < items.size(); i++) {
            Placeable item = items.get(i);
            item.update();
            item.show();
        }

        for (int i = 0; i < buttons.size(); i++) {
            Placeable button = buttons.get(i);
            button.update();
            button.show();
        }
    }

    @Override
    public void mousePressed(MouseEvent event) {
        boolean didNotBreak = true;
        int missClickIndex = -1;
        for (int i = items.size()-1; i >= 0; i--) {
            Placeable item = items.get(i);
            if (item.inBounds(mouseX, mouseY)) {
                boolean shouldBreak = false;
                if (keyCode == SHIFT) {
                    if (event.getButton() == LEFT) {
                        shouldBreak =  item.onShiftLeftClick();
                    }
                    if (event.getButton() == RIGHT) {
                        shouldBreak =  item.onShiftRightClick();
                    }
                } else {
                    if (event.getButton() == LEFT) {
                        shouldBreak =  item.onLeftClick();
                    }
                    if (event.getButton() == RIGHT) {
                        shouldBreak =  item.onRightClick();
                    }
                }
                if (shouldBreak) {
                    didNotBreak = false;
                    missClickIndex = i+1;
                    break;
                }
            } else {
                item.onMissClick();
            }
        }
        if (!didNotBreak) {
            for (int i = missClickIndex; i < items.size(); i++) {
                Placeable item = items.get(i);
                item.onMissClick();
            }
        }

//        if (moving == null) {
            for (int i = 0; i < buttons.size(); i++) {
                Placeable button = buttons.get(i);
                if (moving == null) {
                    if (button.inBounds(mouseX, mouseY)) {
                        button.onLeftClick();
                    } else {
                        button.onMissClick();
                    }
                } else {
                    button.onMissClick();
                }
            }
//        }

        if (didNotBreak && moving != null) {
            moving.stopMoving();
            moving = null;
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        keyCode = 0;
    }

    @Override
    public void keyTyped(KeyEvent event) {
        if (event.getKey() == '+') {
            Card.height *= 1.1f;
            Card.width *= 1.1f;
        } else if (event.getKey() == '_') {
            Card.height /= 1.1f;
            Card.width /= 1.1f;
        }
    }
}
