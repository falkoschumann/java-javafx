/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.javafx.viewcontroller;

import javafx.scene.paint.*;
import javafx.stage.*;
import org.junit.*;
import org.testfx.framework.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class ViewControllerTest extends ApplicationTest {

    private StageController stage;
    private ViewController green;
    private ViewController blue;
    private ViewController yellow;

    private List<String> viewEvents = new ArrayList<>();

    @Before
    public void setUp() {
        green = new ColoredViewController("green", Color.LIGHTGREEN, viewEvents);
        blue = new ColoredViewController("blue", Color.LIGHTBLUE, viewEvents);
        yellow = new ColoredViewController("yellow", Color.LIGHTYELLOW, viewEvents);
    }

    @Override
    public void start(Stage stage) {
        this.stage = new StageController(stage);
    }

    @Override
    public void stop() throws Exception {
        stage.hide();
    }

    @Test
    public void testRootViewController_viewEvents() {
        ViewController viewController = new HelloWorldViewController(viewEvents);
        stage.setRootViewController(viewController);
        assertEquals(Collections.emptyList(), viewEvents);

        viewController.getView();
        assertEquals(Arrays.asList("helloWorld:viewDidLoad"), viewEvents);

        interact(() -> stage.show());
        assertEquals(Arrays.asList(
                "helloWorld:viewDidLoad",
                "helloWorld:viewWillAppear",
                "helloWorld:viewDidAppear"), viewEvents);

        interact(() -> stage.hide());
        assertEquals(Arrays.asList(
                "helloWorld:viewDidLoad",
                "helloWorld:viewWillAppear",
                "helloWorld:viewDidAppear",
                "helloWorld:viewWillDisappear",
                "helloWorld:viewDidDisappear"), viewEvents);
    }

    @Test
    public void testPresent_viewControllerHierarchy() {
        stage.setRootViewController(green);
        interact(() -> stage.show());
        assertViewControllerHierarchyIsGreen();

        interact(() -> green.present(blue));
        assertViewControllerHierarchyIsGreenBlue();

        interact(() -> blue.present(yellow));
        assertViewControllerHierarchyIsGreenBlueYellow();
    }

    @Test
    public void testPresent_viewEvents() {
        // View controller hierarchy: green
        stage.setRootViewController(green);
        interact(() -> interact(() -> stage.show()));
        assertEquals(Arrays.asList(
                "green:viewDidLoad",
                "green:viewWillAppear",
                "green:viewDidAppear"), viewEvents);

        // View controller hierarchy: green -> blue
        interact(() -> green.present(blue, () -> viewEvents.add("blue:present-complete")));
        assertEquals(Arrays.asList(
                "green:viewDidLoad",
                "green:viewWillAppear",
                "green:viewDidAppear",
                "blue:viewDidLoad",
                "green:viewWillDisappear",
                "blue:viewWillAppear",
                "blue:viewDidAppear",
                "green:viewDidDisappear",
                "blue:present-complete"), viewEvents);

        // View controller hierarchy: green -> blue -> yellow
        interact(() -> blue.present(yellow, () -> viewEvents.add("yellow:present-complete")));
        assertEquals(Arrays.asList(
                "green:viewDidLoad",
                "green:viewWillAppear",
                "green:viewDidAppear",
                "blue:viewDidLoad",
                "green:viewWillDisappear",
                "blue:viewWillAppear",
                "blue:viewDidAppear",
                "green:viewDidDisappear",
                "blue:present-complete",
                "yellow:viewDidLoad",
                "blue:viewWillDisappear",
                "yellow:viewWillAppear",
                "yellow:viewDidAppear",
                "blue:viewDidDisappear",
                "yellow:present-complete"), viewEvents);
    }

    // TODO public void dismiss(Runnable completion) -> complete after viewDidDisappear

    @Test
    public void testDismissPresentedTopViewController_viewControllerHierarchy() {
        createViewControllerHierarchyGreenBlueYellow();

        interact(() -> yellow.dismiss());
        assertViewControllerHierarchyIsGreenBlue();
    }

    @Test
    public void testDismissPresentingTopViewController_viewHierarchy() {
        createViewControllerHierarchyGreenBlueYellow();

        interact(() -> blue.dismiss());
        assertViewControllerHierarchyIsGreenBlue();
    }

    @Test
    public void testDismissTopViewController_viewEvents() {
        // TODO implement test
    }

    @Test
    public void testDismissInnerNode_viewHierarchy() {
        // TODO implement test
    }

    @Test
    public void testDismissInnerNode_viewEvents() {
        // TODO implement test
    }

    @Test
    public void testToString() {
        ViewController viewController = new TestingViewController(viewEvents);

        viewController.setTitle("foo");
        assertEquals("ViewController{title='foo'}", viewController.toString());

        viewController.setTitle("bar");
        assertEquals("ViewController{title='bar'}", viewController.toString());
    }

    private void createViewControllerHierarchyGreenBlueYellow() {
        stage.setRootViewController(green);
        interact(() -> stage.show());
        interact(() -> green.present(blue));
        interact(() -> blue.present(yellow));
    }

    private void assertViewControllerHierarchyIsGreen() {
        assertSame(green, stage.getRootViewController());
        assertSame(green.getView(), stage.getStage().getScene().getRoot());

        assertNull(green.getPresentingViewController());
        assertNull(green.getPresentedViewController());
    }

    private void assertViewControllerHierarchyIsGreenBlue() {
        assertSame(green, stage.getRootViewController());
        assertSame(blue.getView(), stage.getStage().getScene().getRoot());

        assertNull(green.getPresentingViewController());
        assertSame(blue, green.getPresentedViewController());

        assertSame(green, blue.getPresentingViewController());
        assertNull(blue.getPresentedViewController());
    }

    private void assertViewControllerHierarchyIsGreenBlueYellow() {
        assertSame(green, stage.getRootViewController());
        assertSame(yellow.getView(), stage.getStage().getScene().getRoot());

        assertNull(green.getPresentingViewController());
        assertSame(blue, green.getPresentedViewController());

        assertSame(green, blue.getPresentingViewController());
        assertSame(yellow, blue.getPresentedViewController());

        assertSame(blue, yellow.getPresentingViewController());
        assertNull(yellow.getPresentedViewController());
    }

}
