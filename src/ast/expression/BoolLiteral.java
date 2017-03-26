package ast.expression;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BoolLiteral extends Expression<Boolean> implements BooleanValue {

    @Getter
    private final Boolean value;

    @Override
    public BoolLiteral evaluate() {
        return this;
    }
}
