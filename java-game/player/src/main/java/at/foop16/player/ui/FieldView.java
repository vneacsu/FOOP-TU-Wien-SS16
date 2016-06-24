package at.foop16.player.ui;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class FieldView extends StackPane {

    private static final int FIELD_SIZE = 16;

    private FieldView(int row, int col, Color color) {
        Rectangle rectangle = new Rectangle(FIELD_SIZE, FIELD_SIZE);
        rectangle.setFill(color);

        setTranslateX(col * FIELD_SIZE);
        setTranslateY(row * FIELD_SIZE);

        getChildren().add(rectangle);
    }

    public static FieldView of(int row, int col, String color) {
        Color fxColor = Color.valueOf(color);

        return new FieldView(row, col, fxColor);
    }

}
