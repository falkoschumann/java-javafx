/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.viewcontroller;

import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.scene.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Base class for view controllers with or without FXML file.
 * <p>
 * You have the following options to use <code>ViewController</code>
 * </p>
 * <ul>
 * <li>Set this class as FXML controller class and set roots fx:id to <em>view</em>.</li>
 * <li>Give location of a FXML file in constructor, root fx:id must be <em>view</em>.</li>
 * <li>Override {@link #loadView()} to create view manually and set it with {@link #setView(Parent)}.</li>
 * </ul>
 * <p>
 * Respond to view events by override following methods
 * </p>
 * <ul>
 * <li>{@link #viewDidLoad()}</li>
 * <li>{@link #viewWillAppear()}</li>
 * <li>{@link #viewDidAppear()}</li>
 * <li>{@link #viewWillDisappear()}</li>
 * <li>{@link #viewDidDisappear()}</li>
 * </ul>
 *
 * @see #createController(Class)
 */
public class ViewController {

    private final URL fxmlLocation;
    private final ResourceBundle resources;

    /**
     * Create controller with FXML view or manual without FXML.
     * <p>
     * If you use the controller for an FXML view, the attribute <code>fx:controller</code> in FXML must be set.
     * </p>
     * <p>
     * If you create the view manual, overwrite {@link #loadView()}.
     * </p>
     */
    public ViewController() {
        this(null, null);
    }

    /**
     * Create controller with given view.
     * <p>
     * The attribute <code>fx:controller</code> in FXML must not be set.
     * </p>
     *
     * @param fxmlLocation URL of a FXML view.
     */
    public ViewController(URL fxmlLocation) {
        this(fxmlLocation, null);
    }

    /**
     * Create controller with given view and resource bundle.
     * <p>
     * The attribute <code>fx:controller</code> in FXML must not be set.
     * </p>
     *
     * @param fxmlLocation URL of a FXML view.
     * @param resources    the resource bundle to translate the view and controller.
     */
    public ViewController(URL fxmlLocation, ResourceBundle resources) {
        this.fxmlLocation = fxmlLocation;
        this.resources = resources;
    }

    /**
     * Create a controller and load its view by convention.
     * <p>
     * The controller must be named <code>{Foo}Controller</code> and the view must be named <code>{Foo}View.fxml</code>.
     * Both files must be in the same package.
     * </p>
     *
     * @param controllerType the controller type.
     * @param <T>            the type of the controller.
     * @return a controller instance with loaded and initialized view.
     */
    public static <T extends ViewController> T createController(Class<T> controllerType) {
        String viewname = controllerType.getSimpleName().replace("Controller", "View") + ".fxml";
        URL location = controllerType.getResource(viewname);
        FXMLLoader loader = new FXMLLoader(location);
        try {
            loader.load();
        } catch (IOException ex) {
            throw new IllegalStateException("Can not load view from location " + location + ": " + ex, ex);
        }
        return loader.getController();
    }

    /**
     * Get the URL of FXML view.
     *
     * @return the URL or <code>null</code> if no FXML is used.
     */
    public URL getFXMLLocation() {
        return fxmlLocation;
    }

    /**
     * Get the resource bundle of view and controller.
     *
     * @return the resource bundle or <code>null</code> if no resource bundle is used.
     */
    public ResourceBundle getResources() {
        return resources;
    }

    @FXML
    private Parent view;

    /**
     * Get the controlled view.
     * <p>
     * Load the view if not loaded.
     * </p>
     *
     * @return the loaded and initialized view.
     */
    public Parent getView() {
        loadViewIfNeeded();
        return view;
    }

    /**
     * Set the view of this controller.
     * <p>
     * Only need if create view manual with {@link #loadView()}.
     * </p>
     *
     * @param view a view.
     */
    public void setView(Parent view) {
        this.view = view;
    }

    /**
     * Ask if view is loaded, but do not load view.
     *
     * @return <code>true</code> if view is loaded, <code>false</code> if not.
     */
    public boolean isViewLoaded() {
        return viewIfLoaded() != null;
    }

    /**
     * Load or create the view.
     * <p>
     * The default implementation load a FXML view from {@link #getFXMLLocation()}.
     * </p>
     * <p>
     * If you want do create the view manual, overwrite this method with view creation and set view with
     * {@link #setView(Parent)}.
     * </p>
     */
    protected void loadView() {
        try {
            FXMLLoader loader = new FXMLLoader(getFXMLLocation(), getResources());
            loader.setController(this);
            loader.load();
        } catch (IOException ex) {
            throw new IllegalStateException("Can not load view from location " + getFXMLLocation() + ".", ex);
        }
    }

    /**
     * Called after the view is loaded or created by {@link #loadView()}.
     */
    protected void viewDidLoad() {
    }

    /**
     * Load lazy the view.
     * <p>
     * If view is load this method does nothing.
     * </p>
     */
    protected void loadViewIfNeeded() {
        if (!isViewLoaded()) {
            loadView();
            viewDidLoad();
        }
    }

    /**
     * Get the view if loaded, but do not load the view.
     *
     * @return the loaded view or <code>null</code> if view is not loaded.
     */
    public Parent viewIfLoaded() {
        return view;
    }

    private StringProperty title;

    /**
     * The view title.
     * <p>
     * If this is a root view controller, the title is show in the window title bar of the stage.
     * </p>
     *
     * @return view title property.
     */
    public StringProperty titleProperty() {
        if (title == null) {
            title = new SimpleStringProperty(this, "title", "");
        }
        return title;
    }

    /**
     * Set the views title.
     *
     * @param value the new view title.
     */
    public void setTitle(String value) {
        titleProperty().setValue(value);
    }

    /**
     * Get the view title.
     *
     * @return the current view title.
     */
    public String getTitle() {
        return title == null ? "" : title.getValue();
    }

    /**
     * Presents a view controller modally.
     *
     * @param viewControllerToPresent the view controller to display over the current view controllers content.
     */
    public void present(ViewController viewControllerToPresent) {
        present(viewControllerToPresent, null);
    }

    /**
     * Presents a view controller modally.
     * <p>
     * Several view controller can present in a stack, only the the top view controller is visible.
     * </p>
     *
     * @param viewControllerToPresent the view controller to display over the current view controllers content.
     * @param completion              execute after the presentation finished.
     */
    public void present(ViewController viewControllerToPresent, Runnable completion) {
        Parent viewToPresent = viewControllerToPresent.getView();
        viewWillDisappear();
        viewControllerToPresent.viewWillAppear();
        getView().getScene().setRoot(viewToPresent);
        presentedViewController = viewControllerToPresent;
        viewControllerToPresent.presentingViewController = this;
        viewControllerToPresent.viewDidAppear();
        viewDidDisappear();

        if (completion != null)
            completion.run();
    }

    /**
     * Dismisses this view controller.
     * <p>
     * If this is not the top view controller, dismiss all view controller over this view controller also.
     * </p>
     */
    public void dismiss() {
        dismiss(null);
    }

    /**
     * Dismisses this view controller.
     * <p>
     * If this is not the top view controller, dismiss all view controller over this view controller also.
     * </p>
     *
     * @param completion execute after the view controller is dismissed.
     */
    public void dismiss(Runnable completion) {
        if (getPresentedViewController() != null) {
            doDismiss(getPresentedViewController());
            if (completion != null)
                completion.run();
        } else if (getPresentingViewController() != null) {
            getPresentingViewController().dismiss(completion);
        }
    }

    private void doDismiss(ViewController viewController) {
        doDismiss(new LinkedList<>(Collections.singleton(viewController)));
    }

    private Scene doDismiss(Deque<ViewController> viewControllers) {
        Scene scene;
        ViewController lastViewController = viewControllers.peekLast();
        if (lastViewController.getPresentedViewController() != null) {
            viewControllers.offerLast(lastViewController.getPresentedViewController());
            scene = doDismiss(viewControllers);
        } else {
            scene = lastViewController.getView().getScene();
        }

        ViewController disapperingViewController = viewControllers.pollLast();
        ViewController apperingViewController = disapperingViewController.getPresentingViewController();

        disapperingViewController.viewWillDisappear();
        apperingViewController.viewWillAppear();

        disapperingViewController.presentingViewController = null;
        apperingViewController.presentedViewController = null;
        if (viewControllers.isEmpty())
            scene.setRoot(getView());

        apperingViewController.viewDidAppear();
        disapperingViewController.viewDidDisappear();

        return scene;
    }

    /**
     * Called before the view is added to the view hierarchy.
     */
    protected void viewWillAppear() {
    }

    /**
     * Called after the view is added to the view hierarchy.
     */
    protected void viewDidAppear() {
    }

    /**
     * Called before the view is removed to the view hierarchy.
     */
    protected void viewWillDisappear() {
    }

    /**
     * Called after the view is removed to the view hierarchy.
     */
    protected void viewDidDisappear() {
    }

    private ViewController presentingViewController;

    /**
     * Get the view controller presenting this view controller.
     *
     * @return the presenting view controller or <code>null</code> if no presenting view controller exists.
     */
    public ViewController getPresentingViewController() {
        return presentingViewController;
    }

    private ViewController presentedViewController;

    /**
     * Get the view controller presented by this view controller.
     *
     * @return the presented view controller or <code>null</code> if this view controller present no view controller.
     */
    public ViewController getPresentedViewController() {
        return presentedViewController;
    }

    @Override
    public String toString() {
        return "ViewController{" +
                "title='" + getTitle() + '\'' +
                '}';
    }

}
