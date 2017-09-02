/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.javafx.viewcontroller;

import javafx.scene.*;
import javafx.stage.*;

import java.util.*;

public class StageController {

    private final Stage stage;
    private final double width;
    private final double height;
    private Scene scene;

    public StageController() {
        this(new Stage());
    }

    public StageController(double width, double height) {
        this(new Stage(), width, height);
    }

    public StageController(Stage stage) {
        this(stage, -1, -1);
    }

    public StageController(Stage stage, double width, double height) {
        this.stage = Objects.requireNonNull(stage, "stage");
        this.width = width;
        this.height = height;

        stage.onShowingProperty().set(event -> existRootViewController(() -> getRootViewController().viewWillAppear()));
        stage.onShownProperty().set(event -> existRootViewController(() -> getRootViewController().viewDidAppear()));
        stage.onHidingProperty().set(event -> existRootViewController(() -> getRootViewController().viewWillDisappear()));
        stage.onHiddenProperty().set(event -> existRootViewController(() -> getRootViewController().viewDidDisappear()));
    }

    private void existRootViewController(Runnable ifTrue) {
        if (getRootViewController() != null)
            ifTrue.run();
    }

    public Stage getStage() {
        return stage;
    }

    public void show() {
        stage.show();
    }

    public void close() {
        stage.close();
    }

    private ViewController rootViewController;

    public final void setRootViewController(ViewController rootViewController) {
        this.rootViewController = Objects.requireNonNull(rootViewController, "rootViewController");
        stage.titleProperty().bind(rootViewController.titleProperty());

        if (scene == null) {
            scene = new Scene(rootViewController.getView(), width, height);
            stage.setScene(scene);
        } else {
            scene.setRoot(rootViewController.getView());
        }
    }

    public final ViewController getRootViewController() {
        return rootViewController;
    }

}
