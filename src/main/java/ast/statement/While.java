package ast.statement;

import ast.State;
import ast.StmConfig;
import ast.expression.interfaces.Expression;
import ast.statement.bad_statements.StuckWhile;
import ast.statement.interfaces.Statement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.IASTElement;
import viewmodel.interfaces.IASTVisitor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class While implements Statement, IASTElement<Tree.Node<ASTNode>> {

    public static While of(Expression condition, Statement s) {
        return condition == null || s == null ? new StuckWhile(condition, s) : new While(condition, s);
    }

    @Getter
    private final Expression condition;
    @Getter
    private final Statement s;

    @Override
    public StmConfig step(State state) {
        return StmConfig.of(new If(condition, new Sequence(s, this), new Skip()), state);
    }

    @Override
    public Tree.Node<ASTNode> accept(IASTVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
