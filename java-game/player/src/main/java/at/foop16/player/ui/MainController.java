package at.foop16.player.ui;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import at.foop16.events.AwaitNewGameEvent;
import at.foop16.player.service.GamePlayerActor;
import at.foop16.player.service.GameStateListener;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController implements Initializable, GameStateListener {

    @Inject
    private ActorSystem actorSystem;

    private volatile ActorRef player;

    private volatile List<ActorRef> players;

    @FXML
    private Label loadingLabel;
    @FXML
    private GridPane newGamePanel;
    @FXML
    private Label waitingPlayersLabel;
    @FXML
    private GridPane playGamePanel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        player = actorSystem.actorOf(Props.create(GamePlayerActor.class, this), "player");
    }

    @Override
    public void onPlayerConnected() {
        Platform.runLater(() -> {
            loadingLabel.setVisible(false);
            newGamePanel.setVisible(true);
        });
    }

    @Override
    public void onGameReady(List<ActorRef> players) {
        this.players = players;

        Platform.runLater(() -> {
            waitingPlayersLabel.setVisible(false);
            playGamePanel.setVisible(true);
        });
    }

    @FXML
    private void newGameSelected(ActionEvent event) {
        String buttonText = ((Button) event.getSource()).getText();

        Matcher matcher = Pattern.compile("([1-4]) players?").matcher(buttonText);

        if (!matcher.matches()) return;

        int nPlayers = Integer.parseInt(matcher.group(1));

        player.tell(new AwaitNewGameEvent(nPlayers), null);

        newGamePanel.setVisible(false);
        waitingPlayersLabel.setVisible(true);
    }

    @FXML
    private void endGame(ActionEvent event) {
        playGamePanel.setVisible(false);
        newGamePanel.setVisible(true);
    }
}
