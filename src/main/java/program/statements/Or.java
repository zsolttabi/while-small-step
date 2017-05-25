package program.statements;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.IProgramElement;
import program.State;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;

import java.util.HashSet;
import java.util.Set;

import static program.Configuration.ConfigType.INTERMEDIATE;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Or implements IStatement {

    @Getter
    private final IStatement s1;
    @Getter
    private final IStatement s2;

    @Override
    public StatementConfiguration step(State state) {
        return new StatementConfiguration(IProgramElement.choose(s1, s2), state, INTERMEDIATE);
    }

    @Override
    public Set<Configuration> peek(State state) {
        Set<Configuration> next = new HashSet<>();
        next.addAll(s1.peek(state));
        next.addAll(s2.peek(state));
        return next;
    }

    @Override
    public IStatement copy() {
        return new Or(s1.copy(), s2.copy());
    }

    @Override
    public Tree.Node<ASTNode> accept(INodeVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }
}
