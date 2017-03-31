package ast.expression.values;


import ast.expression.Expression;
import ast.expression.interfaces.IntValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IntLiteral implements Expression, IntValue {

    @Getter
    private final Integer value;

    @Override
    public IntLiteral evaluate() {
        return this;
    }

    @Override
    public Expression step() {
        return this;
    }
}
