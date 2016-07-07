package com.strona2;

import com.taskadapter.redmineapi.RedmineException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;


//https://dzone.com/articles/build-your-own-java-stopwatch

public class ProjectFX extends Application {

    RedmineData redmineData;
    Stage window;
    Label labelSearchbyId;
    Button buttonStart, buttonPause, buttonEnd, buttonSearchById, buttonSearchAll;
    TextField textFieldSearchById;
    Text textMessage, textSearchById;

    
    public static void main(String[] args) {
        launch(args);

    }

    public ProjectFX() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, RedmineException {
        this.redmineData = new RedmineData();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String windowTitle = "Redmine tray";
        window = primaryStage;

        labelSearchbyId = new Label("Search by Id:");
        buttonStart = new Button("start");
//        buttonStart.setMinSize(40, 30);
        buttonPause = new Button("pause");
//        buttonPause.setMinSize(40, 30);
        buttonEnd = new Button("end");
//        buttonEnd.setMinSize(40, 30);
        buttonSearchById = new Button("search by Id");
        buttonSearchAll = new Button("search all");

        buttonStart.setOnAction(e -> clickButtonAction(e));
        buttonPause.setOnAction(e -> clickButtonAction(e));
        buttonEnd.setOnAction(e -> clickButtonAction(e));
        buttonSearchAll.setOnAction(e -> clickButtonAction(e));
        buttonSearchById.setOnAction(e -> clickButtonAction(e));

        HBox hBoxBottom = new HBox();
        hBoxBottom.setSpacing(10);
//        hBox.setPadding(new Insets(20));

        hBoxBottom.setPadding(new Insets(15));
        hBoxBottom.getChildren().addAll(buttonStart, buttonPause, buttonEnd);

        HBox hBoxTop = new HBox();
        hBoxTop.getChildren().addAll(buttonSearchAll);

        VBox vBoxSearchByIdTop = new VBox();
        vBoxSearchByIdTop.getChildren().addAll(labelSearchbyId, buttonSearchById);

        BorderPane layout1 = new BorderPane();
        layout1.setTop(addGridPane());

        layout1.setBottom(hBoxBottom);

        Scene scene1 = new Scene(layout1, 350, 600);

        window.setScene(scene1);
        window.setTitle(windowTitle);
        window.show();

    }

    public void clickButtonAction(ActionEvent event) {

        Button button = (Button) event.getSource();

//                label1.setText("clicked: "+button.getText());
        System.out.println("button clicked: " + button.getText());

        if (button.equals(buttonSearchAll)) {
            redmineData.printAll(redmineData.getIssues());
        } 
        else if (button.equals(buttonSearchById)) {
            System.out.println("inside 'buttonSearchById'");
            System.out.println(redmineData.searchIssueById(Integer.parseInt(textFieldSearchById.getText())));
            textMessage.setText(redmineData.getExceptionMessage());
        }
        else if(button.equals(buttonStart)){
            System.out.println("we will count time");
        }
        
    }

