package program;

import org.junit.Assert;
import org.junit.Test;
import program.expressions.IExpression;
import program.expressions.Identifier;
import program.expressions.Value;
import program.statements.*;

import static program.Configuration.ConfigType.*;
import static program.expressions.BinOp.Arithmetic.ADD;
import static program.expressions.BinOp.Logical.AND;

public class StatementTests {

    @Test
    public void testSkipSkips() {

        IStatement underTest = new Skip();
        State state = new State();

        Configuration result = underTest.step(state);

        Assert.assertEquals(new StatementConfiguration(new Skip(), new State(), TERMINATED), result);
    }

    @Test
    public void testAssignmentAssignsValueToNewVariable() {

        Identifier identifier = new Identifier("x");
        Value<?> value = new Value<>(5);

        State state = new State();
        State expectedState = new State();
        expectedState.set(identifier, value);
        IStatement underTest = new Assignment(identifier, value);

        Configuration result = underTest.step(state);

        Assert.assertEquals(new StatementConfiguration(underTest, expectedState, TERMINATED), result);
    }


    @Test
    public void testAssignmentAssignsNewValueToExistingVariable() {

        Identifier identifier = new Identifier("x");
        Value<?> value = new Value<>(5);

        State state = new State();
        state.set(identifier, new Value<>(1));
        State expectedState = new State();
        expectedState.set(identifier, value);

        IStatement underTest = new Assignment(identifier, value);

        Configuration result = underTest.step(state);

        Assert.assertEquals(new StatementConfiguration(underTest, expectedState, TERMINATED), result);
    }

    @Test
    public void testAssignNonExistingVariableYieldsStuckAssignment() {

        IExpression x = new Identifier("x");
        IExpression y = new Identifier("y");
        IStatement underTest = new Assignment(x, y);

        Configuration result = underTest.step(new State());

        Assert.assertEquals(new StatementConfiguration(new Assignment(x, y.step(new State()).getNode()),
                new State(),
                STUCK), result);
    }

    @Test
    public void testAssignStuckValueYieldsStuckAssignment() {

        IExpression x = new Identifier("x");
        IExpression stuckVal = ADD.of(new Value<>(true), new Value<>(1)).step(new State()).getNode();
        IStatement underTest = new Assignment(x, stuckVal);

        Configuration result = underTest.step(new State());

        Assert.assertEquals(new StatementConfiguration(new Assignment(x, stuckVal),new State(), STUCK), result);
    }


    @Test
    public void testSequence() {

        IStatement s1 = new Skip();
        IStatement s2 = new If(null, null, null);
        State state = new State();
        IStatement underTest = new Sequence(s1, s2);

        Configuration result = underTest.step(state);

        Assert.assertEquals(new StatementConfiguration(s2, new State(), INTERMEDIATE), result);
    }

    @Test
    public void testStuckStatementYieldsStuckSequence() {

        IStatement stuckS1 = new Assignment(new Identifier("x"), new Identifier("y")).step(new State()).getNode();
        IStatement s2 = new Skip();
        State state = new State();
        IStatement underTest = new Sequence(stuckS1, s2);

        Configuration result = underTest.step(state);

        Assert.assertEquals(new StatementConfiguration(new Sequence(stuckS1, s2), new State(), STUCK), result);
    }


    @Test
    public void testIfTrueConditionYieldsTrueBranch() {

        IStatement s1 = new Assignment(new Identifier("x"), new Value<>(1));
        IStatement s2 = new Assignment(new Identifier("y"), new Value<>(2));
        State state = new State();
        IStatement underTest = new If(new Value<>(true), s1, s2);

        Configuration result = underTest.step(state);

        Assert.assertEquals(new StatementConfiguration(s1, new State(), INTERMEDIATE), result);
    }

    @Test
    public void testIfFalseConditionYieldsFalseBranch() {

        IStatement s1 = new Assignment(new Identifier("x"), new Value<>(1));
        IStatement s2 = new Assignment(new Identifier("y"), new Value<>(2));
        State state = new State();
        IStatement underTest = new If(new Value<>(false), s1, s2);

        Configuration result = underTest.step(state);

        Assert.assertEquals(new StatementConfiguration(s2, new State(), INTERMEDIATE), result);
    }

    @Test
    public void testStuckExprYieldsStuckIf() {

        IExpression stuckExpr = AND.of(new Value<>(1), new Value<>(true)).step(new State()).getNode();
        State state = new State();
        IStatement s1 = new Assignment(new Identifier("x"), new Value<>(1));
        IStatement s2 = new Assignment(new Identifier("y"), new Value<>(2));
        IStatement underTest = new If(stuckExpr, s1, s2);

        Configuration result = underTest.step(state);

        Assert.assertEquals(new StatementConfiguration(new If(stuckExpr, s1, s2), new State(), STUCK), result);
    }

    @Test
    public void testWrongTypeExprYieldsStuckIf() {

        IExpression intExpr = new Value<>(4);
        State state = new State();
        IStatement s1 = new Assignment(new Identifier("x"), new Value<>(1));
        IStatement s2 = new Assignment(new Identifier("y"), new Value<>(2));
        IStatement underTest = new If(intExpr, s1, s2);

        Configuration result = underTest.step(state);

        Assert.assertEquals(new StatementConfiguration(new If(intExpr, s1, s2), new State(), STUCK), result);
    }


    @Test
    public void testWhileTurnsIntoIf() {

        IExpression cond = new Value<>(true);
        IStatement s = new Assignment(new Identifier("x"), new Value<>(1));
        IStatement underTest = new While(cond, s);

        Configuration result = underTest.step(new State());

        Assert.assertEquals(result, new StatementConfiguration(new If(cond, new Sequence(s, underTest), new Skip()), new State(), INTERMEDIATE));
    }

}