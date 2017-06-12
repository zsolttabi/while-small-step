package program.statements;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.IProgramElement;
import program.State;
import viewmodel.interfaces.INodeVisitor;

import java.util.HashSet;
import java.util.Set;

import static program.IProgramElement.choose;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Or implements IProgramElement {

    @Getter
    private final IProgramElement s1;
    @Getter
    private final IProgramElement s2;

    @Override
    public Configuration step(State state) {
        return choose(s1, s2).step(state);
    }

    @Override
    public Set<Configuration> peek(State state) {
        Set<Configuration> next = new HashSet<>();
        next.addAll(s1.peek(state));
        next.addAll(s2.peek(state));
        return next;
    }

    @Override
    public IProgramElement copy() {
        return new Or(s1.copy(), s2.copy());
    }

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }

}
