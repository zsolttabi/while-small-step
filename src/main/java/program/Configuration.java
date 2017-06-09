package program;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import viewmodel.interfaces.INodeVisitor;
import viewmodel.interfaces.IVisitableNode;

import java.util.Set;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class Configuration implements IVisitableNode {

    public enum ConfigType {
        INTERMEDIATE, STUCK, TERMINATED
    }

    @Getter
    private final IProgramElement element;
    @Getter
    private final State state;
    @Getter
    private final ConfigType configType;

    public Configuration step() {
        return element.step(state);
    }

    public Set<Configuration> peek() {
        return element.peek(state);
    }

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }

}
