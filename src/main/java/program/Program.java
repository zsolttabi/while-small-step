package program;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import program.statements.IStatement;
import program.statements.StatementConfiguration;
import viewmodel.interfaces.INodeVisitor;
import viewmodel.interfaces.IVisitableNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static program.Configuration.ConfigType.INTERMEDIATE;

@EqualsAndHashCode
public class Program implements IVisitableNode {

    @Getter
    private final List<Configuration> reductionChain;
    private int index;
    private final int maxPrefix;

    public Program(Configuration startConfiguration, int maxPrefix) {
        if (maxPrefix < 1) {
            throw new RuntimeException("Maximum program evaluation prefix cannot be less then 1.");
        }
        this.maxPrefix = maxPrefix;
        this.reductionChain = new ArrayList<>(maxPrefix);
        this.reductionChain.add(startConfiguration);
        this.index = 0;
    }

    public Program(IStatement statement, int maxPrefix) {
        this(new StatementConfiguration(statement, new State(), INTERMEDIATE), maxPrefix);
    }

    public Configuration current() {
        return getReductionChain().get(index);
    }

    public Set<Configuration> peek() {
        return current().peek();
    }

    public boolean hasNext() {
        return reductionChain.size() < maxPrefix && current().getConfigType() == INTERMEDIATE;
    }

    public void next() {

        if (reductionChain.size() == maxPrefix) {
            throw new RuntimeException("Max prefix reached. Cannot step further.");
        }

        if (reductionChain.size() <= index + 1) {
            reductionChain.add(current().step());
        }
        ++index;
    }

    public boolean hasPrev() {
        return index > 0;
    }

    public void prev() {
        if (index == 0) {
            throw new RuntimeException("No previous configuration exists.");
        }
        --index;
    }

    public void first() {
        while (hasPrev()) {
            prev();
        }
    }

    public void last() {
        while (hasNext() && current().getConfigType() == INTERMEDIATE) {
            next();
        }
    }

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }

}

