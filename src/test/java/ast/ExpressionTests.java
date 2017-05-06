package ast;

import ast.expression.BadIdentifier;
import ast.expression.Identifier;
import ast.expression.interfaces.Expression;
import ast.expression.operations.BinOp;
import ast.expression.values.IntValue;
import org.junit.Assert;
import org.junit.Test;

public class ExpressionTests {

    @Test
    public void testInitializedIdentifierYieldsValue() {

        Identifier underTest = new Identifier("x");
        State state = new State();
        IntValue value = new IntValue(1);
        state.set(underTest, value);

        Expression result = underTest.step(state);

        Assert.assertEquals(result, value);

    }

    @Test
    public void testUninitializedIdentifierYieldsBadIdentifier() {

        Identifier underTest = new Identifier("x");
        State state = new State();
        Expression result = underTest.step(state);

        Assert.assertEquals(result, new BadIdentifier("x"));

    }

    @Test
    public void testBinOpYieldsResult() {

        Expression lhs = new IntValue(1);
        Expression rhs = new IntValue(1);
        Expression underTest = BinOp.arithOp("+", lhs, rhs, Integer::sum);

        Expression result = underTest.step(new State());

        Assert.assertEquals(result, new IntValue(2));

    }

    @Test
    public void testBadRhsYieldsBadBinOp() {

    }

    @Test
    public void testBadLhsYieldsBadBinOp() {

    }

    @Test
    public void testUnOpYieldsResult() {

    }

    @Test
    public void testBadOperandYieldsBadUnOp() {

    }

    @Test
    public void testValueYieldsValue() {

    }

}
