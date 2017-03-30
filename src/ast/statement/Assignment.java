package ast.statement;

import ast.State;
import ast.expression.Expression;
import ast.expression.interfaces.BooleanValue;
import ast.expression.interfaces.IntegerValue;
import ast.expression.interfaces.Value;
import ast.expression.values.BooleanVar;
import ast.expression.values.IntegerVar;
import ast.expression.values.Var;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Pair;

@RequiredArgsConstructor
public class Assignment implements Statement {

    @Getter
    private final Var<?> var;
    @Getter
    private final Expression value;

    @Override
    public Pair<Statement, State> step(State state) {

        if (!(value instanceof Value)) {
            return Pair.of(new Assignment(var, value.step()), state);
        }

        if (value instanceof BooleanValue && var instanceof BooleanVar) {
            ((BooleanVar)var).setValue(((BooleanValue) value).getValue());
        } else if (value instanceof IntegerValue && var instanceof IntegerVar) {
            ((IntegerVar)var).setValue(((IntegerValue) value).getValue());
        } else {
            return Pair.of(new BadAssignment(var, value), state);
        }

        state.setVar(var.getIdentifier(), var);
        return Pair.of(null, state);
    }

}
