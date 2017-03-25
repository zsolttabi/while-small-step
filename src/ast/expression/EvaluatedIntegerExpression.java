package ast.expression;

public class EvaluatedIntegerExpression extends EvaluatedExpression<Integer> implements IntegerValue {


    public EvaluatedIntegerExpression(Integer value) {
        super(value);
    }

    @Override
    public EvaluatedIntegerExpression evaluate() {
        return this;
    }

}
