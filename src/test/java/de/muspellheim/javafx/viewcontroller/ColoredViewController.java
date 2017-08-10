/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.javafx.viewcontroller;

import javafx.scene.layout.*;
import javafx.scene.paint.*;

import java.util.*;

public class ColoredViewController extends TestingViewController {

    private final Paint color;

    public ColoredViewController(String title, Paint color, List<String> viewEvents) {
        super(viewEvents);
        setTitle(title);
        this.color = color;
    }

    @Override
    protected void loadView() {
        Region region = new Region();
        region.setBackground(new Background(new BackgroundFill(color, null, null)));
        setView(region);
    }

}
