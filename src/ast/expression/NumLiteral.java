package ast.expression;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NumLiteral extends Expression<Integer> implements IntegerValue {

    @Getter
    private final Integer value;

    @Override
    public NumLiteral evaluate() {
        return this;
    }
}
