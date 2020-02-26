package jerra.control;

import java.util.Set;
import java.util.HashSet;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import jerra.core.Rect;
import jerra.core.Vector;
import jerra.entity.AmbientSpawner;
import jerra.entity.Bullet;
import jerra.entity.DefaultEntity;
import jerra.entity.Player;
import jerra.presence.ActivePresence;
import jerra.presence.DefaultPresence;
import jerra.room.Room;
import jerra.view.RoomView;
import jerra.view.TextView;

/**
 * TextController
 */
public class RoomController implements Controller {

    private Room room;
    private Canvas canvas;

    private TextView textView;
    private RoomView view;

    private Set<String> heldKeys;

    public RoomController(Room room, Canvas canvas) {
        this.room = room;
        this.canvas = canvas;
        this.heldKeys = new HashSet<String>();
    }

    public void start() {
        Vector zero = new Vector(0, 0);
        Vector block = new Vector(25, 25);

        this.room.spawnEntity(new DefaultEntity(new DefaultPresence(new Rect(new Vector(100, 0), block), zero)));
		this.room.spawnEntity(new DefaultEntity(new DefaultPresence(new Rect(new Vector(200, 0), block), zero)));
		this.room.spawnEntity(new DefaultEntity(new DefaultPresence(new Rect(new Vector(0, 100), block), zero)));
		this.room.spawnEntity(new DefaultEntity(new DefaultPresence(new Rect(new Vector(0, 200), block), zero)));
        this.room.spawnEntity(new DefaultEntity(new DefaultPresence(new Rect(new Vector(125, 125), block), zero)));

        this.room.spawnSpawner(new AmbientSpawner(
            new DefaultEntity(new DefaultPresence(new Rect(new Vector(0, 0), block), zero)),
            new Vector(300, 300), 
            300, 
            25
        ));

        this.room.spawnPlayer(
            new Player(
                new ActivePresence(
                    new Rect(
                        new Vector(0, 0), block
                    ), 
                    new Vector(5, 5), "up", "down", "left", "right"
                ),
                new Bullet(
                    new Rect(
                        new Vector(0, 0), block
                    ), new Vector(30, 30), 10
                ),
                "RIGHT"
            )
        );
    
        this.view = new RoomView(this.room, this.canvas);
        view.render();

        this.textView = new TextView(this.room);
        textView.render();

        // Handle key events
        this.canvas.getScene().setOnKeyPressed(event -> this.handleKeyPress(event));
        this.canvas.getScene().setOnKeyReleased(event -> this.handleKeyRelease(event));

        // Create game loop
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        KeyFrame frame = new KeyFrame(new Duration(20), (event) -> this.update());
        gameLoop.getKeyFrames().add(frame);
        gameLoop.play();

    }

    private void handleKeyPress(KeyEvent keyCode) {     

        String key = keyCode.getCode().toString();

        switch(key) {
            case "W":
                this.heldKeys.add("up");
                break;
            case "S":
                this.heldKeys.add("down");
                break;

            case "A":
                this.heldKeys.add("left");
                break;
            case "D":
                this.heldKeys.add("right");
                break;
            case "SPACE":
                this.heldKeys.add("shoot");
                break;
            default:
                ;
        }

    }

    private void handleKeyRelease(KeyEvent key) {

        String keyString = key.getCode().toString();

        switch(keyString) {
            case "W":
                this.heldKeys.remove("up");
                break;
            case "S":
                this.heldKeys.remove("down");
                break;
            case "A":
                this.heldKeys.remove("left");
                break;
            case "D":
                this.heldKeys.remove("right");
                break;
            case "SPACE":
                this.heldKeys.remove("shoot");
                break;    
            default:
                ;
        }

    }

    private void queueKeys() {
        for (String key: this.heldKeys) {
            this.room.queue(key);
        }
    }

    private void update() {

        this.queueKeys();

        this.room.update();

        this.textView.render();

        this.view.render();

        this.room.clearQueue();

    }

}
