package program.statements;


import program.Configuration;
import program.State;

public class StatementConfiguration extends Configuration {

    public StatementConfiguration(Statement node, State state, ConfigType configType) {
        super(node, state, configType);
    }

    @Override
    public Statement getNode() {
        return (Statement) super.getNode();
    }

}
