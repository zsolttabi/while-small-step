package ast.expression;

public class Addition extends BinaryOperationExpression<Integer> {

    public Addition(IntegerExpression lhs, IntegerExpression rhs) {
        super(lhs, rhs);
    }

    @Override
    public EvaluatedExpression<Integer> evaluate() {
        return evaluate(Integer::sum);
    }

}
