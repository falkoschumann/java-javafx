/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.javafx.viewcontroller;

import java.net.*;
import java.util.*;

public class TestingViewController extends ViewController {

    private final List<String> viewEvents;

    public TestingViewController(List<String> viewEvents) {
        this(null, viewEvents);
    }

    public TestingViewController(URL location, List<String> viewEvents) {
        super(location);
        this.viewEvents = viewEvents;
    }

    @Override
    public void viewDidLoad() {
        viewEvents.add(getTitle() + ":viewDidLoad");
    }

    @Override
    public void viewWillAppear() {
        viewEvents.add(getTitle() + ":viewWillAppear");
    }

    @Override
    public void viewDidAppear() {
        viewEvents.add(getTitle() + ":viewDidAppear");
    }

    @Override
    public void viewWillDisappear() {
        viewEvents.add(getTitle() + ":viewWillDisappear");
    }

    @Override
    public void viewDidDisappear() {
        viewEvents.add(getTitle() + ":viewDidDisappear");
    }

}
