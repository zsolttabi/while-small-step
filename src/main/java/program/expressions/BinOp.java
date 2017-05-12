package program.expressions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.State;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;

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

    public static BinOp<Integer, Integer> arithmetic(String operator, IExpression lhs, IExpression rhs, BiFunction<Integer, Integer, Integer> operatorFunction) {
        return BinOp.of(operator, lhs, rhs, Integer.class, operatorFunction);
    }

    public static BinOp<Boolean, Boolean> logical(String operator, IExpression lhs, IExpression rhs, BiFunction<Boolean, Boolean, Boolean> operatorFunction) {
        return BinOp.of(operator, lhs, rhs, Boolean.class, operatorFunction);
    }

    public static BinOp<Integer, Boolean> relational(String operator, IExpression lhs, IExpression rhs, BiFunction<Integer, Integer, Boolean> operatorFunction) {
        return BinOp.of(operator, lhs, rhs, Integer.class, operatorFunction);
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
    public ExpressionConfiguration next(State state) {
        if (!(lhs instanceof Value)) {
            return lhs.next(state);
        }
        if (!(rhs instanceof Value)) {
            return rhs.next(state);
        }
        return new ExpressionConfiguration(this, state, INTERMEDIATE);
    }

    @Override
    public Tree.Node<ASTNode> accept(INodeVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

    public enum Arithmetic {

        ADD((lhs, rhs) -> BinOp.arithmetic("+", lhs, rhs, (a, b) -> a + b)),
        SUB((lhs, rhs) -> BinOp.arithmetic("-", lhs, rhs, (a, b) -> a - b)),
        MUL((lhs, rhs) -> BinOp.arithmetic("*", lhs, rhs, (a, b) -> a * b)),
        DIV((lhs, rhs) -> BinOp.arithmetic("/", lhs, rhs, (a, b) -> a / b)),
        REM((lhs, rhs) -> BinOp.arithmetic("%", lhs, rhs, (a, b) -> a % b));

        private final BiFunction<IExpression, IExpression, BinOp<Integer, Integer>> operationProvider;

        Arithmetic(BiFunction<IExpression, IExpression, BinOp<Integer, Integer>> operationProvider) {
            this.operationProvider = operationProvider;
        }

        public BinOp<Integer, Integer> of(IExpression e1, IExpression e2) {
            return operationProvider.apply(e1, e2);
        }
    }

    public enum Logical {

        AND((lhs, rhs) -> BinOp.logical("and", lhs, rhs, (a, b) -> a && b)),
        OR((lhs, rhs) -> BinOp.logical("or", lhs, rhs, (a, b) -> a || b)),
        XOR((lhs, rhs) -> BinOp.logical("xor", lhs, rhs, (a, b) -> a ^ b));

        private final BiFunction<IExpression, IExpression, BinOp<Boolean, Boolean>> operationProvider;

        Logical(BiFunction<IExpression, IExpression, BinOp<Boolean, Boolean>> operationProvider) {
            this.operationProvider = operationProvider;
        }

        public BinOp<Boolean, Boolean> of(IExpression e1, IExpression e2) {
            return operationProvider.apply(e1, e2);
        }
    }

    public enum Relational {

        EQ((lhs, rhs) -> BinOp.relational("=", lhs, rhs, Integer::equals)),
        LT((lhs, rhs) -> BinOp.relational("<", lhs, rhs, (a, b) -> a < b)),
        LE((lhs, rhs) -> BinOp.relational("<=", lhs, rhs, (a, b) -> a <= b)),
        GT((lhs, rhs) -> BinOp.relational(">", lhs, rhs, (a, b) -> a > b)),
        GE((lhs, rhs) -> BinOp.relational(">=", lhs, rhs, (a, b) -> a >= b));

        private final BiFunction<IExpression, IExpression, BinOp<Integer, Boolean>> operationProvider;

        Relational(BiFunction<IExpression, IExpression, BinOp<Integer, Boolean>> operationProvider) {
            this.operationProvider = operationProvider;
        }

        public BinOp<Integer, Boolean> of(IExpression e1, IExpression e2) {
            return operationProvider.apply(e1, e2);
        }
    }

}
