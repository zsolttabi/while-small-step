package ast.statement;

import app.SimpleASTNode;
import ast.State;
import ast.expression.interfaces.Expression;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Element;
import utils.Pair;
import utils.Tree;
import utils.Visitor;

@RequiredArgsConstructor
public class While implements Statement, Element<Tree.Node<SimpleASTNode>> {

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
