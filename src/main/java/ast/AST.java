package ast;

import ast.statement.interfaces.BadStatement;
import ast.statement.interfaces.Statement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import utils.Pair;
import utils.Tree.Node;
import viewmodel.ASTNode;
import viewmodel.interfaces.IASTElement;
import viewmodel.interfaces.IASTVisitor;

@EqualsAndHashCode
public class AST implements IAST, IASTElement<Node<ASTNode>> {

    @Getter
    private final State state;
    @Getter
    private final Statement stm;

    public AST(Statement stm) {
        this.stm = stm;
        state = new State();
    }

    protected AST(Pair<Statement, State> newConfig) {
        this.stm = newConfig.getFirst();
        this.state = newConfig.getSecond();
    }

    @Override
    public AST step() {

        Pair<Statement, State> newConfig = stm.step(state);

        if (newConfig.getFirst() instanceof BadStatement) {
            return new BadAST(newConfig);
        }

        return newConfig.getFirst() == null ? new ReducedAST(newConfig) : new AST(newConfig);
    }

    public AST reduce() {

        AST newAST = step();
        while (!(newAST instanceof ReducedAST) && !(newAST instanceof BadAST)) {
            newAST = newAST.step();
        }

        return newAST;
    }

    @Override
    public Node<ASTNode> accept(IASTVisitor<Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}

