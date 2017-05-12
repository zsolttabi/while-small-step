package program.expressions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import program.Configuration.ConfigType;
import program.State;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;

import java.util.function.Function;

@RequiredArgsConstructor
@EqualsAndHashCode
public class UnOp<T, R> implements Expression {

    public static <T, R> UnOp<T, R> of(String operator,
                                        Expression operand,
                                        Class<? extends Value<T>> operandClass,
                                        Function<R, Value<R>> resultCtor,
                                        Function<T, R> evalFun) {

        return new UnOp<>(operator,
                operand,
                operandClass,
                resultCtor,
                evalFun);
    }

    public static UnOp<Integer, Integer> intOp(String operator, Expression operand, Function<Integer, Integer> evalFun) {
        return UnOp.of(operator, operand, IntValue.class, IntValue::of, evalFun);
    }

    public static UnOp<Integer, Integer> neg(Expression operand) {
        return UnOp.intOp("-", operand, i -> 0 - i);
    }

    public static UnOp<Boolean, Boolean> boolOp(String operator, Expression operand, Function<Boolean, Boolean> evalFun) {
        return UnOp.of(operator, operand, BoolValue.class, BoolValue::of, evalFun);
    }

    public static UnOp<Boolean, Boolean> not(Expression operand) {
        return UnOp.boolOp("not", operand, b -> !b);
    }

    @Getter
    private final String operator;
    @Getter
    private final Expression operand;

    private final Class<? extends Value<T>> operandClass;
    private final Function<R, Value<R>> resultCtor;
    private final Function<T, R> evalFun;

    @Override
    public ExpressionConfiguration step(State state) {

//        if (operand instanceof StuckExpression) {
//            return new ExpressionConfiguration(this, state, ConfigType.STUCK);
//        }

        if (!(operand instanceof Value)) {
            return new ExpressionConfiguration(new UnOp<>(operator, operand.step(state).getNode(), operandClass, resultCtor, evalFun), state, ConfigType.INTERMEDIATE);
        }

        Value val = (Value) operand;

        if ((operandClass.isAssignableFrom(val.getClass()))) {
            return new ExpressionConfiguration(resultCtor.apply(evalFun.apply(operandClass.cast(val).getValue())), state, ConfigType.INTERMEDIATE);
        }

        return new ExpressionConfiguration(this, state, ConfigType.STUCK);
    }

    @Override
    public ExpressionConfiguration next(State state) {
        if (!(operand instanceof Value)) {
            return operand.next(state);
        }
        return new ExpressionConfiguration(this, state, ConfigType.INTERMEDIATE);
    }

    @Override
    public Tree.Node<ASTNode> accept(INodeVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
