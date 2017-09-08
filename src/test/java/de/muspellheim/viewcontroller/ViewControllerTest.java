/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.viewcontroller;

import javafx.fxml.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.*;
import org.junit.*;
import org.testfx.framework.junit.*;

import java.util.*;

import static org.junit.Assert.*;

public class ViewControllerTest extends ApplicationTest {

    private StageController stageController;
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
        this.stageController = new StageController(stage);
    }

    @Override
    public void stop() throws Exception {
        stageController.close();
    }

    @Test
    public void testInitialize_withFxmlController() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WithFxmlController.fxml"));
        loader.load();
        ViewController viewController = loader.getController();

        assertNotNull(viewController.getView());
        assertTrue(viewController.getView() instanceof StackPane);
        assertEquals("With FXML Controller", viewController.getTitle());
    }

    @Test
    public void testInitialize_withoutFxmlController() throws Exception {
        ViewController viewController = new WithoutFxmlControllerViewController();

        assertNotNull(viewController.getView());
        assertTrue(viewController.getView() instanceof StackPane);
        assertEquals("Without FXML Controller", viewController.getTitle());
    }

    @Test
    public void testRootViewController_viewEvents() {
        interact(() -> stageController.setRootViewController(green));
        assertEquals(Collections.singletonList("green:viewDidLoad"), viewEvents);

        interact(() -> stageController.getStage().show());
        assertEquals(Arrays.asList(
                "green:viewDidLoad",
                "green:viewWillAppear",
                "green:viewDidAppear"), viewEvents);

        interact(() -> stageController.getStage().hide());
        assertEquals(Arrays.asList(
                "green:viewDidLoad",
                "green:viewWillAppear",
                "green:viewDidAppear",
                "green:viewWillDisappear",
                "green:viewDidDisappear"), viewEvents);
    }

    @Test
    public void testPresent_viewControllerHierarchy() {
        interact(() -> stageController.setRootViewController(green));
        interact(() -> stageController.getStage().show());
        assertViewControllerHierarchyIsGreen();

        interact(() -> green.present(blue));
        assertViewControllerHierarchyIsGreenBlue();

        interact(() -> blue.present(yellow));
        assertViewControllerHierarchyIsGreenBlueYellow();
    }

    @Test
    public void testPresent_viewEvents() {
        interact(() -> stageController.setRootViewController(green));
        interact(() -> interact(() -> stageController.getStage().show()));
        assertEquals(Arrays.asList(
                // View controller hierarchy: green
                "green:viewDidLoad",
                "green:viewWillAppear",
                "green:viewDidAppear"), viewEvents);

        interact(() -> green.present(blue, () -> viewEvents.add("green:presentBlueComplete")));
        assertEquals(Arrays.asList(
                // View controller hierarchy: green
                "green:viewDidLoad",
                "green:viewWillAppear",
                "green:viewDidAppear",
                // View controller hierarchy: green -> blue
                "blue:viewDidLoad",
                "green:viewWillDisappear",
                "blue:viewWillAppear",
                "blue:viewDidAppear",
                "green:viewDidDisappear",
                "green:presentBlueComplete"), viewEvents);

        interact(() -> blue.present(yellow, () -> viewEvents.add("blue:presentYellowComplete")));
        assertEquals(Arrays.asList(
                // View controller hierarchy: green
                "green:viewDidLoad",
                "green:viewWillAppear",
                "green:viewDidAppear",
                // View controller hierarchy: green -> blue
                "blue:viewDidLoad",
                "green:viewWillDisappear",
                "blue:viewWillAppear",
                "blue:viewDidAppear",
                "green:viewDidDisappear",
                "green:presentBlueComplete",
                // View controller hierarchy: green -> blue -> yellow
                "yellow:viewDidLoad",
                "blue:viewWillDisappear",
                "yellow:viewWillAppear",
                "yellow:viewDidAppear",
                "blue:viewDidDisappear",
                "blue:presentYellowComplete"), viewEvents);
    }

    @Test
    public void testDismissTopViewControllerByPresentedViewController_viewControllerHierarchy() {
        createViewControllerHierarchyGreenBlueYellow();

        interact(() -> yellow.dismiss());
        assertViewControllerHierarchyIsGreenBlue();
    }

    @Test
    public void testDismissTopViewControllerByPresentingViewController_viewHierarchy() {
        createViewControllerHierarchyGreenBlueYellow();

        interact(() -> blue.dismiss());
        assertViewControllerHierarchyIsGreenBlue();
    }

    @Test
    public void testDismissHiddenViewController_viewHierarchy() {
        createViewControllerHierarchyGreenBlueYellow();

        interact(() -> green.dismiss());
        assertViewControllerHierarchyIsGreen();
    }

    @Test
    public void testDismissTopViewController_viewEvents() {
        createViewControllerHierarchyGreenBlueYellow();

        interact(() -> blue.dismiss(() -> viewEvents.add("yellow:dismissComplete")));
        assertEquals(Arrays.asList(
                // View controller hierarchy: green
                "green:viewDidLoad",
                "green:viewWillAppear",
                "green:viewDidAppear",
                // View controller hierarchy: green -> blue
                "blue:viewDidLoad",
                "green:viewWillDisappear",
                "blue:viewWillAppear",
                "blue:viewDidAppear",
                "green:viewDidDisappear",
                "green:presentBlueComplete",
                // View controller hierarchy: green -> blue -> yellow
                "yellow:viewDidLoad",
                "blue:viewWillDisappear",
                "yellow:viewWillAppear",
                "yellow:viewDidAppear",
                "blue:viewDidDisappear",
                "blue:presentYellowComplete",
                // View controller hierarchy: green -> blue
                "yellow:viewWillDisappear",
                "blue:viewWillAppear",
                "blue:viewDidAppear",
                "yellow:viewDidDisappear",
                "yellow:dismissComplete"), viewEvents);
    }

    @Test
    public void testDismissHiddenViewController_viewEvents() {
        createViewControllerHierarchyGreenBlueYellow();

        interact(() -> green.dismiss(() -> viewEvents.add("green:dismissComplete")));
        assertEquals(Arrays.asList(
                // View controller hierarchy: green
                "green:viewDidLoad",
                "green:viewWillAppear",
                "green:viewDidAppear",
                // View controller hierarchy: green -> blue
                "blue:viewDidLoad",
                "green:viewWillDisappear",
                "blue:viewWillAppear",
                "blue:viewDidAppear",
                "green:viewDidDisappear",
                "green:presentBlueComplete",
                // View controller hierarchy: green -> blue -> yellow
                "yellow:viewDidLoad",
                "blue:viewWillDisappear",
                "yellow:viewWillAppear",
                "yellow:viewDidAppear",
                "blue:viewDidDisappear",
                "blue:presentYellowComplete",
                // View controller hierarchy: green
                "yellow:viewWillDisappear",
                "blue:viewWillAppear",
                "blue:viewDidAppear",
                "yellow:viewDidDisappear",
                "blue:viewWillDisappear",
                "green:viewWillAppear",
                "green:viewDidAppear",
                "blue:viewDidDisappear",
                "green:dismissComplete"), viewEvents);
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
        interact(() -> stageController.setRootViewController(green));
        interact(() -> stageController.getStage().show());
        interact(() -> green.present(blue, () -> viewEvents.add("green:presentBlueComplete")));
        interact(() -> blue.present(yellow, () -> viewEvents.add("blue:presentYellowComplete")));
    }

    private void assertViewControllerHierarchyIsGreen() {
        assertSame(green, stageController.getRootViewController());
        assertSame(green.getView(), stageController.getStage().getScene().getRoot());

        assertNull(green.getPresentingViewController());
        assertNull(green.getPresentedViewController());

        assertNull(blue.getPresentingViewController());
        assertNull(blue.getPresentedViewController());

        assertNull(yellow.getPresentingViewController());
        assertNull(yellow.getPresentedViewController());
    }

    private void assertViewControllerHierarchyIsGreenBlue() {
        assertSame(green, stageController.getRootViewController());
        assertSame(blue.getView(), stageController.getStage().getScene().getRoot());

        assertNull(green.getPresentingViewController());
        assertSame(blue, green.getPresentedViewController());

        assertSame(green, blue.getPresentingViewController());
        assertNull(blue.getPresentedViewController());

        assertNull(yellow.getPresentingViewController());
        assertNull(yellow.getPresentedViewController());
    }

    private void assertViewControllerHierarchyIsGreenBlueYellow() {
        assertSame(green, stageController.getRootViewController());
        assertSame(yellow.getView(), stageController.getStage().getScene().getRoot());

        assertNull(green.getPresentingViewController());
        assertSame(blue, green.getPresentedViewController());

        assertSame(green, blue.getPresentingViewController());
        assertSame(yellow, blue.getPresentedViewController());

        assertSame(blue, yellow.getPresentingViewController());
        assertNull(yellow.getPresentedViewController());
    }

}
