package program.statements;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.IProgramElement;
import program.State;
import program.expressions.Identifier;
import program.expressions.Value;
import viewmodel.interfaces.INodeVisitor;

import java.util.Collections;
import java.util.Set;

import static program.Configuration.ConfigType.*;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Assignment implements IProgramElement {

    @Getter
    private final IProgramElement identifier;
    @Getter
    private final IProgramElement value;

    @Override
    public Configuration step(State state) {

        if (!(identifier instanceof Identifier)) {
            return new Configuration(this, state, STUCK);
        }

        if (!(value instanceof Value)) {
            Configuration valueConf = value.step(state);
            return new Configuration(new Assignment(identifier, valueConf.getElement()),
                    valueConf.getState(),
                    valueConf.getConfigType() == STUCK ? STUCK : INTERMEDIATE);
        }

        Identifier id = (Identifier) identifier;
        Value val = (Value) this.value;

        Value currentValue = state.get(id);
        if (currentValue != null && currentValue.getValue().getClass() != val.getValue().getClass()) {
            return new Configuration(this, state, STUCK);
        }

        State newState = state.copy();
        newState.set(id, val);
        return new Configuration(this, newState, TERMINATED);
    }

    @Override
    public Set<Configuration> peek(State state) {
        if (!(value instanceof Value)) {
            return value.peek(state);
        }
        Configuration stepConfiguration = step(state);
        return Collections.singleton(new Configuration(this,
                stepConfiguration.getState(),
                stepConfiguration.getConfigType()));
    }

    @Override
    public IProgramElement copy() {
        return new Assignment(identifier.copy(), value.copy());
    }

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }

}
