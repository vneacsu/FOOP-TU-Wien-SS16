package at.foop16.player.ui;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import at.foop16.events.AwaitNewGameEvent;
import at.foop16.events.LeaveActiveGameEvent;
import at.foop16.model.Maze;
import at.foop16.model.fields.Field;
import at.foop16.player.service.GamePlayerActor;
import at.foop16.player.service.GameStateListener;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import javax.inject.Inject;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController implements Initializable, GameStateListener {

    @Inject
    private ActorSystem actorSystem;

    private volatile ActorRef player;

    @FXML
    private ListView<ActorRef> activePlayersList;

    private final ObservableList<ActorRef> activePlayers = FXCollections.observableArrayList();

    @FXML
    private Label loadingLabel;
    @FXML
    private FlowPane newGamePanel;
    @FXML
    private GridPane playGamePanel;
    @FXML
    private GridPane mazePanel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        player = actorSystem.actorOf(Props.create(GamePlayerActor.class, this), "player");

        activePlayersList.setItems(activePlayers);
        customizeActivePlayersDisplayedName();
    }

    private void customizeActivePlayersDisplayedName() {
        activePlayersList.setCellFactory(param -> new ListCell<ActorRef>() {
            @Override
            protected void updateItem(ActorRef item, boolean empty) {
                super.updateItem(item, empty);

                String text = item != null ? "Player #" + Math.abs(item.path().uid()) : null;
                setText(text);
            }
        });
    }

    @Override
    public void onGameServerDown() {
        Platform.runLater(() -> setLoadingState(
                "Game server is down! Please try again later (restart required)..."));
    }

    private void setLoadingState(String message) {
        Arrays.asList(newGamePanel, playGamePanel)
                .forEach(it -> it.setVisible(false));

        loadingLabel.setText(message);
        loadingLabel.setVisible(true);
    }

    @Override
    public void onPlayerConnected() {
        Platform.runLater(() -> {
            loadingLabel.setVisible(false);
            newGamePanel.setVisible(true);
        });
    }

    @Override
    public void onGameReady(Maze maze, List<ActorRef> players) {
        Platform.runLater(() -> {
            activePlayers.clear();
            activePlayers.addAll(players);

            drawMaze(maze);

            loadingLabel.setVisible(false);
            playGamePanel.setVisible(true);
        });
    }

    private void drawMaze(Maze maze) {
        for (int i = 0; i < maze.getRowCnt(); i++) {
            for (int j = 0; j < maze.getColCnt(); j++) {
                Field field = maze.getField(i, j);
                String color = field.getColor();

                mazePanel.getChildren().add(FieldView.of(i, j, color));
            }
        }
    }

    @Override
    public void onPlayerLeftGame(ActorRef player) {
        Platform.runLater(() -> activePlayers.remove(player));
    }

    @FXML
    private void newGameSelected(ActionEvent event) {
        String buttonText = ((Button) event.getSource()).getText();

        Matcher matcher = Pattern.compile("([1-4]) players?").matcher(buttonText);

        if (!matcher.matches()) return;

        int nPlayers = Integer.parseInt(matcher.group(1));

        player.tell(new AwaitNewGameEvent(nPlayers), null);

        setLoadingState("Waiting players...");
    }

    @FXML
    private void endGame(ActionEvent event) {
        playGamePanel.setVisible(false);
        newGamePanel.setVisible(true);

        activePlayers.forEach(it -> it.tell(new LeaveActiveGameEvent(), player));
    }
}
