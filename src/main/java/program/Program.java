package program;

import lombok.EqualsAndHashCode;
import lombok.Getter;
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
    private final int allowedPrefix;

    public Program(Configuration startConfiguration, int allowedPrefix) {
        if (allowedPrefix < 1) {
            throw new RuntimeException("Allowed prefix cannot be less then 1.");
        }
        this.allowedPrefix = allowedPrefix;
        this.reductionChain = new ArrayList<>(allowedPrefix);
        this.reductionChain.add(startConfiguration);
        this.index = 0;
    }

    public Program(IProgramElement statement, int maxPrefix) {
        this(new Configuration(statement, new State(), INTERMEDIATE), maxPrefix);
    }

    public Configuration current() {
        return getReductionChain().get(index);
    }

    public int currentStep() {
        return index;
    }

    public Set<Configuration> peek() {
        return current().peek();
    }

    public boolean hasNext() {
        return reductionChain.size() < allowedPrefix && current().getConfigType() == INTERMEDIATE;
    }

    public void next() {

        if (reductionChain.size() == allowedPrefix) {
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

