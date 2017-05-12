package program.expressions;


import program.Configuration;
import program.State;

public class ExpressionConfiguration extends Configuration {

    public ExpressionConfiguration(Expression node, State state, ConfigType configType) {
        super(node, state, configType);
    }

    @Override
    public Expression getNode() {
        return (Expression) super.getNode();
    }

}
