package ast.statement;

import ast.State;
import ast.StmConfig;
import ast.statement.interfaces.Statement;
import lombok.EqualsAndHashCode;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.IASTElement;
import viewmodel.interfaces.IASTVisitor;

@EqualsAndHashCode
public class Skip implements Statement, IASTElement<Tree.Node<ASTNode>> {

    @Override
    public StmConfig step(State state) {
        return StmConfig.endConfig(state);
    }

    @Override
    public Tree.Node<ASTNode> accept(IASTVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
