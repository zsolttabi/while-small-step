package ast.statement;


import ast.State;
import utils.Pair;

public interface Statement {

    Pair<Statement, State> step(State state);

}
