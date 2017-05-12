package program.statements;

import program.Configuration;
import program.IProgramElement;
import program.State;


public interface IStatement extends IProgramElement {

    StatementConfiguration step(State state);

    Configuration next(State state);

}
