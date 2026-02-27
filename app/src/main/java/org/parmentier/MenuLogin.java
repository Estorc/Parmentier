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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;
import javafx.scene.control.Button;

public class MenuLogin extends Menu {
    private ObservableList<String> logins = FXCollections.observableArrayList();
    private final String jsonPath = "src/main/resources/userName.json";
    private org.parmentier.game.Game game;

    public MenuLogin(org.parmentier.game.Game game) {
        this.game = game;
        loadLoginsFromJson();
    }

    @FXML private TextField searchBar;
    @FXML private ListView<String> listView;
    @FXML private Button btnValider;
   
    private void loadLoginsFromJson() {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(jsonPath));
            JSONObject jo = (JSONObject) obj;
            JSONArray players = (JSONArray) jo.get("players");

            for (Object playerObj : players) {
                JSONObject player = (JSONObject) playerObj;
                String userName = (String) player.get("userName");
                logins.add(userName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    private void saveLoginsToJson() {
        try {
            JSONArray players = new JSONArray();
            for (String login : logins) {
                JSONObject p = new JSONObject();
                p.put("userName", login);
                players.add(p);
            }
            JSONObject root = new JSONObject();
            root.put("players", players);
            try (FileWriter file = new FileWriter(jsonPath)) {
                file.write(root.toJSONString());
                file.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

            btnValider.setOnAction(event -> {
                handleValidation();
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
            String s = search.toLowerCase().trim();
            ObservableList<String> res = logins.filtered(pseudo -> pseudo.toLowerCase().contains(s));

            if (res.isEmpty()) {
                listView.setItems(FXCollections.observableArrayList("Créer : " + search));
            } else {
                listView.setItems(res);
            }
        }
    }

    private void selection(String select) {
        String newName;

        if (select.startsWith("Créer : ")) {
            newName = select.replace("Créer : ", "").trim();
            if (!logins.contains(newName)) {
                logins.add(newName);
                saveLoginsToJson();
            }
        } else {
            newName = select;
        }
        searchBar.clear();
    }

    private void handleValidation() {
        String selected = listView.getSelectionModel().getSelectedItem();
        
        if (selected != null) {
            selection(selected);   
            this.game.getSceneManager().pushScene(new MenuChoiceLevel());
        }
    }
}