package ast;

import ast.statement.interfaces.Statement;
import ast.statement.interfaces.StuckStatement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import utils.Tree.Node;
import viewmodel.ASTNode;
import viewmodel.interfaces.IASTElement;
import viewmodel.interfaces.IASTVisitor;

@EqualsAndHashCode
public class AST implements IAST, IASTElement<Node<ASTNode>> {

    @Getter
    private final StmConfig config;

    public AST(Statement stm) {
        config = StmConfig.of(stm, new State());
    }

    protected AST(StmConfig config) {
        this.config = config;
    }

    @Override
    public AST step() {

        StmConfig newConfig = config.step();

        if (newConfig.getStatement() instanceof StuckStatement) {
            return new BadAST(newConfig);
        }

        return newConfig.getStatement() == null ? new ReducedAST(newConfig) : new AST(newConfig);
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

