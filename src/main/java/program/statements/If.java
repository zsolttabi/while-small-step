package program.statements;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.IProgramElement;
import program.State;
import program.expressions.Value;
import viewmodel.interfaces.INodeVisitor;

import java.util.Set;

import static program.Configuration.ConfigType.INTERMEDIATE;
import static program.Configuration.ConfigType.STUCK;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class If implements IProgramElement {

    @Getter
    private final IProgramElement condition;
    @Getter
    private final IProgramElement s1;
    @Getter
    private final IProgramElement s2;

    @Override
    public Configuration step(State state) {

        if (!(condition instanceof Value)) {
            Configuration condConf = condition.step(state);
            return new Configuration(new If(condConf.getElement(), s1, s2),
                    condConf.getState(),
                    condConf.getConfigType() == STUCK ? STUCK : INTERMEDIATE);
        }

        Object condValue = ((Value) condition).getValue();

        if (!(condValue instanceof Boolean)) {
            return new Configuration(this, state, STUCK);
        }

        return new Configuration((Boolean) condValue ? s1 : s2, state, INTERMEDIATE);
    }

    @Override
    public Set<Configuration> peek(State state) {
        return condition.peek(state);
    }

    @Override
    public IProgramElement copy() {
        return new If(condition.copy(), s1.copy(), s2.copy());
    }

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }

}
