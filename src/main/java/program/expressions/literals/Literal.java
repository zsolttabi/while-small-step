package program.expressions.literals;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.IProgramElement;
import program.State;
import program.expressions.Value;
import viewmodel.interfaces.INodeVisitor;

import java.util.Collections;
import java.util.Set;

import static program.Configuration.ConfigType.INTERMEDIATE;
import static program.Configuration.ConfigType.TERMINATED;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class Literal implements IProgramElement {

    @Getter
    private final String value;

    protected abstract Value<?> convertToValue();

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Configuration step(State state) {
        return new Configuration(convertToValue(), state, TERMINATED);
    }

    @Override
    public Set<Configuration> peek(State state) {
        return Collections.singleton(new Configuration(copy(), state, INTERMEDIATE));
    }

}
