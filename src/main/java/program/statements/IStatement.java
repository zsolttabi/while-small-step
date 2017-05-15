package program.statements;

import program.Configuration;
import program.IProgramElement;
import program.State;

import java.util.Set;


public interface IStatement extends IProgramElement {

    StatementConfiguration step(State state);

    Set<Configuration> peek(State state);


}
