package ast.statement;

import ast.State;
import ast.StmConfig;
import ast.statement.bad_statements.StuckSequence;
import ast.statement.interfaces.Statement;
import ast.statement.interfaces.StuckStatement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.IASTElement;
import viewmodel.interfaces.IASTVisitor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class Sequence implements Statement, IASTElement<Tree.Node<ASTNode>> {

    @Getter
    private final Statement s1;
    @Getter
    private final Statement s2;

    public static Sequence of(Statement s1, Statement s2) {
        return s1 == null || s2 == null ? new StuckSequence(s1, s2) : new Sequence(s1, s2);
    }

    @Override
    public StmConfig step(State state) {

        StmConfig s1NewConfig = s1.step(state);

        if (s1NewConfig.getStatement() instanceof StuckStatement) {
            return StmConfig.of(new StuckSequence(s1NewConfig.getStatement(), s2), s1NewConfig.getState());
        }

        return s1NewConfig.isEndConfiguration() ?
                StmConfig.of(s2, s1NewConfig.getState()) :
                StmConfig.of(new Sequence(s1NewConfig.getStatement(), s2), s1NewConfig.getState());
    }

    @Override
    public Tree.Node<ASTNode> accept(IASTVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
