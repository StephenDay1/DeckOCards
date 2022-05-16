package main.java;

import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import java.awt.*;
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

    ArrayList<Movable> items = new ArrayList<>();
    ArrayList<Placeable> ui = new ArrayList<>();
    Movable moving;
    Button settingsButton;
    Button infoButton;
    Container settings;
    Container stackSettings;
    TextBox info;

    private void initUI() {
        // SETTINGS
        settingsButton = new Button("Settings", () -> {
            ui.remove(settingsButton);
            ui.add(settings);
        }, width - 80, 10, 70, 30);

        settings = new Container(width - 230, 10, 220, 50,
                new Button("Change Stack Mode", ()->{
//                    ui.remove(settings);
//                    ui.add(settingsButton);
//                    Stack.stackMode = Stack.StackMode.DOWN;
                    ui.add(stackSettings);
                }, width - 220, 20, 200, 30)
        ) {
            @Override
            public void onMissClick() {
                if (!stackSettings.inBounds(mouseX, mouseY)) {
                    ui.remove(settings);
                    ui.add(settingsButton);
                }
            }
        };

        stackSettings = new Container(width - 230, 70, 220, 130,
                new Button("UP", () -> {
                    Stack.stackMode = Stack.StackMode.UP;
                }, width - 220, 80, 95, 30),
                new Button("DOWN", () -> {
                    Stack.stackMode = Stack.StackMode.DOWN;
                }, width - 220, 120, 95, 30),
                new Button("LEFT", () -> {
                    Stack.stackMode = Stack.StackMode.LEFT;
                }, width - 115, 120, 95, 30),
                new Button("RIGHT", () -> {
                    Stack.stackMode = Stack.StackMode.RIGHT;
                }, width - 115, 80, 95, 30),
                new Button("COMPACT", () -> {
                    Stack.stackMode = Stack.StackMode.COMPACT;
                }, width - 220, 160, 200, 30)
        ) {
            @Override
            public void onMissClick() {
                ui.remove(stackSettings);
            }
        };

        // INFO
        infoButton = new Button("Info", () -> {
            ui.remove(infoButton);
            ui.add(info);
        }, 10, 10, 40, 30);

        info = new TextBox(infoText, 10, 10, 300, 600) {
            @Override
            public void onMissClick() {
                if (!stackSettings.inBounds(mouseX, mouseY)) {
                    ui.remove(info);
                    ui.add(infoButton);
                }
            }
        };
    }

    @Override
    public void settings() {
//        size(800,600);
        fullScreen();
    }

    @Override
    public void setup() {
        stroke(255);
        strokeWeight(3);
        textAlign(CENTER, CENTER);
        textSize(15);
        imageMode(CENTER);

        initUI();

//        int y = 10;
//        for (int suit = 0; suit < 4; suit++) {
//            for (int i = 0; i < 14; i++) {
//                Card card = new Card(i, Card.Suit.valueOf(suit), i*50+20, y);
//                items.add(card);
//                y+=3;
//            }
//            y+=60;
//        }


        items.add(Stack.createDeck(width/2f-Card.width/2f, height/2f-Card.height/2f));
        ui.add(settingsButton);
        ui.add(infoButton);
    }

    @Override
    public void draw() {
        background(5, 89, 25);
        for (int i = 0; i < items.size(); i++) {
            Placeable item = items.get(i);
            item.update();
            item.show();
        }

        for (int i = 0; i < ui.size(); i++) {
            Placeable button = ui.get(i);
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
            for (int i = ui.size()-1; i >= 0; i--) {
                Placeable button = ui.get(i);
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

    String infoText = """
            Deck O Cards
            
            
            LEFT CLICK to move something
            
            CLICK on a card or stack while
            moving another card or stack to
            stack them together
            
            RIGHT CLICK a card to flip it
            
            SHIFT + RIGHT CLICK a stack to
            flip it
            
            RIGHT CLICK a stack to draw a card
            
            SHIFT + LEFT CLICK a stack for
            additional controls
            
            
            Stephen Day""";
}
