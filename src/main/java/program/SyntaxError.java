package program;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import viewmodel.interfaces.INodeVisitor;

import java.util.Collections;
import java.util.Set;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class SyntaxError implements IProgramElement {

    @Getter
    private final String text;

    @Override
    public Set<Configuration> peek(State state) {
        return Collections.singleton(step(state));
    }

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }
}
