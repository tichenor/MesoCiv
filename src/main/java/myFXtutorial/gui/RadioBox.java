package main.java.myFXtutorial.gui;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * Container for displaying various text information about the game and game events, such as unlocking a new
 * achievement, reaching a milestone or fluff. Wrapper for a VBox component.
 */
public class RadioBox {

    private final VBox box;

    private final int maxNodes = 5;
    private final Queue<Label> queue = new LinkedList<>();
    private final Map<Label, SequentialTransition> transitions = new HashMap<>();

    public RadioBox(VBox vBox) {
        this.box = vBox;
    }

    public void submit(Label l, SequentialTransition st) {
        // If max allowed messages are in the VBox, store the text label and transition and return
        if (box.getChildren().size() >= maxNodes) {
            queue.add(l);
            transitions.put(l, st);
            return;
        }
        l.setRotate(180);
        box.getChildren().add(0, l);
        // Play the transition (animation)
        st.play();
    }

    public void update() {
        while (!queue.isEmpty() && box.getChildren().size() < 5) {
            Label next = queue.remove();
            next.setRotate(180);
            box.getChildren().add(0, next);
            transitions.remove(next).play();
        }
    }

    public SequentialTransition createTransition(Label l, int pauseDur, int fadeDur, int fadeFrom, int fadeTo) {
        PauseTransition pause = new PauseTransition(Duration.seconds(pauseDur));
        FadeTransition fade = new FadeTransition(Duration.seconds(fadeDur));
        fade.setFromValue(fadeFrom);
        fade.setToValue(fadeTo);
        fade.setOnFinished(event -> box.getChildren().remove(l));
        SequentialTransition sequence = new SequentialTransition(
                l,
                pause,
                fade
        );
        return sequence;
    }
}
