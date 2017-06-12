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
    public Configuration step(State state) {

        Configuration s1Conf = s1.step(state);
        if (s1Conf.getElement() instanceof Exception) {
            return s1Conf;
        }

        return s1Conf.getConfigType() == ConfigType.TERMINATED ?
                new Configuration(s2, s1Conf.getState(), ConfigType.INTERMEDIATE) :
                new Configuration(new Sequence((IStatement) s1Conf.getElement(), s2), s1Conf.getState(), s1Conf.getConfigType());
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
