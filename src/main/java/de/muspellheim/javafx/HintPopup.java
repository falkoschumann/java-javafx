/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.javafx;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;

/**
 * This popup can display an hint how to fill out a control.
 * <p>
 * Usually a hint popup show a message that explain invalid input of a text field or other controls. The popup show near
 * a owner control.
 * </p>
 * <p>
 * Usage: Bind a valid flag on show and hide this popup.
 * </p>
 *
 * @see #show(String, Region)
 */
public class HintPopup extends Popup {

    private final Label label;

    /**
     * Create a hint popup.
     */
    public HintPopup() {
        label = new Label();
        StackPane stack = new StackPane();
        stack.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, null, null)));
        stack.getChildren().add(label);
        getContent().add(stack);
    }

    /**
     * Show this popup with a hint text.
     *
     * @param text  the hint.
     * @param owner the owner of this popup.
     */
    public void show(String text, Region owner) {
        label.setText(text);
        Point2D location = getLocation(owner);
        show(owner, location.getX(), location.getY());
    }

    private static Point2D getLocation(Region owner) {
        Scene scene = owner.getScene();
        Window window = scene.getWindow();
        return owner.localToScene(0, 0)
                .add(0, owner.getHeight() + 5)
                .add(scene.getX(), scene.getY())
                .add(window.getX(), window.getY());
    }

}
