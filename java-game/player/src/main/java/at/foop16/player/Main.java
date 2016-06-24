package at.foop16.player;

import akka.actor.ActorSystem;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.PrivateModule;
import com.typesafe.config.ConfigFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Main extends Application {

    private static final Injector injector = Guice.createInjector(new PlayerModule());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
        fxmlLoader.setControllerFactory(injector::getInstance);
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.setTitle("Mice race - Premium Edition");
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        injector.getInstance(ActorSystem.class).terminate();
        injector.getInstance(ScheduledExecutorService.class).shutdownNow();
    }

    private static final class PlayerModule extends PrivateModule {

        @Override
        protected void configure() {
            ActorSystem actorSystem = ActorSystem.create("game", ConfigFactory.load("akka.conf"));

            expose(ActorSystem.class);
            expose(ScheduledExecutorService.class);

            bind(ActorSystem.class).toInstance(actorSystem);
            bind(ScheduledExecutorService.class).toInstance(Executors.newSingleThreadScheduledExecutor());
        }
    }
}
