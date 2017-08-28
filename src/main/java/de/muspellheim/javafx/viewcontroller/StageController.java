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
    private Scene scene;

    public StageController(Stage stage) {
        this.stage = Objects.requireNonNull(stage, "stage");
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

    public void close() {
        stage.close();
    }

    private ViewController rootViewController;

    public final void setRootViewController(ViewController rootViewController) {
        this.rootViewController = Objects.requireNonNull(rootViewController, "rootViewController");

        if (scene == null) {
            scene = new Scene(rootViewController.getView());
            stage.setScene(scene);
        } else {
            scene.setRoot(rootViewController.getView());
        }
    }

    public final ViewController getRootViewController() {
        return rootViewController;
    }

}
