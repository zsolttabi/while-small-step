package program.statements;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.State;
import viewmodel.interfaces.INodeVisitor;

import java.util.Set;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class TryCatch implements IStatement {

    @Getter
    private final IStatement s;
    @Getter
    private final Exception e;

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return null;
    }

    @Override
    public StatementConfiguration step(State state) {
        return null;
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
