package ast.expression.values;


import ast.expression.Expression;
import ast.expression.interfaces.BoolValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoolLiteral implements Expression, BoolValue {

    @Getter
    private final Boolean value;

    @Override
    public Expression step() {
        return this;
    }
}
