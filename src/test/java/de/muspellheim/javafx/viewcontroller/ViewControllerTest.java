/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.javafx.viewcontroller;

import javafx.scene.*;
import javafx.stage.*;
import org.junit.*;
import org.testfx.framework.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class ViewControllerTest extends ApplicationTest {

    // UIWindow => Scene ? UIWindow knows the root view controller?!
    // UIView => Parent

    private ViewController viewController;

    private List<String> viewEvents = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        viewController = new TestingViewController();
        Scene scene = new Scene(viewController.getView());
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testCallbacks_init() {
        assertEquals(Arrays.asList("viewDidLoad"), viewEvents);
        // TODO viewWillAppear
        // TODO viewDidAppear
    }

    private class TestingViewController extends ViewController {

        TestingViewController() {
            super(TestingViewController.class.getResource("HelloWorld.fxml"));
        }

        @Override
        public void viewDidLoad() {
            viewEvents.add("viewDidLoad");
        }

    }

}
