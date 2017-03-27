package ast.statement;

import ast.State;
import ast.expression.*;
import ast.expression.interfaces.BooleanValue;
import ast.expression.interfaces.IntegerValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import utils.Pair;

import java.util.Collections;

@RequiredArgsConstructor
public class Assignment extends Statement {

    @Getter
    private final Var<?> var;
    @Getter
    private final Expression value;

    @Override
    public Pair<Statement, State> run(State state) {
        val evaluated = value.evaluate();
        if (evaluated instanceof BooleanValue && var instanceof BooleanVar) {
            ((BooleanVar)var).setValue(((BooleanValue) evaluated).getValue());
        } else if (evaluated instanceof IntegerValue && var instanceof IntegerVar) {
            ((IntegerVar)var).setValue(((IntegerValue) evaluated).getValue());
        } else {
            throw new EvaluationException("Could not evaluate rhs in Assignment to a Value", Collections.singletonList(value));
        }

        state.setVar(var.getIdentifier(), var);

        return Pair.of(null, state);
    }

}
