package ast.expression;


import ast.expression.interfaces.BooleanValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoolLiteral implements Expression, BooleanValue {

    @Getter
    private final Boolean value;

    @Override
    public BoolLiteral evaluate() {
        return this;
    }
}
