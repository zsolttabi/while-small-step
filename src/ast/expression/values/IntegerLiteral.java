package ast.expression.values;


import ast.expression.Expression;
import ast.expression.interfaces.IntegerValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IntegerLiteral implements Expression, IntegerValue {

    @Getter
    private final Integer value;

    @Override
    public IntegerLiteral evaluate() {
        return this;
    }

    @Override
    public Expression step() {
        return this;
    }
}
