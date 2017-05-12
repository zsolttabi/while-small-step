package program.expressions;


import program.IProgramElement;
import program.State;

public interface IExpression extends IProgramElement {

    ExpressionConfiguration step(State state);

    ExpressionConfiguration next(State state);

}
