/** ********************************************************************************
 * Represents the login menu in the Parmentier puzzle game, allowing users to select or create a username.
 ***********************************************************************************
 * @author Estorc
 * @version v1.0
 * @package org.parmentier
 * @copyright Copyright (c) 2026 Parmentier's team GNU GENERAL PUBLIC LICENSE.
 **********************************************************************************/
/*                             This file is part of
 *                                  Parmentier
 *           (https://github.com/Estorc/Projet-Genie-Logiciel-L3-Parmentier)
 ***********************************************************************************/

package org.parmentier;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.canvas.GraphicsContext;
import java.io.IOException;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;
import org.parmentier.game.Game;
import org.parmentier.game.MainMenu;

/**
 * Represents the login menu in the Parmentier puzzle game, allowing users to select or create a username.
 * The menu loads existing usernames from a JSON file and provides functionality to filter and suggest usernames based on user input.
 */
public class MenuLogin extends Menu {

    /**
     * An observable list that holds the usernames loaded from the JSON file. This list is used to populate the ListView in the login menu.
     */
    private ObservableList<String> logins = FXCollections.observableArrayList();

    /**
     * The path to the JSON file that contains the usernames.
     */
    private final String jsonPath = "src/main/resources/userName.json";
    
    /**
     * A reference to the Game instance, allowing the menu to interact with the game state and transition to other scenes.
     */
    private Game game;

    /**
     * Constructs a new MenuLogin instance with a reference to the Game object. The constructor also loads existing usernames from the JSON file.
     * @param game The Game instance that this menu will interact with.
     */
    public MenuLogin(Game game) {
        this.game = game;
        loadLoginsFromJson();
    }

    /**
     * FXML annotations for the UI components defined in the corresponding FXML file.
     * These components include a TextField for the search bar, a ListView to display usernames, and a Button for validation.
     */
    @FXML private TextField searchBar;

    /** 
     * The ListView component that displays the list of usernames.
     * It is populated with the 'logins' observable list, which is updated based on user input in the search bar.
     */
    @FXML private ListView<String> listView;

    /**
     * The Button component that triggers the validation of the selected username.
     * When clicked, it checks the selected item in the ListView and either creates a new username or selects an existing one,
     * then transitions to the main menu.
     */
    @FXML private Button btnValider;
   
    /**
     * Loads the usernames from the specified JSON file and populates the 'logins' observable list.
     * The JSON file is expected to have a structure where usernames are stored under a "players" array.
     * If there is an error during the loading process (e.g., file not found, parsing error),
     * the exception is caught and its stack trace is printed to the standard error stream.
     */
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
 
    /**
     * Saves the current list of usernames to the specified JSON file.
     * The usernames are stored under a "players" array in the JSON structure.
     * If there is an error during the saving process (e.g., file writing error),
     * the exception is caught and its stack trace is printed to the standard error stream.
     */
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

    /**
     * Initializes the login menu by loading the corresponding FXML layout and setting up event listeners for the search bar and validation button.
     * The method also populates the ListView with the usernames loaded from the JSON file and applies the necessary stylesheets to the interface.
     * @param interfaceLogin The StackPane to which the login menu will be added. This pane is cleared before adding the new menu layout.
     * @exception IOException If there is an error loading the FXML file,
     * the exception is caught and its stack trace is printed to the standard error stream.
     */
    @Override
    public void initialize(StackPane interfaceLogin) {
        try {
            URL fxmlLocation = getClass().getResource("/SceneBuilderMenuLogin.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            loader.setController(this);
            Parent root = loader.load();

            interfaceLogin.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());

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

    /**
     * Updates the state of the login menu.
     * Since the login menu does not have any dynamic elements that require updating over time, this method is left empty.
     * @param deltaTime The time elapsed since the last update, which is not used in this menu.
     * @param uiLayer The StackPane that serves as the UI layer for the menu, which is not modified in this method.
     */
    @Override
    public void update(double deltaTime, StackPane uiLayer) {
        //
    }

    /**
     * Renders the login menu.
     * Since the login menu is primarily defined by its FXML layout and does not have custom rendering logic, this method is left empty.
     * @param gc The GraphicsContext used for rendering, which is not utilized in this menu.
     * @param uiLayer The StackPane that serves as the UI layer for the menu, which is not modified in this method.
     */
    @Override
    public void render(GraphicsContext gc, StackPane uiLayer){
        //
    }

    /**
     * Destroys the login menu by saving the current list of usernames to the JSON file.
     * This ensures that any new usernames created during the session are persisted for future use.
     */
    @Override
    public void destroy() {
        saveLoginsToJson();
    }

    /**
     * Filters the list of usernames based on the user's input in the search bar.
     * If the search input is empty, it displays all usernames.
     * If the search input does not match any existing usernames, it suggests creating a new username with the given input.
     * The method updates the ListView to reflect the filtered or suggested usernames.
     *
     * @param search The user's input from the search bar, which is used to filter existing usernames or suggest creating a new one.
     */
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

    /**
     * Handles the selection of a username from the ListView.
     * If the selected item indicates the creation of a new username, it adds the new username to the list and saves it to the JSON file.
     * If an existing username is selected, it simply uses that username.
     * After processing the selection, it clears the search bar and transitions to the main menu.
     *
     * @param select The selected item from the ListView, which can either be an existing username or a suggestion to create a new one.
     */
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

    /**
     * Handles the validation of the selected username when the validation button is clicked.
     * It retrieves the selected item from the ListView and processes it using the selection method.
     * If a valid selection is made, it sets the current username in the game and transitions to the main menu.
     */
    private void handleValidation() {
        String selected = listView.getSelectionModel().getSelectedItem();
        
        if (selected != null) {
            selection(selected);   

            String finalName = selected.replace("Créer : ", "").trim();
            this.game.setCurrentUserName(finalName);

            this.game.getSceneManager().pushScene(new MainMenu());
        }
    }
}
