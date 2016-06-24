package at.foop16.model.fields;

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
