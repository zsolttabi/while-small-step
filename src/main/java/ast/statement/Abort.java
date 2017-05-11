package ast.statement;

import ast.State;
import ast.StmConfig;
import ast.StuckStmConfig;
import ast.statement.interfaces.Statement;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.IASTVisitor;

public class Abort implements Statement {

    @Override
    public Tree.Node<ASTNode> accept(IASTVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

    @Override
    public StmConfig step(State state) {
        return StuckStmConfig.of(this, state);
    }
}
