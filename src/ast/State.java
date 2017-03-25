package ast;

import ast.expression.Var;

import java.util.HashMap;
import java.util.Map;

public class State {

    private final Map<String, Var<?>> state = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> Var<T> getVar(String identifier) {
        return (Var<T>)state.get(identifier);
    }

    public <T> void setVar(String identifier, Var<T> var) {
        state.put(identifier, var);
    }

}
