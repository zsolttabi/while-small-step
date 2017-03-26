package sample;

import com.google.code.javafxgraph.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
//
    @Override
    public void start(Stage aStage) throws Exception {

        aStage.setMinWidth(800);
        aStage.setMinHeight(600);
        aStage.setTitle(getClass().getSimpleName());

        FXGraphBuilder theBuilder = FXGraphBuilder.create();
        FXGraph theGraph = theBuilder.build();

        List<FXNode> theNodes = new ArrayList<FXNode>();
        int centerX = 400;
        int centerY = 300;
        int numNodes = 20;
        int radius = 220;
        for (int i = 0; i < numNodes; i++) {
            Button button1 = new Button();
            button1.setText("Node " + i);

            double positionX = centerX + Math.cos(Math.toRadians(360 / numNodes * i)) * radius;
            double positionY = centerY + Math.sin(Math.toRadians(360 / numNodes * i)) * radius;

            FXNodeBuilder theNodeBuilder = new FXNodeBuilder(theGraph);
            theNodes.add(theNodeBuilder.node(button1).x(positionX).y(positionY).build());
        }

        for (int i = 0; i < theNodes.size() - 1; i++) {

            FXEdgeBuilder theEdgeBuilder = new FXEdgeBuilder(theGraph);
            theEdgeBuilder.source(theNodes.get(i)).destination(theNodes.get(i+1)).build();
        }

        aStage.setScene(new Scene(theGraph));

        aStage.show();
    }

//    @Override
//    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();
//
//
//    }


    public static void main(String[] args) {
        launch(args);
    }
}
