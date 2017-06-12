package viewmodel;

import lombok.val;
import program.*;
import program.Configuration.ConfigType;
import program.Exception;
import program.expressions.BinOp;
import program.expressions.Identifier;
import program.expressions.UnOp;
import program.expressions.Value;
import program.expressions.literals.Literal;
import program.statements.*;
import utils.Tree;
import utils.Tree.Node;
import viewmodel.SimpleAstNode.NodeType;
import viewmodel.interfaces.INodeVisitor;

import java.util.Set;

import static program.Configuration.ConfigType.TERMINATED;
import static viewmodel.SimpleAstNode.NodeType.SYNTAX_ERROR;

public class SimpleAstBuilder implements INodeVisitor<Node<SimpleAstNode>> {

    private Configuration currentConfiguration;
    private Set<Configuration> nextConfigurations;

    private boolean isNext(IProgramElement element) {
        return nextConfigurations.stream().anyMatch(c -> c.getElement() == element);
    }

    public static Tree<SimpleAstNode> visitAST(Program program) {
        return new Tree<>(new SimpleAstBuilder().visit(program));
    }

    private Node<SimpleAstNode> createNode(String label, IProgramElement element) {
        return new Node<>(new SimpleAstNode(label, getNodeType(element)), null);
    }

    private NodeType getNodeType(IProgramElement element) {
        if (currentConfiguration.getConfigType() == ConfigType.STUCK && isNext(element)) {
            return NodeType.STUCK;
        }
        if (currentConfiguration.getConfigType() == ConfigType.TERMINATED && isNext(element)) {
            return NodeType.TERMINATED;
        }
        if (currentConfiguration.getConfigType() == ConfigType.INTERMEDIATE && isNext(element)) {
            return NodeType.NEXT;
        }
        return NodeType.NORMAL;
    }

    @Override
    public Node<SimpleAstNode> visit(Program element) {
        this.currentConfiguration = element.current();
        this.nextConfigurations = element.peek();
        return element.current().accept(this);
    }

    @Override
    public Node<SimpleAstNode> visit(Configuration element) {
        return element.getConfigType() == TERMINATED ?
                new Node<>(new SimpleAstNode("[terminated]", NodeType.TERMINATED), null) :
                element.getElement().accept(this);
    }

    @Override
    public Node<SimpleAstNode> visit(Skip element) {
        return createNode("skip", element);
    }

    @Override
    public Node<SimpleAstNode> visit(Sequence element) {
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
    public Node<SimpleAstNode> visit(Assignment element) {
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
    public Node<SimpleAstNode> visit(If element) {
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
    public Node<SimpleAstNode> visit(While element) {
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
    public Node<SimpleAstNode> visit(BinOp element) {
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
    public Node<SimpleAstNode> visit(UnOp element) {
        val node = createNode(element.getOperator(), element);
        if (element.getOperand() != null) {
            node.addChild(element.getOperand().accept(this));
        }
        return node;
    }

    @Override
    public Node<SimpleAstNode> visit(Value<?> element) {
        return createNode(element.getValue() == null ? "" : element.getValue().toString(), element);
    }

    @Override
    public Node<SimpleAstNode> visit(Identifier element) {
        return createNode(element.getIdentifier(), element);
    }

    @Override
    public Node<SimpleAstNode> visit(Abort element) {
        return createNode("abort", element);
    }

    @Override
    public Node<SimpleAstNode> visit(Or element) {
        val node = createNode("or", element);
        node.addChild(element.getS1().accept(this));
        node.addChild(element.getS2().accept(this));
        return node;
    }

    @Override
    public Node<SimpleAstNode> visit(Par element) {
        val node = createNode("par", element);
        node.addChild(element.getS1().accept(this));
        node.addChild(element.getS2().accept(this));
        return node;
    }

    @Override
    public Node<SimpleAstNode> visit(SyntaxError element) {
        return new Node<>(new SimpleAstNode(element.getText(), SYNTAX_ERROR), null);
    }

    @Override
    public Node<SimpleAstNode> visit(Literal element) {
        return createNode(element.getValue(), element);
    }

    @Override
    public Node<SimpleAstNode> visit(Exception element) {
        return createNode(element.getName(), element);
    }

    @Override
    public Node<SimpleAstNode> visit(Throw element) {
        val node = createNode("throw", element);
        node.addChild(element.getE().accept(this));
        return node;
    }

    @Override
    public Node<SimpleAstNode> visit(TryCatch element) {
        val node = createNode("try", element);
        node.addChild(element.getS1().accept(this));
        node.addChild(element.getE().accept(this));
        node.addChild(element.getS2().accept(this));
        return node;
    }

}
