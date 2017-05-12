package program.statements;

import program.Configuration;
import program.IASTNode;
import program.State;


public interface Statement extends IASTNode {

    StatementConfiguration step(State state);

    Configuration next(State state);

}
