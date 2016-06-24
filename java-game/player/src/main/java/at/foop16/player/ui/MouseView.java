package at.foop16.player.ui;

import at.foop16.model.Mouse;
import at.foop16.model.Position;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class MouseView extends StackPane {

    private static final int FIELD_SIZE = 16;

    private static final Random random = new Random();

    private Mouse mouse;

    public MouseView(Mouse mouse) {
        this.mouse = mouse;

        Rectangle rectangle = new Rectangle(FIELD_SIZE, FIELD_SIZE);
        Color color = Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble());
        rectangle.setFill(color);

        translate();
        getChildren().add(rectangle);
    }

    public void repaint(Mouse mouse) {
        this.mouse = mouse;
        translate();
    }

    private void translate() {
        Position position = mouse.getPosition();
        setTranslateX(position.getCol() * FIELD_SIZE);
        setTranslateY(position.getRow() * FIELD_SIZE);
    }

    public boolean isForMouse(Mouse mouse) {
        return this.mouse.getId() == mouse.getId();
    }
}