    public GridPane addGridPane() {

        GridPane gridPaneTop = new GridPane();
        gridPaneTop.setHgap(5);
        gridPaneTop.setVgap(5);
        gridPaneTop.setPadding(new Insets(10, 10, 10, 10));

        // column 0-3 row 0        
        textSearchById = new Text("Search by Id");
        textSearchById.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
//         gridPane.add(node, column Index,row Index, column span(rozpietosc), row span)
        gridPaneTop.add(textSearchById, 0, 0, 3, 1);

//         column 0 row 1 
        Label labelSearchById = new Label("Id: ");
        gridPaneTop.add(labelSearchById, 0, 1);

        // column 1 row 1
        textFieldSearchById = new TextField();
        textFieldSearchById.setPrefWidth(80);
        gridPaneTop.add(textFieldSearchById, 1, 1);

        // column 2 row 1
        gridPaneTop.add(buttonSearchById, 2, 1);

        // column 0-3 row 2
        textMessage = new Text();
        gridPaneTop.add(textMessage, 0, 2, 3, 1);

        
       
        
        // column 0 row 15
        gridPaneTop.add(buttonStart,0,15);
           // column 1 row 15
        gridPaneTop.add(buttonPause,1,15);
           // column 2 row 15
        gridPaneTop.add(buttonEnd,2,15);
        
        
//        // Category in column 2, row 1
//        Text category = new Text("Sales:");
//        category.setFont(Font.font("Arial", FontWeight.BOLD, 20));
//        grid.add(category, 1, 0);
//
//        // Title in column 3, row 1
//        Text chartTitle = new Text("Current Year");
//        chartTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
//        grid.add(chartTitle, 2, 0);
//
//        // Subtitle in columns 2-3, row 2
//        Text chartSubtitle = new Text("Goods and Services");
//        grid.add(chartSubtitle, 1, 1, 2, 1);
//
//        // House icon in column 1, rows 1-2
//        ImageView imageHouse = new ImageView(
//                new Image(ProjectFX.class.getResourceAsStream("/graphics/2.jpg")));
//        grid.add(imageHouse, 0, 0, 1, 2);
//
//        // Left label in column 1 (bottom), row 3
//        Text goodsPercent = new Text("Goods\n80%");
//        GridPane.setValignment(goodsPercent, VPos.BOTTOM);
//        grid.add(goodsPercent, 0, 2);
//
//        // Chart in columns 2-3, row 3
//        ImageView imageChart = new ImageView(
//                new Image(ProjectFX.class.getResourceAsStream("/graphics/1.jpg")));
//        grid.add(imageChart, 1, 2, 2, 1);
//
//        // Right label in column 4 (top), row 3
//        Text servicesPercent = new Text("Services\n20%");
//        GridPane.setValignment(servicesPercent, VPos.TOP);
//        grid.add(servicesPercent, 3, 2);
        gridPaneTop.gridLinesVisibleProperty().set(true);
        return gridPaneTop;
    }
}

//
//
//public class Test7 extends Application{   
//    Stage window;
//    Label label1;
//        public static void main(String[] args){
//            launch(args);
//        }
//    @Override
//    public void start(Stage primaryStage) throws Exception {     
//        window=primaryStage;
//       label1=new Label();     
//        Button button1=new Button("Button 1");
//        Button button2=new Button("Button 2");
//        Button button3=new Button("Button 3");
//        Button button4=new Button("Button 4");
//        Button button5=new Button("Button 5");
//        Button button6=new Button("Button 6");
//
//        button1.setOnAction(e ->{  
//            buttonAction(e);
//        });
//        button2.setOnAction(e ->{  
//            buttonAction(e);
//        });
//        button3.setOnAction(e ->{  
//            buttonAction(e);
//        });
//        button4.setOnAction(e ->{  
//            buttonAction(e);
//        });
//        button5.setOnAction(e ->{  
//            buttonAction(e);
//        });
//        button6.setOnAction(e ->{  
//            buttonAction(e);
//        });    
//        // vertical
//        VBox vBox=new VBox();
//        // space between buttons
//        vBox.setSpacing(5);
//        // margin around button
//        vBox.setPadding(new Insets(20));
//            vBox.getChildren().addAll(button1,button2,button3,label1);     
//        // horicontal
//        HBox hBox=new HBox();
//        hBox.setSpacing(5);
//        hBox.setPadding(new Insets(20));
//            hBox.getChildren().addAll(button4,button5,button6);     
//        BorderPane layout1=new BorderPane();
//                   layout1.setTop(hBox);
//                   layout1.setBottom(vBox);       
//        Scene scene1=new Scene(layout1,300,400);       
//        window.setScene(scene1);
//        window.show();
//    }
//     void buttonAction(ActionEvent event){
//                Button button=(Button) event.getSource();
//                label1.setText("clicked: "+button.getText());
//                System.out.println("clicked:"+button.getText());
//        }
//}
