package program;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import viewmodel.interfaces.INodeVisitor;

import java.util.Collections;
import java.util.Set;

import static program.Configuration.ConfigType.STUCK;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class SyntaxError implements IProgramElement {

    @Getter
    private final String text;

    @Override
    public Configuration step(State state) {
        return new Configuration(copy(), state, STUCK);
    }

    @Override
    public Set<Configuration> peek(State state) {
        return Collections.singleton(step(state));
    }

    @Override
    public IProgramElement copy() {
        return new SyntaxError(getText());
    }

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }

}
