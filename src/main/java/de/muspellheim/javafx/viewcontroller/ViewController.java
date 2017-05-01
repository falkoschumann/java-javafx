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
 * <p> Give location of a FXML file in constructor or override {@link #loadView()} to create view
 * manually and set it with {@link #setView(Parent)}.</p>
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
            Scene scene = doDismiss(this);
            scene.setRoot(getView());
            if (completion != null)
                completion.run();
        } else if (getPresentingViewController() != null) {
            getPresentingViewController().dismiss(completion);
        }
    }

    private Scene doDismiss(ViewController viewController) {
        Scene scene;
        if (viewController.getPresentedViewController() != null) {
            scene = doDismiss(viewController.getPresentedViewController());
            viewController.getPresentedViewController().presentingViewController = null;
            viewController.viewDidAppear();
            viewController.presentedViewController.viewDidDisappear();
            viewController.presentedViewController = null;
        } else {
            viewController.viewWillDisappear();
            viewController.presentingViewController.viewWillAppear();
            viewController.presentingViewController = null;
            scene = viewController.getView().getScene();
        }
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
