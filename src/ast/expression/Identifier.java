package ast.expression;


import app.SimpleASTNode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Element;
import utils.Tree;
import utils.Visitor;

@RequiredArgsConstructor
public class Identifier implements Expression, Element<Tree.Node<SimpleASTNode>> {

    @Getter
    private final String identifier;

    @Override
    public Expression step() {
        return this;
    }

    @Override
    public Tree.Node<SimpleASTNode> accept(Visitor<Tree.Node<SimpleASTNode>> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identifier that = (Identifier) o;

        return identifier != null ? identifier.equals(that.identifier) : that.identifier == null;
    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }

}
