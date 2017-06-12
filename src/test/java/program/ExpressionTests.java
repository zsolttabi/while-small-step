package program;

import org.junit.Assert;
import org.junit.Test;
import program.expressions.IExpression;
import program.expressions.Identifier;
import program.expressions.Value;

import java.math.BigInteger;

import static program.Configuration.ConfigType.STUCK;
import static program.Configuration.ConfigType.TERMINATED;
import static program.expressions.BinOp.Arithmetic.ADD;
import static program.expressions.BinOp.Logical.AND;
import static program.expressions.UnOp.Arithmetic.NEG;
import static program.expressions.UnOp.Logical.NOT;

public class ExpressionTests {

    @Test
    public void testInitializedIdentifierYieldsValue() {

        Identifier underTest = new Identifier("x");
        Value<BigInteger> value = new Value<>(new BigInteger("1"));
        State state = new State();
        state.set(underTest, value);

        Configuration result = underTest.step(state);

        Assert.assertEquals(new Configuration(value, state, TERMINATED), result);
    }

    @Test
    public void testUninitializedIdentifierYieldsStuckIdentifier() {

        Identifier underTest = new Identifier("x");
        State state = new State();

        Configuration result = underTest.step(state);

        Assert.assertEquals(new Configuration(underTest, new State(), STUCK), result);
    }

    @Test
    public void testBinOpYieldsResult() {

        IExpression lhs = new Value<>(new BigInteger("1"));
        IExpression rhs = new Value<>(new BigInteger("1"));
        IExpression underTest = ADD.of(lhs, rhs);

        Configuration result = underTest.step(new State());

        Assert.assertEquals(new Configuration(new Value<>(new BigInteger("2")), new State(), TERMINATED), result);
    }

    @Test
    public void testStuckOperandYieldsStuckBinOp() {

        IExpression lhs = new Identifier("x");
        IExpression rhs = new Value<>(new BigInteger("1"));
        IExpression underTest = ADD.of(lhs, rhs);

        Configuration result = underTest.step(new State());

        Assert.assertEquals(new Configuration(ADD.of(lhs, rhs), new State(), STUCK), result);
    }

    @Test
    public void testMismatchedTypesYieldsStuckBinOp() {

        IExpression lhs = new Value<>(new BigInteger("1"));
        IExpression rhs = new Value<>(true);
        IExpression underTest = ADD.of(lhs, rhs);

        Configuration result = underTest.step(new State());

        Assert.assertEquals(new Configuration(ADD.of(lhs, rhs), new State(), STUCK), result);
    }

    @Test
    public void testWrongOperandTypesYieldsStuckBinOp() {

        IExpression lhs = new Value<>(new BigInteger("1"));
        IExpression rhs = new Value<>(new BigInteger("1"));
        IExpression underTest = AND.of(lhs, rhs);

        Configuration result = underTest.step(new State());

        Assert.assertEquals(new Configuration(AND.of(lhs, rhs), new State(), STUCK), result);
    }

    @Test
    public void testUnOpYieldsResult() {

        IExpression operand = new Value<>(new BigInteger("1"));
        IExpression underTest = NEG.of(operand);

        Configuration result = underTest.step(new State());

        Assert.assertEquals(new Configuration(new Value<>(new BigInteger("-1")), new State(), TERMINATED), result);
    }

    @Test
    public void testStuckOperandYieldsStuckUnOp() {

        IExpression operand = new Identifier("x");
        IExpression underTest = NEG.of(operand);

        Configuration result = underTest.step(new State());

        Assert.assertEquals(new Configuration(NEG.of((IExpression) operand.step(new State()).getElement()),
                new State(),
                STUCK), result);
    }

    @Test
    public void testWrongOperandTypeYieldsStuckUnOp() {

        IExpression operand = new Value<>(new BigInteger("1"));
        IExpression underTest = NOT.of(operand);

        Configuration result = underTest.step(new State());

        Assert.assertEquals(new Configuration(NOT.of(operand), new State(), STUCK), result);
    }

}
