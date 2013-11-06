package se.flittermou.jsbp.db.datatypes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface Datatype<T> {
    public String toString();

    public T getValue();

    public void setValue(ResultSet rs, String name) throws SQLException;

    public String getStringRepresentation();

    public List<Class> getAllowedModifiers();

    public Datatype<T> copy();
}
