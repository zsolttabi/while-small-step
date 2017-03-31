package ast.expression;

import ast.expression.abstract_operations.RelBinOp;

public class LessThen extends RelBinOp {

    public LessThen(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression step() {
        return step(LessThen::new, (i1,i2) -> i1 < i2);
    }
}
