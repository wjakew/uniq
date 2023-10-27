/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com / j.wawak@usp.pl
 */
package com.jakubwawak.uniq.components;

import com.jakubwawak.uniq.UniqApplication;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.Set;

/**
 * Object for creating window and showing history
 */
public class ShowHistoryWindow {

    public Dialog main_dialog;

    VerticalLayout main_layout;

    Grid<GridElement> history_grid;

    /**
     * Constructor
     */
    public ShowHistoryWindow(){
        main_dialog = new Dialog();
        main_layout = new VerticalLayout();
        prepareDialog();
    }

    /**
     * Function for preparing dialog window
     */
    void prepareDialog(){
        history_grid = new Grid<>(GridElement.class,false);

        ArrayList<GridElement> data = new ArrayList<>();

        for(String password : UniqApplication.rge.history){
            data.add(new GridElement(password.split("#")[1]));
        }

        history_grid.addColumn(GridElement::getGridelement_text).setHeader("Old Passwords");
        history_grid.setItems(data);
        history_grid.setWidth("100%");history_grid.setHeight("100%");

        history_grid.addSelectionListener(e->{
            Set<GridElement> selected = history_grid.getSelectedItems();
            for(GridElement element : selected){
                UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", element.gridelement_text);
                Notification.show("Password copied to clipboard!");
                break;
            }
        });

        main_layout.add(new H6("UN1Q PASSWORD HISTORY"));
        main_layout.add(history_grid);
        main_layout.setSizeFull();
        main_layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        main_layout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        main_layout.getStyle().set("text-align", "center");
        main_dialog.add(main_layout);
        main_dialog.setWidth("50%");main_dialog.setHeight("50%");
    }
}
