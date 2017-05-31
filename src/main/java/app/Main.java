package app;

import javafx.application.Application;
import javafx.stage.Stage;
import view.ASTScene;


public class Main extends Application {

    private static final String EXAMPLE =
            "x := 0;\n" +
            "while x < 5 do\n " +
                "  x :=  x + 1\n" +
            "od";
    public static final int START_WIDTH = 1000;
    public static final int START_HEIGHT = 700;
    public static final int MIN_WIDTH = 600;
    public static final int MIN_HEIGHT = 400;

    @Override
    public void start(Stage aStage) throws Exception {

        aStage.setWidth(START_WIDTH);
        aStage.setHeight(START_HEIGHT);

        aStage.setMinWidth(MIN_WIDTH);
        aStage.setMinHeight(MIN_HEIGHT);

        aStage.setTitle(getClass().getSimpleName());
        aStage.setScene(ASTScene.of(EXAMPLE));
        aStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

