/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.viewcontroller;

import javafx.scene.*;
import javafx.stage.*;

import java.util.*;

/**
 * Manage a stage and hold the root view controller of an view controller hierarchy.
 * <p>
 * Example usage:
 * </p>
 * <pre><code>public class ExampleApp extends Application {
 *
 * public void start(Stage stage) throws Exception {
 * StageController stageController = new StageController(stage, 800, 600);
 * ViewController rootController = ViewController.createController(ExampleController.class);
 * stageController.setRootViewController(rootController);
 * stageController.show();
 * }
 *
 * }</code></pre>
 *
 * @see ViewController
 * @see ViewController#createController(Class)
 */
public class StageController {

    private final Stage stage;
    private final double width;
    private final double height;
    private Scene scene;

    /**
     * Create a controller with a stage.
     */
    public StageController() {
        this(new Stage());
    }

    /**
     * Create a controller with a stage with given scene size.
     *
     * @param width  the width of the scene.
     * @param height the height of the scene.
     */
    public StageController(double width, double height) {
        this(new Stage(), width, height);
    }

    /**
     * Create a controller with given stage.
     *
     * @param stage a stage to control.
     */
    public StageController(Stage stage) {
        this(stage, -1, -1);
    }

    /**
     * Create a controller with given stage and size.
     *
     * @param stage  a stage to control.
     * @param width  the width of the scene.
     * @param height the height of the scene.
     */
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

    /**
     * Get the controlled stage.
     *
     * @return the stage.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Show the stage.
     */
    public void show() {
        stage.show();
    }

    /**
     * Close the stage.
     */
    public void close() {
        stage.close();
    }

    private ViewController rootViewController;

    /**
     * Set a view controller as root of view controller hierarchy of the stage.
     *
     * @param rootViewController a view controller to set as root.
     */
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

    /**
     * Get the view controller, which is the root of view controller hierarchy of the stage.
     *
     * @return the root view controller of the stage.
     */
    public final ViewController getRootViewController() {
        return rootViewController;
    }

}
