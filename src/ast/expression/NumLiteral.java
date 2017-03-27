package ast.expression;


import ast.expression.interfaces.IntegerValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NumLiteral implements Expression, IntegerValue {

    @Getter
    private final Integer value;

    @Override
    public NumLiteral evaluate() {
        return this;
    }
}
