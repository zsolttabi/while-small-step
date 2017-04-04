package ast;

import ast.expression.Identifier;
import ast.expression.interfaces.Value;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class State {

    private final Map<Identifier, Value<?>> state = new LinkedHashMap<>();

    public Value<?> get(Identifier identifier) {
        return state.get(identifier);
    }

    public void set(Identifier identifier, Value<?> var) {
        state.put(identifier, var);
    }

    public Set<Map.Entry<Identifier, Value<?>>> entrySet() {
        return state.entrySet();
    }

}
