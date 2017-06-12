package program;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import viewmodel.interfaces.INodeVisitor;

import static program.Configuration.ConfigType.STUCK;


@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Exception implements IProgramElement {

    @Getter
    private final String name;

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }

    @Override
    public Configuration step(State state) {
        return new Configuration(this, state, STUCK);
    }

    @Override
    public Exception copy() {
        return new Exception(getName());
    }

}
