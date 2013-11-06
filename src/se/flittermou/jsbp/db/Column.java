package se.flittermou.jsbp.db;

import se.flittermou.jsbp.db.datatypes.Datatype;
import se.flittermou.jsbp.db.datatypes.modifiers.Modifier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Column {
    private String name;
    private Datatype type;
    private List<Modifier> modifiers = new ArrayList<>();

    private Column(String name, Datatype type) {
        this.name = name;
        this.type = type;
    }

    public static Column create(String name, Datatype type) {
        return new Column(name, type.copy());
    }

    public Column addModifier(Modifier name) {
        modifiers.add(name);
        return this;
    }

    public String getName() {
        return name;
    }

    public Datatype getType() {
        return type;
    }

    public void setValue(ResultSet rs) throws SQLException {
        type.setValue(rs, name);
    }

    public Object getValue() {
        return type.getValue();
    }

    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder();
        returnString.append(name).append(" ").append(type.getStringRepresentation()).append(" ");
        for (Modifier modifier : modifiers) {
            returnString.append(modifier.toString()).append(" ");
        }
        returnString.deleteCharAt(returnString.lastIndexOf(" "));
        return returnString.toString();
    }
}
