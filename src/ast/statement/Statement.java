package ast.statement;


import ast.State;
import utils.Pair;

public abstract class Statement {

    public abstract Pair<Statement, State> run(State state);

}
