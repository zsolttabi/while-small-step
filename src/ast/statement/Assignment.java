package ast.statement;

import ast.State;
import ast.expression.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import utils.Pair;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class Assignment extends Statement {

    @Getter
    private final Var<?> var;
    @Getter
    private final Expression<?> value;

    @Override
    public Pair<Statement, State> run(State state) {
        if (var instanceof BooleanVar && ! (value instanceof BooleanExpression) ||
                var instanceof IntegerVar && ! (value instanceof IntegerExpression)) {
            throw new EvaluationException("Lhs and rhs of assignment do not match", Stream.of(var, value).collect(Collectors.toList()));
        }
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
