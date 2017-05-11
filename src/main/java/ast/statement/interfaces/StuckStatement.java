package ast.statement.interfaces;

import ast.State;
import ast.StmConfig;


public interface StuckStatement extends Statement {

    StmConfig step(State state);

}
