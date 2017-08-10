/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.javafx.viewcontroller;

public class WithoutFxmlControllerViewController extends ViewController {

    public WithoutFxmlControllerViewController() {
        super(WithoutFxmlControllerViewController.class.getResource("WithoutFxmlController.fxml"));
        setTitle("Without FXML Controller");
    }

}
