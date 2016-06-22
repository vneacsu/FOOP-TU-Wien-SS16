package at.foop16.model.fields;

import javafx.scene.paint.Color;

public class CheeseField extends Field {

    @Override
    public String getColor() {
        return "YELLOW";
    }

    @Override
    public boolean isTarget() {
        return true;
    }

}
