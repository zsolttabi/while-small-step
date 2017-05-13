package program.statements;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.State;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;

import java.util.Random;

import static program.Configuration.ConfigType.INTERMEDIATE;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Or implements IStatement {

    private static final Random RANDOM = new Random();

    @Getter
    private final IStatement s1;
    @Getter
    private final IStatement s2;

    @Override
    public StatementConfiguration step(State state) {
        boolean nextS1 = RANDOM.nextBoolean();
        return new StatementConfiguration(nextS1 ? s1 : s2, state, INTERMEDIATE);
    }

    @Override
    public Configuration next(State state) {
        return new StatementConfiguration(this, state, INTERMEDIATE);
    }

    @Override
    public Tree.Node<ASTNode> accept(INodeVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }
}
