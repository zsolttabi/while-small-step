package ast.statement;

import app.SimpleASTNode;
import ast.State;
import ast.expression.interfaces.BadExpression;
import ast.expression.interfaces.Expression;
import ast.expression.interfaces.Value;
import ast.expression.values.BoolValue;
import ast.statement.bad_statements.BadIf;
import ast.statement.bad_statements.BadSequence;
import ast.statement.interfaces.BadStatement;
import ast.statement.interfaces.Statement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Element;
import utils.Pair;
import utils.Tree;
import utils.Visitor;

@RequiredArgsConstructor
public class If implements Statement, Element<Tree.Node<SimpleASTNode>> {

    public static If of(Expression condition, Statement s1, Statement s2) {
        return condition == null || s1 == null || s2 == null ? new BadIf(condition, s1, s2) : new If(condition, s1, s2);
    }

    @Getter
    private final Expression condition;
    @Getter
    private final Statement s1;
    @Getter
    private final Statement s2;

    @Override
    public Pair<Statement, State> step(State state) {

        if (condition instanceof BadExpression || s1 instanceof BadStatement || s2 instanceof BadStatement) {
            return Pair.of(new BadIf(condition, s1, s2), state);
        }

        if (!(condition instanceof Value)) {
            return Pair.of(new If(condition.step(state), s1, s2), state);
        }

        if (condition instanceof BoolValue) {
            return Pair.of(((BoolValue) condition).getValue() ? s1 : s2, state);
        }

        return Pair.of(new BadIf(condition, s1, s1), state);
    }

    @Override
    public Tree.Node<SimpleASTNode> accept(Visitor<Tree.Node<SimpleASTNode>> visitor) {
        return visitor.visit(this);
    }
}
