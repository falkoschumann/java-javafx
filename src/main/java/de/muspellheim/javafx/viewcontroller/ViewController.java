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
 * Give location of a FXML file in constructor or override {@link #loadView()} to create view manually and set it with
 * {@link #setView(Parent)}.
 */
public class ViewController {

    private final URL location;
    private final ResourceBundle resources;

    private Parent view;

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

    public URL getLocation() {
        return location;
    }

    public Parent getView() {
        loadViewIfNeeded();
        return view;
    }

    public void setView(Parent view) {
        this.view = view;
    }

    public void loadViewIfNeeded() {
        if (!isViewLoaded())
            loadView();
    }

    public boolean isViewLoaded() {
        return view != null;
    }

    public void loadView() {
        try {
            view = FXMLLoader.load(location, resources);
        } catch (IOException ex) {
            throw new IllegalStateException("Can not load view from location " + location + ".", ex);
        }
    }

}
