package program.expressions;


import program.Configuration;
import program.IProgramElement;
import program.State;

import java.util.Set;

public interface IExpression extends IProgramElement {

    ExpressionConfiguration step(State state);

    Set<Configuration> peek(State state);

}
