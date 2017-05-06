package viewmodel;

import ast.AST;
import ast.BadAST;
import ast.expression.Identifier;
import ast.expression.interfaces.BadExpression;
import ast.expression.interfaces.Value;
import ast.expression.operations.BinOp;
import ast.expression.operations.UnOp;
import ast.expression.operations.bad_operations.BadBinOp;
import ast.expression.operations.bad_operations.BadUnOp;
import ast.statement.*;
import ast.statement.bad_statements.BadAssignment;
import ast.statement.bad_statements.BadIf;
import ast.statement.bad_statements.BadSequence;
import ast.statement.bad_statements.BadWhile;
import lombok.val;
import utils.Tree;
import utils.Tree.Node;
import viewmodel.interfaces.IASTVisitor;

public class ASTVisitor implements IASTVisitor<Node<ASTNode>> {

    public static Tree<ASTNode> visitAST(AST ast) {
        return new Tree<>(new ASTVisitor().visit(ast));
    }

    private static Node<ASTNode> createNode(Class<?> clazz, boolean isBad) {
        return new Node<>(new ASTNode(clazz, null, isBad), null);
    }

    private static Node<ASTNode> createNode(Class<?> clazz, String value, boolean isBad) {
        return new Node<>(new ASTNode(clazz, value, isBad), null);
    }

    @Override
    public Node<ASTNode> visit(AST element) {
        return element.getStm() == null ?
                new Node<>(new ASTNode(element.getClass(), "EMPTY", element instanceof BadAST), null) :
                element.getStm().accept(this);
    }

    @Override
    public Node<ASTNode> visit(Skip element) {
        return createNode(element.getClass(), "SKIP", false);
    }

    @Override
    public Node<ASTNode> visit(Sequence element) {
        val node = createNode(element.getClass(), ";", element instanceof BadSequence);
        if (element.getS1() != null) {
            node.addChild(element.getS1().accept(this));
        }
        if (element.getS2() != null) {
            node.addChild(element.getS2().accept(this));
        }
        return node;
    }

    @Override
    public Node<ASTNode> visit(Assignment element) {
        val node = createNode(element.getClass(), ":=", element instanceof BadAssignment);
        if (element.getIdentifier() != null) {
            node.addChild(element.getIdentifier().accept(this));
        }
        if (element.getValue() != null) {
            node.addChild(element.getValue().accept(this));
        }
        return node;
    }

    @Override
    public Node<ASTNode> visit(If element) {
        val node = createNode(element.getClass(), "if", element instanceof BadIf);
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
    public Node<ASTNode> visit(While element) {
        val node = createNode(element.getClass(), "while", element instanceof BadWhile);
        if (element.getCondition() != null) {
            node.addChild(element.getCondition().accept(this));
        }
        if (element.getS() != null) {
            node.addChild(element.getS().accept(this));
        }
        return node;
    }

    @Override
    public Node<ASTNode> visit(BinOp element) {
        val node = createNode(element.getClass(), element.getOperator(), element instanceof BadBinOp);
        if (element.getLhs() != null) {
            node.addChild(element.getLhs().accept(this));
        }
        if (element.getRhs() != null) {
            node.addChild(element.getRhs().accept(this));
        }
        return node;
    }

    @Override
    public Node<ASTNode> visit(UnOp element) {
        val node = createNode(element.getClass(), element.getOperator(), element instanceof BadUnOp);
        if (element.getOperand() != null) {
            node.addChild(element.getOperand().accept(this));
        }
        return node;
    }

    @Override
    public Node<ASTNode> visit(Value<?> element) {
        return new Node<>(new ASTNode(element.getClass(), element.getValue() == null ? "" : element.getValue().toString(), element instanceof BadExpression), null);
    }

    @Override
    public Node<ASTNode> visit(Identifier element) {
        return new Node<>(new ASTNode(element.getClass(), element.getIdentifier(), element instanceof BadExpression), null);
    }

}
