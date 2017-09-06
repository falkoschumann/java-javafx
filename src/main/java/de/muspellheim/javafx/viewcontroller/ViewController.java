/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.javafx.viewcontroller;

import javafx.beans.property.*;
import javafx.fxml.*;
import javafx.scene.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Base class for view controllers with and without FXML file.
 * <p>
 * You have the following options to use <code>ViewController</code>
 * </p>
 * <ul>
 * <li>Give location of a FXML file in constructor.</li>
 * <li>Override {@link #loadView()} to create view manually and set it with {@link #setView(Parent)}.</li>
 * <li>Set this class as FXML controller class and set root fx:id to "view".</li>
 * </ul>
 * <p>React to view events by override the methods</p>
 * <ul>
 * <li>{@link #viewDidLoad()} called after the view is loaded or created by {@link #loadView()}.</li>
 * <li>{@link #viewWillAppear()} called before the view is added to the view hierarchy.</li>
 * <li>{@link #viewDidAppear()} called after the view is added to the view hierarchy.</li>
 * <li>{@link #viewWillDisappear()} called before the view is removed to the view hierarchy.</li>
 * <li>{@link #viewDidDisappear()} called after the view is removed to the view hierarchy.</li>
 * </ul>
 */
public class ViewController {

    private final URL fxmlLocation;
    private final ResourceBundle resources;

    public ViewController() {
        this(null, null);
    }

    public ViewController(URL fxmlLocation) {
        this(fxmlLocation, null);
    }

    public ViewController(URL fxmlLocation, ResourceBundle resources) {
        this.fxmlLocation = fxmlLocation;
        this.resources = resources;
    }

    /**
     * Create a controller and load its view.
     * <p>The controller must be named <code>*Controller</code> and the view must be named <code>*View.fxml</code>. Both
     * files must be in the same package.</p>
     *
     * @param controllerType the controller type.
     * @param <T>            the controller type
     * @return the controller initialized with the view.
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


    public URL getFXMLLocation() {
        return fxmlLocation;
    }

    public ResourceBundle getResources() {
        return resources;
    }

    @FXML
    private Parent view;

    public Parent getView() {
        loadViewIfNeeded();
        return view;
    }

    public void setView(Parent view) {
        this.view = view;
    }

    public boolean isViewLoaded() {
        return viewIfLoaded() != null;
    }

    protected void loadView() {
        try {
            FXMLLoader loader = new FXMLLoader(getFXMLLocation(), getResources());
            loader.setController(this);
            loader.load();
        } catch (IOException ex) {
            throw new IllegalStateException("Can not load view from location " + getFXMLLocation() + ".", ex);
        }
    }

    protected void viewDidLoad() {
    }

    protected void loadViewIfNeeded() {
        if (!isViewLoaded()) {
            loadView();
            viewDidLoad();
        }
    }

    public Parent viewIfLoaded() {
        return view;
    }

    private StringProperty title;

    public void setTitle(String value) {
        titleProperty().setValue(value);
    }

    public String getTitle() {
        return title == null ? "" : title.getValue();
    }

    public StringProperty titleProperty() {
        if (title == null) {
            title = new SimpleStringProperty(this, "title", "");
        }
        return title;
    }

    public void present(ViewController viewControllerToPresent) {
        present(viewControllerToPresent, null);
    }

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

    public void dismiss() {
        dismiss(null);
    }

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

    protected void viewWillAppear() {
    }

    protected void viewDidAppear() {
    }

    protected void viewWillDisappear() {
    }

    protected void viewDidDisappear() {
    }

    private ViewController presentingViewController;

    public ViewController getPresentingViewController() {
        return presentingViewController;
    }

    private ViewController presentedViewController;

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
