package program;

import org.junit.Assert;
import org.junit.Test;
import program.expressions.*;

public class ExpressionTests {

    @Test
    public void testInitializedIdentifierYieldsValue() {

        Identifier underTest = new Identifier("x");
        IntValue value = new IntValue(1);
        State state = new State();
        state.set(underTest, value);

        Configuration result = underTest.step(state);

        Assert.assertEquals(result.getNode(), value);
        Assert.assertEquals(result.getState(), state);
        // assert whole config

    }

    @Test
    public void testUninitializedIdentifierYieldsBadIdentifier() {

        Identifier underTest = new Identifier("x");
        State state = new State();
        Configuration result = underTest.step(state);

        Assert.assertEquals(result.getNode(), underTest);
        Assert.assertEquals(result.getState(), new State());

    }

    @Test
    public void testBinOpYieldsResult() {

        Expression lhs = new IntValue(1);
        Expression rhs = new IntValue(1);
        Expression underTest = BinOp.add(lhs, rhs);

        Configuration result = underTest.step(new State());

        Assert.assertEquals(result.getNode(), new IntValue(2));
        Assert.assertEquals(result.getState(), new State());

    }

    @Test
    public void testBadOperandYieldsBadBinOp() {

//        Expression lhs = new IntValue(1);
//        Expression rhs = new Identifier("x");
//        Expression underTest = BinOp.add(lhs, rhs);
//
//        Configuration result = underTest.step(new State());
//
//        Assert.assertEquals(result.getNode(),  BinOp.add(lhs, rhs.step(new State()).getNode()));
//
//        result = result.step();
//
//        Assert.assertEquals(result.getNode(), new StuckBinOp<>("+", lhs, rhs.step(new State()).getNode()));
//        Assert.assertEquals(result.getState(), new State());

    }

    @Test
    public void testMismatchedTypesYieldsBadBinOp() {

//        Expression lhs = new BoolValue(true);
//        Expression rhs = new IntValue(1);
//        Expression underTest = BinOp.add(lhs, rhs);
//
//        Configuration result = underTest.step(new State());
//
//        Assert.assertEquals(result.getNode(), new StuckBinOp<>("+", lhs, rhs));
//        Assert.assertEquals(result.getState(), new State());

    }

    @Test
    public void testBadOperandTypesYieldsBadBinOp() {

//        Expression lhs = new IntValue(1);
//        Expression rhs = new IntValue(1);
//        Expression underTest = BinOp.and(lhs, rhs);
//
//        ExprConfig result = underTest.step(new State());
//
//        Assert.assertEquals(result.getExpression(), new StuckBinOp<>("and", lhs, rhs));
//        Assert.assertEquals(result.getState(), new State());

    }

    @Test
    public void testUnOpYieldsResult() {

        Expression operand = new IntValue(1);
        Expression underTest = UnOp.neg(operand);

        Configuration result = underTest.step(new State());

        Assert.assertEquals(result.getNode(), new IntValue(-1));
        Assert.assertEquals(result.getState(), new State());

    }

    @Test
    public void testBadOperandYieldsBadUnOp() {

        Expression operand = new Identifier("x");
        Expression underTest = UnOp.neg(operand);

        Configuration result = underTest.step(new State());

        Assert.assertEquals(result.getNode(), UnOp.neg(operand.step(new State()).getNode()));

        result = result.step();

        Assert.assertEquals(result.getNode(), UnOp.neg(operand.step(new State()).getNode()));
        Assert.assertEquals(result.getState(), new State());
        // TODO: assert stuck

    }

    @Test
    public void testBadOperandTypeYieldsBadUnOp() {

        Expression operand = new IntValue(1);
        Expression underTest = UnOp.not(operand);

        Configuration result = underTest.step(new State());

        Assert.assertEquals(result.getNode(), UnOp.not(operand));
        Assert.assertEquals(result.getState(), new State());

    }

}
