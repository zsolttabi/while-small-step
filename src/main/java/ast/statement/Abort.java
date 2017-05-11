package ast.statement;

import ast.State;
import ast.statement.interfaces.BadStatement;
import ast.statement.interfaces.Statement;
import utils.Pair;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.IASTVisitor;

public class Abort implements BadStatement {

    @Override
    public Pair<Statement, State> step(State state) {
        return stuckStep(state);
    }

    @Override
    public Tree.Node<ASTNode> accept(IASTVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
