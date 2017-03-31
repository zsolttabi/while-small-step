package ast;

import ast.expression.interfaces.Value;

import java.util.HashMap;
import java.util.Map;

public class State {

    private final Map<String, Value<?>> state = new HashMap<>();

    public Value<?> get(String identifier) {
        return state.get(identifier);
    }

    public void set(String identifier, Value<?> var) {
        state.put(identifier, var);
    }

}
