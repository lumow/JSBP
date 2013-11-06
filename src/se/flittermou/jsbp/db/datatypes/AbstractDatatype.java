package se.flittermou.jsbp.db.datatypes;

import java.util.List;

public abstract class AbstractDatatype<T> implements Datatype {
    private List<Class> allowedModifiers;

    public AbstractDatatype(List<Class> allowedModifiers) {
        this.allowedModifiers = allowedModifiers;
    }

    public List<Class> getAllowedModifiers() {
        return allowedModifiers;
    }
}
