package program.expressions;


import program.IASTNode;
import program.State;

public interface Expression extends IASTNode {

    ExpressionConfiguration step(State state);

    ExpressionConfiguration next(State state);

}
