package ast;

import ast.expression.Identifier;
import ast.expression.interfaces.Value;

import java.util.HashMap;
import java.util.Map;

public class State {

    private final Map<Identifier, Value<?>> state = new HashMap<>();

    public Value<?> get(Identifier identifier) {
        return state.get(identifier);
    }

    public void set(Identifier identifier, Value<?> var) {
        state.put(identifier, var);
    }

}
