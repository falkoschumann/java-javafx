/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.javafx.viewcontroller;

import java.util.*;

public class HelloWorldViewController extends TestingViewController {

    public HelloWorldViewController(List<String> viewEvents) {
        super(HelloWorldViewController.class.getResource("HelloWorld.fxml"), viewEvents);
        setTitle("helloWorld");
    }

}
