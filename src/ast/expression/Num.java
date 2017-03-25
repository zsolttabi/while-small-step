package ast.expression;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Num extends Expression<Integer> implements IntegerValue {

    @Getter
    private final Integer value;

    @Override
    public Num evaluate() {
        return this;
    }
}
