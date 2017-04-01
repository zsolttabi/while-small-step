package ast;

import app.SimpleASTNode;
import ast.statement.BadStatement;
import ast.statement.Statement;
import lombok.Getter;
import utils.Element;
import utils.Pair;
import utils.Tree.Node;
import utils.Visitor;

public class AST implements IAST, Element<Node<SimpleASTNode>> {

    @Getter
    private final State state;
    @Getter
    private final Statement stm;

    public AST(Statement stm) {
        this.stm = stm;
        state = new State();
    }

    AST(Pair<Statement, State> newConfig) {
        this.stm = newConfig.getFirst();
        this.state = newConfig.getSecond();
    }

    @Override
    public IAST step() {

        Pair<Statement, State> newConfig = stm.step(state);

        if (newConfig.getFirst() instanceof BadStatement) {
            return new BadAST(newConfig);
        }

        return newConfig.getFirst() == null ? new ReducedAST(newConfig) : new AST(newConfig);
    }

    public IAST reduce() {

        IAST newAST = step();
        while (!(newAST instanceof ReducedAST) && !(newAST instanceof BadAST)) {
            newAST = newAST.step();
        }

        return newAST;
    }

    @Override
    public Node<SimpleASTNode> accept(Visitor<Node<SimpleASTNode>> visitor) {
        return visitor.visit(this);
    }

}

