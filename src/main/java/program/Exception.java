package program;

import viewmodel.interfaces.INodeVisitor;

import java.util.Set;


public class Exception implements IProgramElement {

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return null;
    }

    @Override
    public Configuration step(State state) {
        return null;
    }

    @Override
    public Set<Configuration> peek(State state) {
        return null;
    }

    @Override
    public Exception copy() {
        return null;
    }

}
