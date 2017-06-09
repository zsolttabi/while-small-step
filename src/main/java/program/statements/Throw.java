package program.statements;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.State;
import viewmodel.interfaces.INodeVisitor;

import java.util.Set;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Throw implements IStatement {

    private final program.Exception e;

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return null;
    }

    @Override
    public Configuration step(State state) {
        return new Configuration(e, state, Configuration.ConfigType.INTERMEDIATE);
    }

    @Override
    public Set<Configuration> peek(State state) {
        return null;
    }

    @Override
    public IStatement copy() {
        return null;
    }

}
