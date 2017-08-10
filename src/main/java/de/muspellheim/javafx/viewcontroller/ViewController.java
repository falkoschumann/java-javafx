/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.javafx.viewcontroller;

import javafx.fxml.*;
import javafx.scene.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Provides the infrastructure for managing the views of your JavaFX app.
 * <p>
 * You have the following options to use <code>ViewController</code>
 * <ul>
 * <li>Give location of a FXML file in constructor.</li>
 * <li>Override {@link #loadView()} to create view manually and set it with {@link #setView(Parent)}.</li>
 * <li>Set this class as FXML controller class and set root fx:id to "view".</p>
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

    private final URL location;
    private final ResourceBundle resources;

    private String title;

    @FXML
    private Parent view;

    private ViewController presentingViewController;
    private ViewController presentedViewController;

    public ViewController() {
        this(null, null);
    }

    public ViewController(URL location) {
        this(location, null);
    }

    public ViewController(URL location, ResourceBundle resources) {
        this.location = location;
        this.resources = resources;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URL getLocation() {
        return location;
    }

    public ResourceBundle getResources() {
        return resources;
    }

    public Parent viewIfLoaded() {
        return view;
    }

    public Parent getView() {
        loadViewIfNeeded();
        return view;
    }

    public void setView(Parent view) {
        this.view = view;
    }

    protected void loadViewIfNeeded() {
        if (!isViewLoaded()) {
            loadView();
            viewDidLoad();
        }
    }

    public boolean isViewLoaded() {
        return viewIfLoaded() != null;
    }

    protected void loadView() {
        try {
            setView(FXMLLoader.load(getLocation(), getResources()));
        } catch (IOException ex) {
            throw new IllegalStateException("Can not load view from location " + getLocation() + ".", ex);
        }
    }

    protected void viewDidLoad() {
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

    public ViewController getPresentingViewController() {
        return presentingViewController;
    }

    public ViewController getPresentedViewController() {
        return presentedViewController;
    }

    public void dismiss() {
        dismiss(null);
    }

    public void dismiss(Runnable completion) {
        if (getPresentedViewController() != null) {
            Scene scene = doDismiss(getPresentedViewController());
            if (completion != null)
                completion.run();
        } else if (getPresentingViewController() != null) {
            getPresentingViewController().dismiss(completion);
        }
    }

    private Scene doDismiss(ViewController viewController) {
        return doDismiss(new LinkedList<>(Collections.singleton(viewController)));
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

    @Override
    public String toString() {
        return "ViewController{" +
                "title='" + getTitle() + '\'' +
                '}';
    }

}
