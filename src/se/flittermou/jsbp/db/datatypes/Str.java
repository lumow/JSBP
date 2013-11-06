package se.flittermou.jsbp.db.datatypes;

import se.flittermou.jsbp.db.datatypes.modifiers.NOTNULL;
import se.flittermou.jsbp.db.datatypes.modifiers.PRIMARY;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Str extends AbstractDatatype<String> {
    private static final List<Class> allowedModifiers = new ArrayList<Class>() {
        {
            add(NOTNULL.class);
            add(PRIMARY.class);
        }
    };
    private String value;

    public Str() {
        super(allowedModifiers);
    }

    public Str(String value) {
        this();
        this.value = value;
    }

    public String toString() {
        return value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(ResultSet rs, String name) throws SQLException {
        value = rs.getString(name);
    }

    @Override
    public String getStringRepresentation() {
        return "TEXT";
    }

    @Override
    public Datatype<Str> copy() {
        if (this.value != null) {
            return new Str(this.value);
        } else {
            return new Str();
        }
    }
}
