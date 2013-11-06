package se.flittermou.jsbp.db.datatypes;

import se.flittermou.jsbp.db.datatypes.modifiers.NOTNULL;
import se.flittermou.jsbp.db.datatypes.modifiers.PRIMARY;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Int extends AbstractDatatype<Integer> {
    private static final List<Class> allowedModifiers = new ArrayList<Class>() {
        {
            add(NOTNULL.class);
            add(PRIMARY.class);
        }
    };
    private Integer value;

    public Int() {
        super(allowedModifiers);
    }

    public Int(int value) {
        this();
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void setValue(ResultSet rs, String name) throws SQLException {
        value = rs.getInt(name);
    }

    @Override
    public String getStringRepresentation() {
        return "INT";
    }

    @Override
    public Datatype<Int> copy() {
        if (this.value != null) {
            return new Int(this.value);
        } else {
            return new Int();
        }
    }
}
