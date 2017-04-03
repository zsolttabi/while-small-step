package ast.statement;

import app.SimpleASTNode;
import ast.State;
import ast.expression.interfaces.Expression;
import ast.expression.Identifier;
import ast.expression.interfaces.Value;
import ast.expression.values.BoolValue;
import ast.expression.values.IntValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Element;
import utils.Pair;
import utils.Tree;
import utils.Visitor;

@RequiredArgsConstructor
public class Assignment implements Statement, Element<Tree.Node<SimpleASTNode>> {

    @Getter
    private final Expression identifier;
    @Getter
    private final Expression value;

    @Override
    public Pair<Statement, State> step(State state) {

        if(!(identifier instanceof Identifier)) {
            return Pair.of(new BadAssignment(identifier, value), state);
        }

        if (!(value instanceof Value)) {
            return Pair.of(new Assignment(identifier, value.step(state)), state);
        }

        Identifier id = (Identifier)identifier;

        Value<?> currentValue = state.get(id);
        if (currentValue == null) {
            state.set(id, (Value<?>) value);
        } else if (value instanceof BoolValue && currentValue instanceof BoolValue) {
            state.set(id, (BoolValue)value);
        } else if (value instanceof IntValue && currentValue instanceof IntValue) {
            state.set(id, (IntValue)value);
        } else {
            return Pair.of(new BadAssignment(id, value), state);
        }

        return Pair.of(null, state);
    }

    @Override
    public Tree.Node<SimpleASTNode> accept(Visitor<Tree.Node<SimpleASTNode>> visitor) {
        return visitor.visit(this);
    }
}
