package program.expressions;


import program.Configuration;
import program.State;

public class ExpressionConfiguration extends Configuration {

    public ExpressionConfiguration(IExpression node, State state, ConfigType configType) {
        super(node, state, configType);
    }

    @Override
    public IExpression getNode() {
        return (IExpression) super.getNode();
    }

}
