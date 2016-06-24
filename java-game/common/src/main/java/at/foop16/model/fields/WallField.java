package at.foop16.model.fields;

public class WallField extends Field {

    public boolean canWalkIn() {
        return false;
    }

    @Override
    public String getColor() {
        return "BLACK";
    }
}
