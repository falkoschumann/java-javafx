/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.javafx.viewcontroller;

import javafx.scene.*;
import javafx.stage.*;
import org.junit.*;
import org.testfx.framework.junit.*;

public class ViewControllerTest extends ApplicationTest {

    // UIWindow => Scene ? UIWindow knows the root view controller?!
    // UIView => Parent

    private ViewController viewController;

    @Override
    public void start(Stage stage) {
        viewController = new ViewController(getClass().getResource("HelloWorld.fxml"));
        Scene scene = new Scene(viewController.getView());
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void test() {
        interact(() -> {
            // TODO do things in UI thread
        });
    }

}
