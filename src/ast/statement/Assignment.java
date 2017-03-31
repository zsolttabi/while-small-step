package ast.statement;

import ast.State;
import ast.expression.Expression;
import ast.expression.interfaces.BoolValue;
import ast.expression.interfaces.IntValue;
import ast.expression.interfaces.Value;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Pair;

@RequiredArgsConstructor
public class Assignment implements Statement {

    @Getter
    private final String identifier;
    @Getter
    private final Expression value;

    @Override
    public Pair<Statement, State> step(State state) {

        if (!(value instanceof Value)) {
            return Pair.of(new Assignment(identifier, value.step()), state);
        }

        Value<?> currentValue = state.get(identifier);

        if (value instanceof BoolValue && currentValue instanceof BoolValue) {
            state.set(identifier, (BoolValue)value);
        } else if (value instanceof IntValue && currentValue instanceof IntValue) {
            state.set(identifier, (IntValue)value);
        } else {
            return Pair.of(new BadAssignment(identifier, value), state);
        }

        return Pair.of(null, state);
    }

}
