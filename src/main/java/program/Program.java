package program;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import program.Configuration.ConfigType;
import program.statements.Statement;
import program.statements.StatementConfiguration;
import utils.Tree.Node;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;
import viewmodel.interfaces.IVisitableNode;

import java.util.*;

@EqualsAndHashCode
public class Program implements IVisitableNode<Node<ASTNode>> {

    @Getter
    private final List<Configuration> reductionChain;
    private int currentConfigIndex;
    private final int maxPrefix;

    public Program(Configuration startConfiguration, int maxPrefix) {
        this.maxPrefix = maxPrefix;
        this.reductionChain = new ArrayList<>(maxPrefix);
        this.reductionChain.add(startConfiguration);
        this.currentConfigIndex = 0;
    }

    public Program(Statement statement, int maxPrefix) {
        this(new StatementConfiguration(statement, new State(), ConfigType.INTERMEDIATE), maxPrefix);
    }

    public Configuration getCurrentConfiguration() {
        return getReductionChain().get(currentConfigIndex);
    }

    public Configuration step() {
        reductionChain.add(getCurrentConfiguration().step());
        currentConfigIndex++;
        return getCurrentConfiguration();
    }

    public Configuration next() {
        return getCurrentConfiguration().next();
    }

    public List<Configuration> reduce() {
        for (int i = currentConfigIndex; i < maxPrefix - 1; i++) {
            step();
        }
        return new ArrayList<>(reductionChain);
    }

    @Override
    public Node<ASTNode> accept(INodeVisitor<Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}

