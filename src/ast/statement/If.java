package ast.statement;

import app.SimpleASTNode;
import ast.State;
import ast.expression.Expression;
import ast.expression.interfaces.Value;
import ast.expression.values.BoolValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Element;
import utils.Pair;
import utils.Tree;
import utils.Visitor;

@RequiredArgsConstructor
public class If implements Statement, Element<Tree.Node<SimpleASTNode>> {

    @Getter
    private final Expression condition;
    @Getter
    private final Statement s1;
    @Getter
    private final Statement s2;

    @Override
    public Pair<Statement, State> step(State state) {

        if (condition == null || s1 == null || s2 == null) {
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
