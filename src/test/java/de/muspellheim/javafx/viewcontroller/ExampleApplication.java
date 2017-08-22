/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.javafx.viewcontroller;

import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;

public class ExampleApplication extends Application {

    private ViewController green;
    private ViewController blue;
    private ViewController yellow;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setWidth(400);
        primaryStage.setHeight(300);

        green = new ViewController() {

            @Override
            protected void loadView() {
                setTitle("Green");
                FlowPane pane = new FlowPane();
                pane.setAlignment(Pos.CENTER);
                pane.setHgap(10);
                pane.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)));
                Button presentBlue = new Button("Present blue");
                presentBlue.setOnAction(e -> present(blue));
                pane.getChildren().add(presentBlue);
                setView(pane);
            }

        };

        blue = new ViewController() {

            @Override
            protected void loadView() {
                setTitle("Blue");
                FlowPane pane = new FlowPane();
                pane.setAlignment(Pos.CENTER);
                pane.setHgap(10);
                pane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
                Button presentYellow = new Button("Present yellow");
                presentYellow.setOnAction(e -> present(yellow));
                pane.getChildren().add(presentYellow);
                Button dismissBlue = new Button("Dismiss blue");
                dismissBlue.setOnAction(e -> dismiss());
                pane.getChildren().add(dismissBlue);
                setView(pane);
            }

        };

        yellow = new ViewController() {

            @Override
            protected void loadView() {
                setTitle("Yellow");
                FlowPane pane = new FlowPane();
                pane.setAlignment(Pos.CENTER);
                pane.setHgap(10);
                pane.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, null, null)));
                Button dismissYellow = new Button("Dismiss yellow");
                dismissYellow.setOnAction(e -> dismiss());
                pane.getChildren().add(dismissYellow);
                setView(pane);
            }

        };

        StageController stageController = new StageController(primaryStage);
        stageController.setRootViewController(green);

        primaryStage.show();
    }

}
