package program.statements;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.State;
import viewmodel.interfaces.INodeVisitor;

import java.util.Collections;
import java.util.Set;

import static program.Configuration.ConfigType.INTERMEDIATE;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Throw implements IStatement {

    @Getter
    private final program.Exception e;

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Configuration step(State state) {
        return new Configuration(e, state, INTERMEDIATE);
    }

    @Override
    public Set<Configuration> peek(State state) {
        return Collections.singleton(new Configuration(this, state, INTERMEDIATE));
    }

    @Override
    public IStatement copy() {
        return new Throw(e);
    }

}
