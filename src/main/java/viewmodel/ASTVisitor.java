package viewmodel;

import lombok.val;
import program.Configuration;
import program.Configuration.ConfigType;
import program.IProgramElement;
import program.Program;
import program.expressions.BinOp;
import program.expressions.Identifier;
import program.expressions.UnOp;
import program.expressions.Value;
import program.statements.*;
import utils.Tree;
import utils.Tree.Node;
import viewmodel.ASTNode.NodeType;
import viewmodel.interfaces.INodeVisitor;

import static program.Configuration.ConfigType.TERMINATED;

public class ASTVisitor implements INodeVisitor<Node<ASTNode>> {

    private Configuration currentConfiguration;
    private Configuration nextConfiguration;

    public static Tree<ASTNode> visitAST(Program program) {
        return new Tree<>(new ASTVisitor().visit(program));
    }

    private Node<ASTNode> createNode(String label, IProgramElement element) {
        return new Node<>(new ASTNode(label, getNodeType(element)), null);
    }

    private NodeType getNodeType(IProgramElement element) {
        if (currentConfiguration.getConfigType() == ConfigType.STUCK && nextConfiguration.getNode() == element) {
            return NodeType.STUCK;
        }
        if (currentConfiguration.getConfigType() == ConfigType.TERMINATED && nextConfiguration.getNode() == element) {
            return NodeType.TERMINATED;
        }
        if (currentConfiguration.getConfigType() == ConfigType.INTERMEDIATE && nextConfiguration.getNode() == element) {
            return NodeType.NEXT;
        }
        return NodeType.NORMAL;
    }

    @Override
    public Node<ASTNode> visit(Program element) {
        this.currentConfiguration = element.getCurrentConfiguration();
        this.nextConfiguration = element.next();
        return element.getCurrentConfiguration().accept(this);
    }

    @Override
    public Node<ASTNode> visit(Configuration element) {
        return element.getConfigType() == TERMINATED ?
                new Node<>(new ASTNode("TERMINATED", NodeType.TERMINATED), null) :
                element.getNode().accept(this);
    }

    @Override
    public Node<ASTNode> visit(Skip element) {
        return createNode("SKIP", element);
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
        if (element.getStm() != null) {
            node.addChild(element.getStm().accept(this));
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
        return createNode("ABORT", element);
    }

    @Override
    public Node<ASTNode> visit(Or element) {
        val node = createNode("OR", element);
        node.addChild(element.getS1().accept(this));
        node.addChild(element.getS2().accept(this));
        return node;
    }

}
