package viewmodel;

import ast.AST;
import ast.BadAST;
import ast.expression.Identifier;
import ast.expression.interfaces.BadExpression;
import ast.expression.interfaces.Expression;
import ast.expression.interfaces.Value;
import ast.expression.operations.BinOp;
import ast.expression.operations.UnOp;
import ast.statement.*;
import ast.statement.interfaces.BadStatement;
import ast.statement.interfaces.Statement;
import lombok.val;
import utils.Tree;
import utils.Tree.Node;
import viewmodel.interfaces.IASTVisitor;

public class ASTVisitor implements IASTVisitor<Node<ASTNode>> {

    public static Tree<ASTNode> visitAST(AST ast) {
        return new Tree<>(new ASTVisitor().visit(ast));
    }

    private static Node<ASTNode> createNode(String label, boolean isBad) {
        return new Node<>(new ASTNode(label, isBad), null);
    }

    private static Node<ASTNode> createNode(String label, Expression exp) {
        return createNode(label, exp instanceof BadExpression);
    }

    private static Node<ASTNode> createNode(String label, Statement s) {
        return createNode(label, s instanceof BadStatement);
    }

    @Override
    public Node<ASTNode> visit(AST element) {
        return element.getStm() == null ?
                createNode("EMPTY", element instanceof BadAST) :
                element.getStm().accept(this);
    }

    @Override
    public Node<ASTNode> visit(Skip element) {
        return createNode("SKIP", false);
    }

    @Override
    public Node<ASTNode> visit(Sequence element) {
        val node = createNode(";", element);
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
        val node = createNode(":=", element);
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
        val node = createNode("if", element);
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
        val node = createNode("while", element);
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
        val node = createNode(element.getOperator(), element);
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
        val node = createNode(element.getOperator(), element);
        if (element.getOperand() != null) {
            node.addChild(element.getOperand().accept(this));
        }
        return node;
    }

    @Override
    public Node<ASTNode> visit(Value<?> element) {
        return createNode(element.getValue() == null ? "" : element.getValue().toString(), element);
    }

    @Override
    public Node<ASTNode> visit(Identifier element) {
        return createNode(element.getIdentifier(), element);
    }

    @Override
    public Node<ASTNode> visit(Abort element) {
        return createNode("ABORT", true);
    }

}
