package program.statements;


import program.Configuration;
import program.State;

public class StatementConfiguration extends Configuration {

    public StatementConfiguration(IStatement node, State state, ConfigType configType) {
        super(node, state, configType);
    }

    @Override
    public IStatement getElement() {
        return (IStatement) super.getElement();
    }

}
