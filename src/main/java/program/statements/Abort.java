package program.statements;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import program.Configuration;
import program.Configuration.ConfigType;
import program.IProgramElement;
import program.State;
import viewmodel.interfaces.INodeVisitor;

@EqualsAndHashCode
@ToString
public class Abort implements IProgramElement {

    @Override
    public Configuration step(State state) {
        return new Configuration(this, state, ConfigType.STUCK);
    }

    @Override
    public IProgramElement copy() {
        return new Abort();
    }

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }

}
