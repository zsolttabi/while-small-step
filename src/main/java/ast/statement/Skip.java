package ast.statement;

import ast.State;
import ast.statement.interfaces.Statement;
import lombok.EqualsAndHashCode;
import utils.Pair;
import utils.Tree;
import viewmodel.interfaces.IASTElement;
import viewmodel.ASTNode;
import viewmodel.interfaces.IASTVisitor;

@EqualsAndHashCode
public class Skip implements Statement, IASTElement<Tree.Node<ASTNode>> {

    @Override
    public Pair<Statement, State> step(State state) {
        return Pair.of(null, state);
    }

    @Override
    public Tree.Node<ASTNode> accept(IASTVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
