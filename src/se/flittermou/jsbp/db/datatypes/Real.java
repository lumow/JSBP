package se.flittermou.jsbp.db.datatypes;

import se.flittermou.jsbp.db.datatypes.modifiers.NOTNULL;
import se.flittermou.jsbp.db.datatypes.modifiers.PRIMARY;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Real extends AbstractDatatype<Double> {
    private static final List<Class> allowedModifiers = new ArrayList<Class>() {
        {
            add(NOTNULL.class);
            add(PRIMARY.class);
        }
    };
    private Double value;

    public Real() {
        super(allowedModifiers);
    }

    public Real(double value) {
        this();
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public void setValue(ResultSet rs, String name) throws SQLException {
        value = rs.getDouble(name);
    }

    @Override
    public String getStringRepresentation() {
        return "REAL";
    }

    @Override
    public Datatype<Real> copy() {
        if (this.value != null) {
            return new Real(this.value);
        } else {
            return new Real();
        }
    }
}
