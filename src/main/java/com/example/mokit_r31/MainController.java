package com.example.mokit_r31;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.util.List;

/** Ohjelman käyttöliittymää hallinnoiva controller-luokka.
 * Täällä on määritetty välilehdet ja niillä siirtyminen nuolinäppäimillä.
 */
@SuppressWarnings("unused")
public class MainController {

    @FXML private Tab tabAsiakkaat;
    @FXML private Tab tabMokit;
    @FXML private Tab tabVaraukset;
    @FXML private Tab tabPalvelut;
    @FXML private Tab tabLaskutus;
    @FXML private Tab tabRaportit;
    @FXML private Tab tabPalveluRaportti;
    @FXML private TabPane mainTabPane;

    public void initialize() {
        navigointiTabeissaNuolinappaimilla();
    }

    // Tässä säädetään, että silloin, kun fokus on välilehdissä, niillä voi siirtyä nuolinäppämillä oikea/vasen
    private void navigointiTabeissaNuolinappaimilla() {

        List<Tab> tabsList = mainTabPane.getTabs();
        var ref = new Object() {
            int currentTabIndex = mainTabPane.getSelectionModel().getSelectedIndex();
        };

        mainTabPane.setOnKeyPressed(event -> {
            if (mainTabPane.isFocused()) {
                KeyCode code = event.getCode();
                if (code == KeyCode.LEFT || code == KeyCode.RIGHT) {
                    int newIndex = code == KeyCode.LEFT ? ref.currentTabIndex - 1 : ref.currentTabIndex + 1;
                    if (newIndex < 0) {
                        newIndex = tabsList.size() - 1;
                    } else if (newIndex >= tabsList.size()) {
                        newIndex = 0;
                    }
                    mainTabPane.getSelectionModel().select(newIndex);
                    ref.currentTabIndex = newIndex;
                    event.consume();
                }
            }
        });
}
}
