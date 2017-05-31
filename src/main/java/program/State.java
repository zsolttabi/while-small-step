package program;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import program.expressions.Identifier;
import program.expressions.Value;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode
@ToString
public class State {

    private final Map<Identifier, Value<?>> state;

    public State() {
        this(new HashMap<>());
    }

    public Value<?> get(Identifier identifier) {
        return state.get(identifier);
    }

    public void set(Identifier identifier, Value<?> var) {
        state.put(identifier, var);
    }

    public Set<Map.Entry<Identifier, Value<?>>> entrySet() {
        return state.entrySet();
    }

    public State copy() {
        return new State(new LinkedHashMap<>(state));
    }

    private State(Map<Identifier, Value<?>> state) {
        this.state = state;
    }

}
