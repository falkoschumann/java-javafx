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
    }

    @Test
    public void testViewEvents() {
        stageController.setRootViewController(viewController);
        assertEquals(Collections.emptyList(), viewEvents);

        viewController.getView();
        assertEquals(Arrays.asList("viewDidLoad"), viewEvents);

        interact(() -> stageController.show());
        assertEquals(Arrays.asList("viewDidLoad", "viewWillAppear", "viewDidAppear"), viewEvents);

        interact(() -> stageController.hide());
        assertEquals(Arrays.asList("viewDidLoad", "viewWillAppear", "viewDidAppear", "viewWillDisappear", "viewDidDisappear"), viewEvents);
    }

    // TODO public void present(ViewController viewControllerToPresent, Runnable completion)

    @Test
    public void testToString() {
        viewController.setTitle("foo");
        assertEquals("ViewController{title='foo'}", viewController.toString());

        viewController.setTitle("bar");
        assertEquals("ViewController{title='bar'}", viewController.toString());
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
