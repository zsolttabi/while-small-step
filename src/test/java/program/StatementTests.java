package program;

import org.junit.Test;

public class StatementTests {

    @Test
    public void testSkipSkips() {

//        Statement underTest = new Skip();
//        State state = new State();
//
//        StmConfig result = underTest.step(state);
//
//        Assert.assertNull(result.getStatement());
//        Assert.assertEquals(state, result.getState());

    }

    @Test
    public void testAssignmentAssignsValueToNewVariable() {

//        Identifier identifier = new Identifier("x");
//        Value<?> value = new IntValue(5);
//
//        State state = new State();
//        State expectedState = new State();
//        expectedState.set(identifier, value);
//        Statement underTest = new Assignment(identifier, value);
//
//        StmConfig result = underTest.step(state);
//
//        Assert.assertNull(result.getStatement());
//        Assert.assertEquals(expectedState, result.getState());

    }


    @Test
    public void testAssignmentAssignsNewValueToExistingVariable() {

//        Identifier identifier = new Identifier("x");
//        Value<?> value = new IntValue(5);
//
//        State state = new State();
//        state.set(identifier, new IntValue(1));
//        State expectedState = new State();
//        expectedState.set(identifier, value);
//
//        Statement underTest = new Assignment(identifier, value);
//
//        StmConfig result = underTest.step(state);
//
//        Assert.assertNull(result.getStatement());
//        Assert.assertEquals(expectedState, result.getState());

    }

    @Test
    public void testAssignNonExistingVariableYieldsBadAssignment() {

//        Expression x = new Identifier("x");
//        Expression y = new Identifier("y");
//        Statement underTest = new Assignment(x, y);
//
//        StmConfig result = underTest.step(new State());
//
//        Assert.assertEquals(new Assignment(x, y.step(new State()).getNode()), result.getStatement());
//        Assert.assertEquals(new State(), result.getState());
//
//        result = result.getStatement().step(result.getState());
//
//        Assert.assertEquals(new StuckAssignment(x, y.step(new State()).getNode()), result.getStatement());
//        Assert.assertEquals(new State(), result.getState());

    }

    @Test
    public void testAssignToBadIdentifierYieldsBadAssignment() {
//
//        Expression x = new StuckIdentifier("x");
//        Expression y = new Identifier("y");
//        Statement underTest = new Assignment(x, y);
//
//        StmConfig result = underTest.step(new State());
//
//        Assert.assertEquals(new StuckAssignment(x, y), result.getStatement());
//        Assert.assertEquals(new State(), result.getState());

    }


    @Test
    public void testAssignBadValueYieldsBadAssignment() {

//        Expression x = new Identifier("x");
//        Expression badVal = new StuckUnOp<Integer, Integer>("", null);
//        Statement underTest = new Assignment(x, badVal);
//
//        StmConfig result = underTest.step(new State());
//
//        Assert.assertEquals(new StuckAssignment(x, badVal), result.getStatement());
//        Assert.assertEquals(new State(), result.getState());

    }


    @Test
    public void testSequence() {

//        Statement s1 = new Skip();
//        Statement s2 = new If(null, null, null);
//        State state = new State();
//        Statement underTest = new Sequence(s1, s2);
//
//        StmConfig result = underTest.step(state);
//
//        Assert.assertEquals(s2, result.getStatement());
//        Assert.assertEquals(new State(), result.getState());

    }

    @Test
    public void testBadSequence() {
//
//        Statement s1 = new StuckAssignment(new Identifier("x"), new Identifier("y"));
//        Statement s2 = new Skip();
//        State state = new State();
//        Statement underTest = new Sequence(s1, s2);
//
//        StmConfig result = underTest.step(state);
//
//        Assert.assertEquals(StuckSequence.class, result.getStatement().getClass());
//        Assert.assertEquals(new State(), result.getState());

    }


    @Test
    public void testIfTrueBranch() {

//        Statement s1 = new If(null, null, null);
//        Statement s2 = new Sequence(null, null);
//        State state = new State();
//        Statement underTest = new If(new BoolValue(true), s1, s2);
//
//        StmConfig result = underTest.step(state);
//
//        Assert.assertEquals(s1, result.getStatement());
//        Assert.assertEquals(new State(), result.getState());

    }

    @Test
    public void testIfFalseBranch() {

//        Statement s1 = new If(null, null, null);
//        Statement s2 = new Sequence(null, null);
//        Statement underTest = new If(new BoolValue(false), s1, s2);
//
//        StmConfig result = underTest.step(new State());
//
//        Assert.assertEquals(s2, result.getStatement());
//        Assert.assertEquals(new State(), result.getState());

    }

    @Test
    public void testBadExprYieldsBadIf() {

//        Expression expr = new StuckUnOp<Integer, Integer>("", null);
//        State state = new State();
//        Statement underTest = new If(expr, null, null);
//
//        StmConfig result = underTest.step(state);
//
//        Assert.assertEquals(result.getStatement(), new StuckIf(expr, null, null));
//        Assert.assertEquals(result.getState(), new State());

    }

    @Test
    public void testWrongTypeExprYieldsBadIf() {

//        Expression expr = new IntValue(4);
//        State state = new State();
//        Statement underTest = new If(expr, null, null);
//
//        StmConfig result = underTest.step(state);
//
//        Assert.assertEquals(result.getStatement(), new StuckIf(expr, null, null));
//        Assert.assertEquals(result.getState(), new State());
    }


    @Test
    public void testWhileTurnsIntoIf() {

//        Expression cond = new BoolValue(true);
//        Statement s = new Sequence(new Skip(), new Skip());
//        Statement underTest = new While(cond, s);
//
//        StmConfig result = underTest.step(new State());
//
//        Assert.assertEquals(result.getStatement(), new If(cond, new Sequence(s, underTest), new Skip()));
//        Assert.assertEquals(result.getState(), new State());

    }

}