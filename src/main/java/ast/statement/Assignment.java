package ast.statement;

import ast.State;
import ast.StmConfig;
import ast.expression.Identifier;
import ast.expression.interfaces.Expression;
import ast.expression.interfaces.StuckExpression;
import ast.expression.interfaces.Value;
import ast.statement.bad_statements.StuckAssignment;
import ast.statement.interfaces.Statement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.IASTElement;
import viewmodel.interfaces.IASTVisitor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class Assignment implements Statement, IASTElement<Tree.Node<ASTNode>> {

    public static Assignment of(Expression identifier, Expression value) {
        return identifier == null || value == null ? new StuckAssignment(identifier, value) : new Assignment(identifier, value);
    }

    @Getter
    private final Expression identifier;
    @Getter
    private final Expression value;

    @Override
    public StmConfig step(State state) {

        if (identifier instanceof StuckExpression || value instanceof StuckExpression) {
            return StmConfig.of(new StuckAssignment(identifier, value), state);
        }

        if(!(identifier instanceof Identifier)) {
            return StmConfig.of(new StuckAssignment(identifier, value), state);
        }

        if (!(value instanceof Value)) {
            return StmConfig.of(new Assignment(identifier, value.step(state).getExpression()), state);
        }

        Identifier id = (Identifier) identifier;

        Value currentValue = state.get(id);
        State newState = state.copy();
        if (currentValue == null || value.getClass() == currentValue.getClass()) {
            newState.set(id, (Value) value);
            return StmConfig.endConfig(newState);
        }

        return StmConfig.of(new StuckAssignment(id, value), state);
    }

    @Override
    public Tree.Node<ASTNode> accept(IASTVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
