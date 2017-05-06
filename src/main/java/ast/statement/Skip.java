package ast.statement;

import app.SimpleASTNode;
import ast.State;
import ast.statement.interfaces.Statement;
import lombok.EqualsAndHashCode;
import utils.Element;
import utils.Pair;
import utils.Tree;
import utils.Visitor;

@EqualsAndHashCode
public class Skip implements Statement, Element<Tree.Node<SimpleASTNode>> {

    @Override
    public Pair<Statement, State> step(State state) {
        return Pair.of(null, state);
    }

    @Override
    public Tree.Node<SimpleASTNode> accept(Visitor<Tree.Node<SimpleASTNode>> visitor) {
        return visitor.visit(this);
    }

}
