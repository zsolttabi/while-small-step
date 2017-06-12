package program.expressions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.IProgramElement;
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
public class BinOp<T, R> implements IProgramElement {

    @Getter
    private final String operator;
    @Getter
    private final IProgramElement lhs;
    @Getter
    private final IProgramElement rhs;
    private final Class<T> operandClass;
    private final BiFunction<T, T, R> operatorFunction;

    public static <T, R> BinOp<T, R> of(String operator, IProgramElement lhs, IProgramElement rhs, Class<T> operandClass, BiFunction<T, T, R> operatorFunction) {
        return new BinOp<>(operator, lhs, rhs, operandClass, operatorFunction);
    }

    public static BinOp<BigInteger, BigInteger> arithmetic(String operator, IProgramElement lhs, IProgramElement rhs, BiFunction<BigInteger, BigInteger, BigInteger> operatorFunction) {
        return BinOp.of(operator, lhs, rhs, BigInteger.class, operatorFunction);
    }

    public static BinOp<Boolean, Boolean> logical(String operator, IProgramElement lhs, IProgramElement rhs, BiFunction<Boolean, Boolean, Boolean> operatorFunction) {
        return BinOp.of(operator, lhs, rhs, Boolean.class, operatorFunction);
    }

    public static BinOp<BigInteger, Boolean> relational(String operator, IProgramElement lhs, IProgramElement rhs, BiFunction<BigInteger, BigInteger, Boolean> operatorFunction) {
        return BinOp.of(operator, lhs, rhs, BigInteger.class, operatorFunction);
    }

    public static BinOp<BigInteger, Boolean> relational2(String operator, IProgramElement lhs, IProgramElement rhs, BiFunction<BigInteger, BigInteger, Boolean> operatorFunction) {
        return BinOp.of(operator, lhs, rhs, BigInteger.class, operatorFunction);
    }

    @Override
    public Configuration step(State state) {

        if (!(lhs instanceof Value)) {
            Configuration lhsConf = lhs.step(state);
            return new Configuration(BinOp.of(operator,
                    lhsConf.getElement(),
                    rhs,
                    operandClass,
                    operatorFunction), lhsConf.getState(), lhsConf.getConfigType() == STUCK ? STUCK : INTERMEDIATE);
        }

        if (!(rhs instanceof Value)) {
            Configuration rhsConf = rhs.step(state);
            return new Configuration(BinOp.of(operator,
                    lhs,
                    rhsConf.getElement(),
                    operandClass,
                    operatorFunction), rhsConf.getState(), rhsConf.getConfigType() == STUCK ? STUCK : INTERMEDIATE);
        }

        Object lhsValue = ((Value) lhs).getValue();
        Object rhsValue = ((Value) rhs).getValue();

        if (!lhsValue.getClass().equals(operandClass) || !rhsValue.getClass().equals(operandClass)) {
            return new Configuration(new BinOp<>(operator, lhs, rhs, operandClass, operatorFunction),
                    state,
                    STUCK);
        }


        return new Configuration(new Value<>(operatorFunction.apply(operandClass.cast(lhsValue),
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
        return Collections.singleton(new Configuration(this, state, INTERMEDIATE));
    }

    @Override
    public IProgramElement copy() {
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

        private final BiFunction<IProgramElement, IProgramElement, BinOp<BigInteger, BigInteger>> operationProvider;

        Arithmetic(BiFunction<IProgramElement, IProgramElement, BinOp<BigInteger, BigInteger>> operationProvider) {
            this.operationProvider = operationProvider;
        }

        public BinOp<BigInteger, BigInteger> of(IProgramElement e1, IProgramElement e2) {
            return operationProvider.apply(e1, e2);
        }
    }

    public enum Logical {

        AND((lhs, rhs) -> BinOp.logical("&&", lhs, rhs, (a, b) -> a && b)),
        OR((lhs, rhs) -> BinOp.logical("||", lhs, rhs, (a, b) -> a || b)),
        XOR((lhs, rhs) -> BinOp.logical("^", lhs, rhs, (a, b) -> a ^ b));

        private final BiFunction<IProgramElement, IProgramElement, BinOp<Boolean, Boolean>> operationProvider;

        Logical(BiFunction<IProgramElement, IProgramElement, BinOp<Boolean, Boolean>> operationProvider) {
            this.operationProvider = operationProvider;
        }

        public BinOp<Boolean, Boolean> of(IProgramElement e1, IProgramElement e2) {
            return operationProvider.apply(e1, e2);
        }
    }

    public enum Relational {

        EQ((lhs, rhs) -> BinOp.relational2("=", lhs, rhs, (a, b) -> a.compareTo(b) == 0)),
        NE((lhs, rhs) -> BinOp.relational2("!=", lhs, rhs, (a, b) -> a.compareTo(b) != 0)),
        LT((lhs, rhs) -> BinOp.relational2("<", lhs, rhs, (a, b) -> a.compareTo(b) < 0)),
        LE((lhs, rhs) -> BinOp.relational2("<=", lhs, rhs, (a, b) -> a.compareTo(b) <= 0)),
        GT((lhs, rhs) -> BinOp.relational2(">", lhs, rhs, (a, b) -> a.compareTo(b) > 0)),
        GE((lhs, rhs) -> BinOp.relational2(">=", lhs, rhs, (a, b) -> a.compareTo(b) >= 0));

        private final BiFunction<IProgramElement, IProgramElement, BinOp<BigInteger, Boolean>> operationProvider;

        Relational(BiFunction<IProgramElement, IProgramElement, BinOp<BigInteger, Boolean>> operationProvider) {
            this.operationProvider = operationProvider;
        }

        public BinOp<BigInteger, Boolean> of(IProgramElement e1, IProgramElement e2) {
            return operationProvider.apply(e1, e2);
        }
    }

}
