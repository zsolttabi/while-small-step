package utils;

import app.SimpleASTNode;
import ast.AST;
import ast.BadAST;
import ast.expression.Identifier;
import ast.expression.interfaces.BadExpression;
import ast.expression.operations.bad_operations.BadBinOp;
import ast.expression.operations.bad_operations.BadUnOp;
import ast.expression.operations.BinOp;
import ast.expression.operations.UnOp;
import ast.expression.interfaces.Value;
import ast.statement.*;
import ast.statement.bad_statements.BadAssignment;
import ast.statement.bad_statements.BadIf;
import ast.statement.bad_statements.BadSequence;
import ast.statement.bad_statements.BadWhile;
import lombok.val;
import utils.Tree.Node;

public class SimpleTreeBuilder implements Visitor<Node<SimpleASTNode>> {

    private static Node<SimpleASTNode> makeSimple(Class<?> clazz, boolean isBad) {
        return new Node<>(new SimpleASTNode(clazz, null, isBad), null);
    }

    private static Node<SimpleASTNode> makeSimple(Class<?> clazz, String value, boolean isBad) {
        return new Node<>(new SimpleASTNode(clazz, value, isBad), null);
    }


    @Override
    public Node<SimpleASTNode> visit(AST element) {
        return element.getStm() == null ?
                new Node<>(new SimpleASTNode(element.getClass(), "EMPTY", element instanceof BadAST), null) :
                element.getStm().accept(this);
    }

    @Override
    public Node<SimpleASTNode> visit(Skip element) {
        return makeSimple(element.getClass(), "SKIP", false);
    }

    @Override
    public Node<SimpleASTNode> visit(Sequence element) {
        val node = makeSimple(element.getClass(), ";", element instanceof BadSequence);
        if (element.getS1() != null) {
            node.addChild(element.getS1().accept(this));
        }
        if (element.getS2() != null) {
            node.addChild(element.getS2().accept(this));
        }
        return node;
    }

    @Override
    public Node<SimpleASTNode> visit(Assignment element) {
        val node = makeSimple(element.getClass(), ":=", element instanceof BadAssignment);
        if (element.getIdentifier() != null) {
            node.addChild(element.getIdentifier().accept(this));
        }
        if (element.getValue() != null) {
            node.addChild(element.getValue().accept(this));
        }
        return node;
    }


    @Override
    public Node<SimpleASTNode> visit(If element) {
        val node = makeSimple(element.getClass(), "if", element instanceof BadIf);
        if (element.getCondition() != null) {
            node.addChild(element.getCondition().accept(this));
        }
        if (element.getS1() != null) {
            node.addChild(element.getS1().accept(this));
        }
        if (element.getS2() != null) {
            node.addChild(element.getS2().accept(this));
        }
        return node;
    }

    @Override
    public Node<SimpleASTNode> visit(While element) {
        val node = makeSimple(element.getClass(), "while", element instanceof BadWhile);
        if (element.getCondition() != null) {
            node.addChild(element.getCondition().accept(this));
        }
        if (element.getS() != null) {
            node.addChild(element.getS().accept(this));
        }
        return node;
    }

    @Override
    public Node<SimpleASTNode> visit(BinOp element) {
        val node = makeSimple(element.getClass(), element.getOperator(), element instanceof BadBinOp);
        if (element.getLhs() != null) {
            node.addChild(element.getLhs().accept(this));
        }
        if (element.getRhs() != null) {
            node.addChild(element.getRhs().accept(this));
        }
        return node;
    }

    @Override
    public Node<SimpleASTNode> visit(UnOp element) {
        val node = makeSimple(element.getClass(), element.getOperator(), element instanceof BadUnOp);
        if (element.getOperand() != null) {
            node.addChild(element.getOperand().accept(this));
        }
        return node;
    }

    @Override
    public Node<SimpleASTNode> visit(Value<?> element) {
        return new Node<>(new SimpleASTNode(element.getClass(), element.getValue() == null ? "" : element.getValue().toString(), element instanceof BadExpression), null);
    }

    @Override
    public Node<SimpleASTNode> visit(Identifier element) {
        return new Node<>(new SimpleASTNode(element.getClass(), element.getIdentifier(), element instanceof BadExpression), null);
    }

}
