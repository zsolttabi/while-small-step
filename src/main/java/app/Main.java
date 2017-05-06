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

    @Override
    public void start(Stage aStage) throws Exception {
        aStage.setMinWidth(1200);
        aStage.setMinHeight(700);
        aStage.setTitle(getClass().getSimpleName());
        aStage.setScene(ASTScene.of(EXAMPLE));
        aStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

