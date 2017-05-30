package program.expressions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.State;
import viewmodel.interfaces.INodeVisitor;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiFunction;

import static program.Configuration.ConfigType.*;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class BinOp<T, R> implements IExpression {

    @Getter
    private final String operator;
    @Getter
    private final IExpression lhs;
    @Getter
    private final IExpression rhs;
    private final Class<T> operandClass;
    private final BiFunction<T, T, R> operatorFunction;

    public static <T, R> BinOp<T, R> of(String operator, IExpression lhs, IExpression rhs, Class<T> operandClass, BiFunction<T, T, R> operatorFunction) {
        return new BinOp<>(operator, lhs, rhs, operandClass, operatorFunction);
    }

    public static BinOp<BigInteger, BigInteger> arithmetic(String operator, IExpression lhs, IExpression rhs, BiFunction<BigInteger, BigInteger, BigInteger> operatorFunction) {
        return BinOp.of(operator, lhs, rhs, BigInteger.class, operatorFunction);
    }

    public static BinOp<Boolean, Boolean> logical(String operator, IExpression lhs, IExpression rhs, BiFunction<Boolean, Boolean, Boolean> operatorFunction) {
        return BinOp.of(operator, lhs, rhs, Boolean.class, operatorFunction);
    }

    public static BinOp<BigInteger, Boolean> relational(String operator, IExpression lhs, IExpression rhs, BiFunction<BigInteger, BigInteger, Boolean> operatorFunction) {
        return BinOp.of(operator, lhs, rhs, BigInteger.class, operatorFunction);
    }

    public static BinOp<BigInteger, Boolean> relational2(String operator, IExpression lhs, IExpression rhs, BiFunction<BigInteger, BigInteger, Boolean> operatorFunction) {
        return BinOp.of(operator, lhs, rhs, BigInteger.class, operatorFunction);
    }

    @Override
    public ExpressionConfiguration step(State state) {

        if (!(lhs instanceof Value)) {
            ExpressionConfiguration lhsConf = lhs.step(state);
            return new ExpressionConfiguration(BinOp.of(operator,
                    lhsConf.getNode(),
                    rhs,
                    operandClass,
                    operatorFunction), lhsConf.getState(), lhsConf.getConfigType() == STUCK ? STUCK : INTERMEDIATE);
        }

        if (!(rhs instanceof Value)) {
            ExpressionConfiguration rhsConf = rhs.step(state);
            return new ExpressionConfiguration(BinOp.of(operator,
                    lhs,
                    rhsConf.getNode(),
                    operandClass,
                    operatorFunction), rhsConf.getState(), rhsConf.getConfigType() == STUCK ? STUCK : INTERMEDIATE);
        }

        Object lhsValue = ((Value) lhs).getValue();
        Object rhsValue = ((Value) rhs).getValue();

        if (!lhsValue.getClass().equals(operandClass) || !rhsValue.getClass().equals(operandClass)) {
            return new ExpressionConfiguration(new BinOp<>(operator, lhs, rhs, operandClass, operatorFunction),
                    state,
                    STUCK);
        }


        return new ExpressionConfiguration(new Value<>(operatorFunction.apply(operandClass.cast(lhsValue),
                operandClass.cast(rhsValue))),
                state,
                TERMINATED);
    }

    @Override
    public Set<Configuration> peek(State state) {
        if (!(lhs instanceof Value)) {
            return lhs.peek(state);
        }
        if (!(rhs instanceof Value)) {
            return rhs.peek(state);
        }
        return Collections.singleton(new ExpressionConfiguration(this, state, INTERMEDIATE));
    }

    @Override
    public IExpression copy() {
        return new BinOp<>(operator, lhs.copy(), rhs.copy(), operandClass, operatorFunction);
    }

    @Override
    public <V> V accept(INodeVisitor<V> visitor) {
        return visitor.visit(this);
    }

    public enum Arithmetic {

        ADD((lhs, rhs) -> BinOp.arithmetic("+", lhs, rhs, BigInteger::add)),
        SUB((lhs, rhs) -> BinOp.arithmetic("-", lhs, rhs, BigInteger::subtract)),
        MUL((lhs, rhs) -> BinOp.arithmetic("*", lhs, rhs, BigInteger::multiply)),
        DIV((lhs, rhs) -> BinOp.arithmetic("/", lhs, rhs, BigInteger::divide)),
        REM((lhs, rhs) -> BinOp.arithmetic("%", lhs, rhs, BigInteger::mod));

        private final BiFunction<IExpression, IExpression, BinOp<BigInteger, BigInteger>> operationProvider;

        Arithmetic(BiFunction<IExpression, IExpression, BinOp<BigInteger, BigInteger>> operationProvider) {
            this.operationProvider = operationProvider;
        }

        public BinOp<BigInteger, BigInteger> of(IExpression e1, IExpression e2) {
            return operationProvider.apply(e1, e2);
        }
    }

    public enum Logical {

        AND((lhs, rhs) -> BinOp.logical("&&", lhs, rhs, (a, b) -> a && b)),
        OR((lhs, rhs) -> BinOp.logical("||" , lhs, rhs, (a, b) -> a || b)),
        XOR((lhs, rhs) -> BinOp.logical("^" , lhs, rhs, (a, b) -> a ^ b));

        private final BiFunction<IExpression, IExpression, BinOp<Boolean, Boolean>> operationProvider;

        Logical(BiFunction<IExpression, IExpression, BinOp<Boolean, Boolean>> operationProvider) {
            this.operationProvider = operationProvider;
        }

        public BinOp<Boolean, Boolean> of(IExpression e1, IExpression e2) {
            return operationProvider.apply(e1, e2);
        }
    }

    public enum Relational {

        EQ((lhs, rhs) -> BinOp.relational2("=" , lhs, rhs, (a, b) -> a.compareTo(b) == 0)),
        NE((lhs, rhs) -> BinOp.relational2("!=", lhs, rhs, (a, b) -> a.compareTo(b) != 0)),
        LT((lhs, rhs) -> BinOp.relational2("<" , lhs, rhs, (a, b) -> a.compareTo(b)  < 0)),
        LE((lhs, rhs) -> BinOp.relational2("<=", lhs, rhs, (a, b) -> a.compareTo(b) <= 0)),
        GT((lhs, rhs) -> BinOp.relational2(">" , lhs, rhs, (a, b) -> a.compareTo(b)  > 0)),
        GE((lhs, rhs) -> BinOp.relational2(">=", lhs, rhs, (a, b) -> a.compareTo(b) >= 0));

        private final BiFunction<IExpression, IExpression, BinOp<BigInteger, Boolean>> operationProvider;

        Relational(BiFunction<IExpression, IExpression, BinOp<BigInteger, Boolean>> operationProvider) {
            this.operationProvider = operationProvider;
        }

        public BinOp<BigInteger, Boolean> of(IExpression e1, IExpression e2) {
            return operationProvider.apply(e1, e2);
        }
    }

}
