package program.statements;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.Configuration.ConfigType;
import program.State;
import viewmodel.interfaces.INodeVisitor;

import java.util.Set;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Sequence implements IStatement {

    @Getter
    private final IStatement s1;
    @Getter
    private final IStatement s2;

    @Override
    public StatementConfiguration step(State state) {

        StatementConfiguration s1Conf = s1.step(state);

        return s1Conf.getConfigType() == ConfigType.TERMINATED ?
                new StatementConfiguration(s2, s1Conf.getState(), ConfigType.INTERMEDIATE) :
                new StatementConfiguration(new Sequence(s1Conf.getNode(), s2), s1Conf.getState(), s1Conf.getConfigType());
    }

    @Override
    public Set<Configuration> peek(State state) {
        return s1.peek(state);
    }

    @Override
    public IStatement copy() {
        return new Sequence(s1.copy(), s2.copy());
    }

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }
}
