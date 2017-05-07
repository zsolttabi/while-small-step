package ast.statement;

import ast.State;
import ast.expression.Identifier;
import ast.expression.interfaces.BadExpression;
import ast.expression.interfaces.Expression;
import ast.expression.interfaces.Value;
import ast.expression.values.BoolValue;
import ast.expression.values.IntValue;
import ast.statement.bad_statements.BadAssignment;
import ast.statement.interfaces.Statement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Pair;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.IASTElement;
import viewmodel.interfaces.IASTVisitor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class Assignment implements Statement, IASTElement<Tree.Node<ASTNode>> {

    public static Assignment of(Expression identifier, Expression value) {
        return identifier == null || value == null ? new BadAssignment(identifier, value) : new Assignment(identifier, value);
    }

    @Getter
    private final Expression identifier;
    @Getter
    private final Expression value;

    @Override
    public Pair<Statement, State> step(State state) {

        if (identifier instanceof BadExpression || value instanceof BadExpression) {
            return Pair.of(new BadAssignment(identifier, value), state);
        }

        if(!(identifier instanceof Identifier)) {
            return Pair.of(new BadAssignment(identifier, value), state);
        }

        if (!(value instanceof Value)) {
            return Pair.of(new Assignment(identifier, value.step(state)), state);
        }

        Identifier id = (Identifier)identifier;

        Value currentValue = state.get(id);
        if (currentValue == null) {
            state.set(id, (Value) value);
        } else if (value instanceof BoolValue && currentValue instanceof BoolValue) {
            state.set(id, (BoolValue) value);
        } else if (value instanceof IntValue && currentValue instanceof IntValue) {
            state.set(id, (IntValue) value);
        } else {
            return Pair.of(new BadAssignment(id, value), state);
        }

        return Pair.of(null, state);
    }

    @Override
    public Tree.Node<ASTNode> accept(IASTVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
