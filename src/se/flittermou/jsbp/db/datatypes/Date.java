package se.flittermou.jsbp.db.datatypes;

import se.flittermou.jsbp.db.datatypes.modifiers.NOTNULL;
import se.flittermou.jsbp.db.datatypes.modifiers.PRIMARY;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Date extends AbstractDatatype<java.util.Date> {
    private static final List<Class> allowedModifiers = new ArrayList<Class>() {
        {
            add(NOTNULL.class);
            add(PRIMARY.class);
        }
    };
    private java.util.Date value;

    public Date() {
        super(allowedModifiers);
    }

    public Date(java.util.Date date) {
        this();
        this.value = date;
    }

    @Override
    public java.util.Date getValue() {
        return value;
    }

    @Override
    public void setValue(ResultSet rs, String name) throws SQLException {
        value = rs.getDate(name);
    }

    @Override
    public String getStringRepresentation() {
        return "DATETIME";
    }

    @Override
    public Datatype<java.util.Date> copy() {
        if (this.value != null) {
            return new Date(this.value);
        } else {
            return new Date();
        }
    }
}
