package program.expressions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.State;
import viewmodel.interfaces.INodeVisitor;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

import static program.Configuration.ConfigType.*;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class UnOp<T, R> implements IExpression {

    @Getter
    private final String operator;
    @Getter
    private final IExpression operand;
    private final Class<T> operandClass;
    private final Function<T, R> operatorFunction;

    public static <T, R> UnOp<T, R> of(String operator, IExpression operand, Class<T> operandClass, Function<T, R> operatorFunction) {
        return new UnOp<>(operator, operand, operandClass, operatorFunction);
    }

    public static UnOp<Integer, Integer> arithmetic(String operator, IExpression operand, Function<Integer, Integer> operatorFunction) {
        return UnOp.of(operator, operand, Integer.class, operatorFunction);
    }

    public static UnOp<Boolean, Boolean> logical(String operator, IExpression operand, Function<Boolean, Boolean> operatorFunction) {
        return UnOp.of(operator, operand, Boolean.class, operatorFunction);
    }

    @Override
    public ExpressionConfiguration step(State state) {

        if (!(operand instanceof Value)) {
            ExpressionConfiguration operandConf = operand.step(state);
            return new ExpressionConfiguration(new UnOp<>(operator,
                    operandConf.getNode(),
                    operandClass,
                    operatorFunction), operandConf.getState(), operandConf.getConfigType() == STUCK ? STUCK : INTERMEDIATE);
        }

        Object operandValue = ((Value) operand).getValue();
        if (!operandValue.getClass().equals(operandClass)) {
            return new ExpressionConfiguration(this, state, STUCK);
        }

        return new ExpressionConfiguration(new Value<>(operatorFunction.apply(operandClass.cast(operandValue))),
                state,
                TERMINATED);
    }

    @Override
    public Set<Configuration> peek(State state) {
        if (!(operand instanceof Value)) {
            return operand.peek(state);
        }
        return Collections.singleton(new ExpressionConfiguration(this, state, INTERMEDIATE));
    }

    @Override
    public IExpression copy() {
        return new UnOp<>(operator, operand.copy(), operandClass, operatorFunction);
    }

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }

    public enum Arithmetic {

        NEG(o -> UnOp.arithmetic("-", o, a -> -a));

        private final Function<IExpression, UnOp<Integer, Integer>> operationProvider;

        Arithmetic(Function<IExpression, UnOp<Integer, Integer>> operationProvider) {
            this.operationProvider = operationProvider;
        }

        public UnOp<Integer, Integer> of(IExpression operand) {
            return operationProvider.apply(operand);
        }
    }

    public enum Logical {

        NOT(o -> UnOp.logical("!", o, a -> !a));

        private final Function<IExpression, UnOp<Boolean, Boolean>> operationProvider;

        Logical(Function<IExpression, UnOp<Boolean, Boolean>> operationProvider) {
            this.operationProvider = operationProvider;
        }

        public UnOp<Boolean, Boolean> of(IExpression operand) {
            return operationProvider.apply(operand);
        }
    }

}
