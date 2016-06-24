package at.foop16.model.fields;

import java.io.Serializable;

public abstract class Field implements Serializable {

    public boolean isTarget() {
        return false;
    }

    public boolean canWalkIn() {
        return true;
    }

    public abstract String getColor();

}
