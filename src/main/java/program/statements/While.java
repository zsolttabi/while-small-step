package program.statements;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.Configuration.ConfigType;
import program.State;
import program.expressions.IExpression;
import viewmodel.interfaces.INodeVisitor;

import java.util.Collections;
import java.util.Set;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class While implements IStatement {

    @Getter
    private final IExpression condition;
    @Getter
    private final IStatement stm;

    @Override
    public StatementConfiguration step(State state) {
        return new StatementConfiguration(new If(condition, new Sequence(stm, copy()), new Skip()), state, ConfigType.INTERMEDIATE);
    }

    @Override
    public Set<Configuration> peek(State state) {
        return Collections.singleton(new StatementConfiguration(this, state, ConfigType.INTERMEDIATE));
    }

    @Override
    public IStatement copy() {
        return new While(condition.copy(), stm.copy());
    }

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }
}
