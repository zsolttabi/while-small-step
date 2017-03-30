package ast.expression;

import ast.expression.abstract_operations.ArithBinOp;

public class Plus extends ArithBinOp {

    protected Plus(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression evaluate() {
        return evaluate(Integer::sum);
    }

    @Override
    public Expression step() {
        return step(Plus::new, Integer::sum);
    }

}
