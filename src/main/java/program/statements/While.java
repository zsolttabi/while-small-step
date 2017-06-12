package program.statements;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.Configuration.ConfigType;
import program.IProgramElement;
import program.State;
import viewmodel.interfaces.INodeVisitor;

import java.util.Collections;
import java.util.Set;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class While implements IProgramElement {

    @Getter
    private final IProgramElement condition;
    @Getter
    private final IProgramElement stm;

    @Override
    public Configuration step(State state) {
        return new Configuration(new If(condition, new Sequence(stm, copy()), new Skip()), state, ConfigType.INTERMEDIATE);
    }

    @Override
    public Set<Configuration> peek(State state) {
        return Collections.singleton(new Configuration(this, state, ConfigType.INTERMEDIATE));
    }

    @Override
    public IProgramElement copy() {
        return new While(condition.copy(), stm.copy());
    }

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }
}
