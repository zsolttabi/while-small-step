package program.expressions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.State;
import viewmodel.interfaces.INodeVisitor;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

import static program.Configuration.ConfigType.INTERMEDIATE;
import static program.Configuration.ConfigType.TERMINATED;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class Literal implements IExpression {

    @Getter
    private final String value;

    protected abstract Value<?> convertToValue();

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }

    @Override
    public ExpressionConfiguration step(State state) {
        return new ExpressionConfiguration(convertToValue(), state, TERMINATED);
    }

    @Override
    public Set<Configuration> peek(State state) {
        return Collections.singleton(new ExpressionConfiguration(this, state, INTERMEDIATE));
    }

}
