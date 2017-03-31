package ast.expression;

import ast.expression.abstract_operations.BoolBinOp;

public class And extends BoolBinOp {

    public And(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression evaluate() {
        return evaluate(Boolean::logicalAnd);
    }

    @Override
    public Expression step() {
        return step(And::new, Boolean::logicalAnd);
    }
}
