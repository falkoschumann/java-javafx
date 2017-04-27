/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.javafx.viewcontroller;

import javafx.stage.*;
import org.junit.*;
import org.testfx.framework.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class ViewControllerTest extends ApplicationTest {

    private StageController stageController;
    private ViewController viewController;

    private List<String> viewEvents = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        viewController = new TestingViewController();

        stageController = new StageController(stage);
        stageController.setRootViewController(viewController);
        stageController.show();
    }

    @Test
    public void testCallbacks_Init() {
        assertEquals(Arrays.asList("viewDidLoad", "viewWillAppear", "viewDidAppear"), viewEvents);
    }

    @Test
    public void testCallbacks_Hide() throws Exception {
        interact(() -> stageController.hide());

        assertEquals(Arrays.asList("viewDidLoad", "viewWillAppear", "viewDidAppear", "viewWillDisappear", "viewDidDisappear"), viewEvents);
    }


    private class TestingViewController extends ViewController {

        TestingViewController() {
            super(TestingViewController.class.getResource("HelloWorld.fxml"));
        }

        @Override
        public void viewDidLoad() {
            viewEvents.add("viewDidLoad");
        }

        @Override
        public void viewWillAppear() {
            viewEvents.add("viewWillAppear");
        }

        @Override
        public void viewDidAppear() {
            viewEvents.add("viewDidAppear");
        }

        @Override
        public void viewWillDisappear() {
            viewEvents.add("viewWillDisappear");
        }

        @Override
        public void viewDidDisappear() {
            viewEvents.add("viewDidDisappear");
        }

    }

}
