package program.statements;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import program.Configuration;
import program.Configuration.ConfigType;
import program.State;
import viewmodel.interfaces.INodeVisitor;

import java.util.Collections;
import java.util.Set;

@EqualsAndHashCode
@ToString
public class Skip implements IStatement {

    @Override
    public StatementConfiguration step(State state) {
        return new StatementConfiguration(this, state, ConfigType.TERMINATED);
    }

    @Override
    public Set<Configuration> peek(State state) {
        return Collections.singleton(step(state));
    }

    @Override
    public IStatement copy() {
        return new Skip();
    }

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }
}
