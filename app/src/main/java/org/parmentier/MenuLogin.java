package org.parmentier;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;

public class MenuLogin extends Menu {
    private ObservableList<String> logins = FXCollections.observableArrayList("login1", "login2", "login3", "login4", "login5");

    @FXML private TextField searchBar;
    @FXML private ListView<String> listView;

    @Override
    public void levelScene(StackPane interfaceLogin) {
        try {
            URL fxmlLocation = getClass().getResource("/SceneBuilderMenuLogin.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            loader.setController(this);
            Parent root = loader.load();

            listView.setItems(logins);

            searchBar.textProperty().addListener((observable, oldVal, newVal) -> {
                FilterOrSuggestCreation(newVal);
            });

            listView.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, select) -> {
                if (select != null) {
                    selection(select);
                }
            });

            interfaceLogin.getChildren().clear();
            interfaceLogin.getChildren().add(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void FilterOrSuggestCreation(String search) {
        if (search == null || search.isEmpty()) {
            listView.setItems(logins);
        }
        else {
            String s = search.toLowerCase();
            ObservableList<String> res = logins.filtered(pseudo -> pseudo.toLowerCase().contains(s));

            if (res.isEmpty()) {
                listView.setItems(FXCollections.observableArrayList("Créer : " + search));
            } else {
                listView.setItems(res);
            }
        }
    }

    private void selection(String select) {
        if (select.startsWith("Créer : ")) {
            String newName = select.replace("Créer : ", "").trim();
            
            if (!logins.contains(newName)) {
                logins.add(newName);
                searchBar.clear();
            }
        }
        searchBar.clear();
    }
}