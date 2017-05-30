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
public class Identifier implements IExpression {

    @Getter
    private final String identifier;

    @Override
    public ExpressionConfiguration step(State state) {
        Value<?> value = state.get(this);
        if (value == null) {
            return new ExpressionConfiguration(this, state, ConfigType.STUCK);
        } else {
            return new ExpressionConfiguration(value, state, ConfigType.TERMINATED);
        }
    }

    @Override
    public Set<Configuration> peek(State state) {
        return Collections.singleton(new ExpressionConfiguration(this, state, ConfigType.INTERMEDIATE));
    }

    @Override
    public IExpression copy() {
        return new Identifier(identifier);
    }

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }

}
