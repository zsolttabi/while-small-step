package utils;

import app.SimpleASTNode;
import ast.AST;
import ast.expression.Identifier;
import ast.expression.abstract_operations.BinOp;
import ast.expression.abstract_operations.UnOp;
import ast.expression.interfaces.Value;
import ast.statement.*;
import lombok.val;
import utils.Tree.Node;

public class SimpleTreeBuilder implements Visitor<Node<SimpleASTNode>> {

    private static Node<SimpleASTNode> makeSimple(Class<?> clazz) {
        return new Node<>(new SimpleASTNode(clazz, null), null);
    }

    @Override
    public Node<SimpleASTNode> visit(AST element) {
        return element.getStm() == null ?
                new Node<>(new SimpleASTNode(element.getClass(), "EMPTY"), null) :
                element.getStm().accept(this);
    }

    @Override
    public Node<SimpleASTNode> visit(Skip element) {
        return makeSimple(element.getClass());
    }

    @Override
    public Node<SimpleASTNode> visit(Sequence element) {
        val node = makeSimple(element.getClass());
        node.addChild(element.getS1().accept(this));
        node.addChild(element.getS2().accept(this));
        return node;
    }

    @Override
    public Node<SimpleASTNode> visit(Assignment element) {
        val node = makeSimple(element.getClass());
        node.addChild(element.getIdentifier().accept(this));
        node.addChild(element.getValue().accept(this));
        return node;
    }


    @Override
    public Node<SimpleASTNode> visit(If element) {
        val node = makeSimple(element.getClass());
        node.addChild(element.getCondition().accept(this));
        node.addChild(element.getS1().accept(this));
        node.addChild(element.getS2().accept(this));
        return node;
    }

    @Override
    public Node<SimpleASTNode> visit(While element) {
        val node = makeSimple(element.getClass());
        node.addChild(element.getCondition().accept(this));
        node.addChild(element.getS().accept(this));
        return node;
    }

    @Override
    public Node<SimpleASTNode> visit(BinOp element) {
        val node = makeSimple(element.getClass());
        node.addChild(element.getLhs().accept(this));
        node.addChild(element.getRhs().accept(this));
        return node;
    }

    @Override
    public Node<SimpleASTNode> visit(UnOp element) {
        val node = makeSimple(element.getClass());
        node.addChild(element.getOperand().accept(this));
        return node;
    }

    @Override
    public Node<SimpleASTNode> visit(Value<?> element) {
        return new Node<>(new SimpleASTNode(element.getClass(), element.getValue().toString()), null);
    }

    @Override
    public Node<SimpleASTNode> visit(Identifier element) {
        return new Node<>(new SimpleASTNode(element.getClass(), element.getIdentifier()), null);
    }

}
