package program;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import syntax.while_parser.WhileParser;

@RunWith(DataProviderRunner.class)
public class ProgramTest {

    @DataProvider
    public static Object[][] goodCodeProvider() {
        return new Object[][]{
                {
                    "SKIP"
                },

                {
                    "SKIP; " +
                    "SKIP"
                },

                {
                    "x := 1"
                },

                {
                    "b := tt"
                },

                {
                    "if tt then " +
                        "SKIP " +
                    "else " +
                        "SKIP " +
                    "fi"
                },

                {
                    "b := tt; " +
                    "if b then " +
                        "x := 1 " +
                    "else " +
                        "SKIP " +
                    "fi; " +
                    "z := x"
                },

                {
                    "x := 1;" +
                    "while x < 10 do " +
                        "x := x + 1 " +
                    "od"
                },
        };
    }

    @DataProvider
    public static Object[][] badCodeProvider() {
        return new Object[][]{
                {
                    "x := 1; " +
                    "x := !x "
                },

                {
                    "x := tt; " +
                    "x := -x "
                },


                {
                    "x := 1; " +
                    "y := ff; " +
                    "z := x + y"
                },


                {
                    "x := 1; " +
                    "x := tt; " +
                    "x := 2"
                },

                {
                    "x := y"
                },

                {
                    "x := 1; " +
                    "if x then " +
                        "SKIP " +
                    "else " +
                        "SKIP " +
                    "fi"
                },

                {
                    "if z then " +
                        "SKIP " +
                    "else " +
                        "SKIP " +
                    "fi"
                },

                {
                    "x := tt; " +
                    "if x then " +
                        "x := 1 " +
                    "else " +
                        "SKIP " +
                    "fi"
                },

                {
                    "x := ff; " +
                    "if x then " +
                        "SKIP " +
                    "else " +
                        "x := 1 " +
                    "fi"
                },

                {
                    "while 2 do " +
                        "SKIP" +
                    " od"
                },

                {
                    "while z do " +
                        "SKIP " +
                    "od"
                },

                {
                    "x := tt; " +
                    "while x do " +
                        "x := x + 1 " +
                    "od"
                },
        };
    }

    @Test
    @UseDataProvider("goodCodeProvider")
    public void testGoodAST(String code) {

//        Program program = WhileParser.parse(code);
//        Assert.assertTrue(program.reduce() instanceof ReducedAST);

    }

    @Test
    @UseDataProvider("badCodeProvider")
    public void testBadAST(String code) {

//        Program program = WhileParser.parse(code);
//        Assert.assertTrue(program.reduce() instanceof BadAST);

    }

}
