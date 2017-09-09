/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.viewcontroller;

public class WithoutFxmlController extends ViewController {

    public WithoutFxmlController() {
        super(WithoutFxmlController.class.getResource("WithoutFxmlView.fxml"));
        setTitle("Without FXML Controller");
    }

}
