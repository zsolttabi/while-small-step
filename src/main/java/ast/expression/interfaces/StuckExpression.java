package ast.expression.interfaces;


import ast.ExprConfig;
import ast.State;

public interface StuckExpression extends Expression {

    @Override
    ExprConfig step(State state);

}
