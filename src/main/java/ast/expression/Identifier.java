package ast.expression;


import app.SimpleASTNode;
import ast.State;
import ast.expression.interfaces.Expression;
import ast.expression.interfaces.Value;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Element;
import utils.Tree;
import utils.Visitor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class Identifier implements Expression, Element<Tree.Node<SimpleASTNode>> {

    @Getter
    private final String identifier;

    @Override
    public Expression step(State state) {
        Value<?> value = state.get(this);
        return value == null ? new BadIdentifier(identifier) : value;
    }

    @Override
    public Tree.Node<SimpleASTNode> accept(Visitor<Tree.Node<SimpleASTNode>> visitor) {
        return visitor.visit(this);
    }

}
