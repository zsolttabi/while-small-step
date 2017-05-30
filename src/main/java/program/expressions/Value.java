package program.expressions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.Configuration.ConfigType;
import program.State;
import viewmodel.interfaces.INodeVisitor;

import java.util.Collections;
import java.util.Set;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Value<T> implements IExpression {

    @Getter
    private final T value;

    @Override
    public ExpressionConfiguration step(State state) {
        return new ExpressionConfiguration(this, state, ConfigType.TERMINATED);
    }

    @Override
    public Set<Configuration> peek(State state) {
        return Collections.singleton(step(state));
    }

    @Override
    public IExpression copy() {
        return new Value<>(value);
    }

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }

}
