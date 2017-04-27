/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.javafx.viewcontroller;

import javafx.scene.*;
import javafx.stage.*;

/**
 * Manage the root view controller, show and hide a stage.
 * <p>
 * Override {@link #createScene()} to customize the scene.
 */
public class StageController {

    private final Stage stage;

    private ViewController rootViewController;

    public StageController(Stage stage) {
        this.stage = stage;

        stage.onShowingProperty().set(event -> getRootViewController().viewWillAppear());
        stage.onShownProperty().set(event -> getRootViewController().viewDidAppear());
        stage.onHidingProperty().set(event -> getRootViewController().viewWillDisappear());
        stage.onHiddenProperty().set(event -> getRootViewController().viewDidDisappear());
    }

    public Stage getStage() {
        return stage;
    }

    public ViewController getRootViewController() {
        return rootViewController;
    }

    public void setRootViewController(ViewController rootViewController) {
        this.rootViewController = rootViewController;
    }

    public void show() {
        stage.setScene(createScene());
        stage.show();
    }

    protected Scene createScene() {
        return new Scene(getRootViewController().getView());
    }

    public void hide() {
        stage.hide();
    }

}
