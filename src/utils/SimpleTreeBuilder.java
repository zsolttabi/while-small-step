package utils;

import app.SimpleASTNode;
import ast.AST;
import ast.expression.Expression;
import ast.expression.abstract_operations.BinOp;
import ast.expression.abstract_operations.UnOp;
import ast.expression.interfaces.Value;
import ast.statement.If;
import ast.statement.Sequence;
import ast.statement.While;
import lombok.val;
import utils.Tree.Node;

public class SimpleTreeBuilder implements Visitor<Node<SimpleASTNode>> {

    private static Node<SimpleASTNode> makeSimple(Class<?> clazz) {
        return new Node<>(new SimpleASTNode(clazz, null), null);
    }



    @Override
    public Node<SimpleASTNode> visit(Element<Node<SimpleASTNode>> element) {
        return null;
    }

    public Node<SimpleASTNode> visit(AST n) {
        val node = makeSimple(n.getClass());
        node.addChild(n.getStm().accept(this));
        return node;
    }

    public Node<SimpleASTNode> visit(Sequence n) {
        val node = makeSimple(n.getClass());
        node.addChild(n.getS1().accept(this));
        node.addChild(n.getS2().accept(this));
        return node;
    }

    public Node<SimpleASTNode> visit(If n) {
        val node = makeSimple(n.getClass());
        node.addChild(n.getCondition().accept(this));
        node.addChild(n.getS1().accept(this));
        node.addChild(n.getS2().accept(this));
        return node;
    }

    public Node<SimpleASTNode> visit(While n) {
        val node = makeSimple(n.getClass());
        node.addChild(n.getCondition().accept(this));
        node.addChild(n.getS().accept(this));
        return node;
    }

    public Node<SimpleASTNode> visit(Expression e) {
        return makeSimple(e.getClass());
    }

    public Node<SimpleASTNode> visit(BinOp e) {
        val node = makeSimple(e.getClass());
        node.addChild(e.getLhs().accept(this));
        node.addChild(e.getRhs().accept(this));
        return node;
    }

    public Node<SimpleASTNode> visit(UnOp e) {
        val node = makeSimple(e.getClass());
        node.addChild(e.getOperand().accept(this));
        return node;
    }

    public Node<SimpleASTNode> visit(Value<?> e) {
        return new Node<>(new SimpleASTNode(e.getClass(), e.getValue().toString()), null);
    }

}
