package ast.statement;

import app.SimpleASTNode;
import ast.State;
import ast.expression.interfaces.Expression;
import ast.statement.bad_statements.BadWhile;
import ast.statement.interfaces.Statement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Element;
import utils.Pair;
import utils.Tree;
import utils.Visitor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class While implements Statement, Element<Tree.Node<SimpleASTNode>> {

    public static While of(Expression condition, Statement s) {
        return condition == null || s == null ? new BadWhile(condition, s) : new While(condition, s);
    }

    @Getter
    private final Expression condition;
    @Getter
    private final Statement s;

    @Override
    public Pair<Statement, State> step(State state) {
        return Pair.of(new If(condition, new Sequence(s, this), new Skip()), state);
    }

    @Override
    public Tree.Node<SimpleASTNode> accept(Visitor<Tree.Node<SimpleASTNode>> visitor) {
        return visitor.visit(this);
    }

}
