package ast.expression;

import java.util.Objects;

public class Equilty extends BinaryIntegerOperation<Boolean> {

    public Equilty(IntegerExpression lhs, IntegerExpression rhs) {
        super(lhs, rhs);
    }

    @Override
    public EvaluatedExpression<Boolean> evaluate() {
       return evaluate(Objects::equals);
    }

}
