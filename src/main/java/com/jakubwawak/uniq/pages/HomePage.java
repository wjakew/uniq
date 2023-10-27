/**
 * by Jakub Wawak
 * all rights reserved
 * kubawawak@gmail.com / j.wawak@usp.pl
 */
package com.jakubwawak.uniq.pages;

import com.jakubwawak.uniq.UniqApplication;
import com.jakubwawak.uniq.components.ShowHistoryWindow;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.Lumo;
import org.atmosphere.interceptor.AtmosphereResourceStateRecovery;

@PageTitle("un1q by Jakub Wawak")
@Route(value = "home")
@RouteAlias(value = "/")
@JsModule("./recipe/copytoclipboard.js")
public class HomePage extends VerticalLayout {

    H1 passwordHeader;

    Button options_button, drawagain_button,showhistory_button;

    int passwordSize;
    boolean useNumbers, usePunctuation;

    String currentPassword;

    Checkbox useNumbers_checkbox, usePuctuation_checkbox;
    IntegerField passwordSize_picker;

    VerticalLayout optionsLayout;

    /**
     * Constructor
     */
    public HomePage() {
        passwordSize = 15;
        useNumbers = true;
        usePunctuation = true;
        currentPassword = UniqApplication.rge.generateRandomString(passwordSize,useNumbers,usePunctuation);
        this.getElement().setAttribute("theme", Lumo.DARK);
        this.getStyle().set("background-color", "#000000");

        prepareView();

        setSizeFull();
        setSpacing(true);
        getThemeList().add("spacing-l");
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

    /**
     * Function for reloading password data
     */
    private void reloadPassword(){
        currentPassword = UniqApplication.rge.generateRandomString(passwordSize,useNumbers,usePunctuation);
        passwordHeader.setText(currentPassword);
    }

    /**
     * Function for preparing components
     */
    void prepareComponents(){
        passwordHeader = new H1(currentPassword);

        passwordHeader.addClickListener(e->{
            UI.getCurrent().getPage().executeJs("window.copyToClipboard($0)", passwordHeader.getText());
            Notification.show("Password copied to clipboard!");
        });

        options_button = new Button("Options", VaadinIcon.OPTION.create(),this::optionsbutton_action);
        options_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        drawagain_button = new Button("Draw Again!",VaadinIcon.REFRESH.create(),this::drawagainbutton_action);
        drawagain_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        useNumbers_checkbox = new Checkbox("Use Numbers");
        useNumbers_checkbox.setValue(useNumbers);

        useNumbers_checkbox.addValueChangeListener(e->{
            useNumbers = useNumbers_checkbox.getValue();
            reloadPassword();
        });

        usePuctuation_checkbox = new Checkbox("Use Punctuation");
        usePuctuation_checkbox.setValue(usePunctuation);

        usePuctuation_checkbox.addValueChangeListener(e->{
            usePunctuation = usePuctuation_checkbox.getValue();
            reloadPassword();
        });

        passwordSize_picker = new IntegerField();
        passwordSize_picker.setValue(passwordSize);
        passwordSize_picker.setStepButtonsVisible(true);
        passwordSize_picker.setMin(8);
        passwordSize_picker.setMax(40);

        passwordSize_picker.addValueChangeListener(e->{
            try{
                passwordSize = passwordSize_picker.getValue();
                reloadPassword();

            }catch(Exception ex){
                Notification.show("Wrong size value!");
                passwordSize_picker.setValue(15);
                reloadPassword();
            }

        });

        showhistory_button = new Button("History",VaadinIcon.ARCHIVES.create(),this::historybutton_action);
        showhistory_button.addThemeVariants(ButtonVariant.LUMO_CONTRAST,ButtonVariant.LUMO_PRIMARY);

        optionsLayout = new VerticalLayout();
        optionsLayout.add(passwordSize_picker,useNumbers_checkbox,usePuctuation_checkbox,showhistory_button);
        optionsLayout.setVisible(false);
        optionsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        optionsLayout.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        optionsLayout.getStyle().set("text-align", "center");
    }

    /**
     * Function for preparing view
     */
    void prepareView(){
        prepareComponents();
        StreamResource res = new StreamResource("uniq_icon.png", () -> {
            return HomePage.class.getClassLoader().getResourceAsStream("images/uniq_icon.png");
        });

        Image logo = new Image(res,"uniq logo");
        logo.setHeight("15rem");
        logo.setWidth("15rem");
        add(logo);
        add(new H6("UN1Q BY JAKUB WAWAK"));
        add(passwordHeader);
        add(optionsLayout);
        add(new HorizontalLayout(options_button,drawagain_button));
        add(new H6(UniqApplication.version+"/"+UniqApplication.build));
    }

    /**
     * drawagain_button action
     * @param ex
     */
    private void drawagainbutton_action(ClickEvent ex){
        reloadPassword();
    }

    /**
     * options_button action
     * @param ex
     */
    private void optionsbutton_action(ClickEvent ex){
        if ( optionsLayout.isVisible()){
            optionsLayout.setVisible(false);
        }
        else{
            optionsLayout.setVisible(true);
        }
    }

    /**
     * history_button action
     * @param ex
     */
    private void historybutton_action(ClickEvent ex){
        ShowHistoryWindow shw = new ShowHistoryWindow();
        add(shw.main_dialog);
        shw.main_dialog.open();
    }
}
