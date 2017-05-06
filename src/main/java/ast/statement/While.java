package ast.statement;

import app.SimpleASTNode;
import ast.State;
import ast.expression.interfaces.Expression;
import ast.statement.bad_statements.BadWhile;
import ast.statement.interfaces.Statement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Element;
import utils.Pair;
import utils.Tree;
import utils.Visitor;

@RequiredArgsConstructor
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        While aWhile = (While) o;

        if (condition != null ? !condition.equals(aWhile.condition) : aWhile.condition != null) return false;
        return s != null ? s.equals(aWhile.s) : aWhile.s == null;
    }

    @Override
    public int hashCode() {
        int result = condition != null ? condition.hashCode() : 0;
        result = 31 * result + (s != null ? s.hashCode() : 0);
        return result;
    }
}
