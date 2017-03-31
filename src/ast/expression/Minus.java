package ast.expression;

import ast.expression.abstract_operations.ArithBinOp;

public class Minus extends ArithBinOp {

    public Minus(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression evaluate() {
        return evaluate((i1, i2) -> i1 - i2);
    }

    @Override
    public Expression step() {
        return step(Minus::new, (i1, i2) -> i1 - i2);
    }
}
