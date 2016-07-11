package com.strona2;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Issue;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Timeline;
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

import javafx.animation.*;
import javafx.event.*;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Calendar;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import javafx.animation.*;
import javafx.event.*;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Calendar;
import javafx.application.Application;
import javafx.beans.DefaultProperty;
import javafx.scene.Scene;
import javafx.scene.control.Labeled;

//https://dzone.com/articles/build-your-own-java-stopwatch
public class ProjectFX extends Application {

    RedmineData redmineData;
    Watch watch = new Watch();

    Stage window;

    Scene scene1;
    BorderPane layout1;
    GridPane gridPaneTop;
    Label labelSearchbyId, labelTotalTimeTaken, labelId, labelSubject, labelTracker, labelStatus, labelAssignedTo, labelUpdated, labelEstimatedTime, labelSpentTime, labelTargetVersion;
    Button buttonStart, buttonPause, buttonResume, buttonEnd, buttonSearchById, buttonSearchAll;
    TextField textFieldSearchById;
    Text textMessage, textSearchById, textId, textSubject, textTracker, textStatus, textAssignedTo, textUpdated, textEstimatedTime, textSpentTime, textTargetVersion;
    String tempTime;
    boolean isPauseButtonAlreadyClicked = false;

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
        labelTotalTimeTaken = new Label();
        buttonStart = new Button("start");
//        buttonStart.setMinSize(40, 30);
        buttonPause = new Button("pause");
//        buttonPause.setMinSize(40, 30);
        buttonResume = new Button("resume");
        buttonEnd = new Button("end");
//        buttonEnd.setMinSize(40, 30);
        buttonSearchById = new Button("search by Id");
        buttonSearchAll = new Button("search all");

        buttonStart.setOnAction(e -> clickButtonAction(e));
        buttonPause.setOnAction(e -> clickButtonAction(e));
        buttonResume.setOnAction(e -> clickButtonAction(e));
        buttonEnd.setOnAction(e -> clickButtonAction(e));
        buttonSearchAll.setOnAction(e -> clickButtonAction(e));
        buttonSearchById.setOnAction(e -> clickButtonAction(e));
        textId = new Text(null);
        textSubject = new Text(null);
        textTracker = new Text(null);
        textStatus = new Text(null);
        textSubject=new Text(null);
        textAssignedTo=new Text(null);
        textUpdated=new Text(null);
        textEstimatedTime=new Text(null);
        textSpentTime=new Text(null);
        textTargetVersion=new Text(null);
        

        layout1 = new BorderPane();
        layout1.setTop(addGridPane());

        scene1 = new Scene(layout1, 350, 600);

        window.setScene(scene1);
        window.setTitle(windowTitle);
        window.show();

    }

    public void clickButtonAction(ActionEvent event) {

        Button button = (Button) event.getSource();

        System.out.println("button clicked: " + button.getText());

        if (button.equals(buttonSearchAll)) {
            redmineData.printAll(redmineData.getIssues());
        } else if (button.equals(buttonSearchById)) {
            System.out.println("inside 'buttonSearchById'");
            
        //  clear before searching
        textId.setText(null);
        textSubject.setText(null);
        textTracker.setText(null);
        textStatus.setText(null);
        textSubject.setText(null);
        textAssignedTo.setText(null);
        textUpdated.setText(null);
        textEstimatedTime.setText(null);
        textSpentTime.setText(null);
        textTargetVersion.setText(null);
        
        
            textMessage.setText(redmineData.getExceptionMessage());

            Issue tempIssue = redmineData.searchIssueById(Integer.parseInt(textFieldSearchById.getText()));

            textId.setText(Integer.toString(tempIssue.getId()));
            textSubject.setText(tempIssue.getSubject());
            textTracker.setText(tempIssue.getTracker().getName());
            textStatus.setText(tempIssue.getStatusName());
            textAssignedTo.setText(tempIssue.getAssignee().getFullName());
            textUpdated.setText(tempIssue.getUpdatedOn().toString());
            textEstimatedTime.setText(tempIssue.getEstimatedHours().toString());
            textSpentTime.setText(tempIssue.getSpentHours().toString());
            textTargetVersion.setText(tempIssue.getTargetVersion().getName());

        } else if (button.equals(buttonStart)) {
            watch.start();

        } else if (button.equals(buttonPause) && isPauseButtonAlreadyClicked == false) {

            watch.pause();
            System.out.println("time - paused: " + watch.getActualspentTime());
            isPauseButtonAlreadyClicked = true;
            refreshWindow();

        } else if (button.equals(buttonResume) && isPauseButtonAlreadyClicked == true) {
            watch.resume();
            System.out.println("time - resumed: " + watch.getActualspentTime());
            isPauseButtonAlreadyClicked = false;
            refreshWindow();
        } else if (button.equals(buttonEnd)) {
            try {
                watch.end();

            } catch (IllegalStateException e) {
                System.out.println("from 'buttonEnd': " + e);
            }

        }
        System.out.println("time: " + watch.getActualspentTime());
    }

    public GridPane addGridPane() {

        gridPaneTop = new GridPane();
        gridPaneTop.setHgap(5);
        gridPaneTop.setVgap(5);
        gridPaneTop.setPadding(new Insets(10, 10, 10, 10));

        // column 0-3 row 0        
        textSearchById = new Text("Search by Id");
        textSearchById.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
//         gridPane.add(node, column Index,row Index, column span(rozpietosc), row span)
        gridPaneTop.add(textSearchById, 0, 0, 3, 1);

//         column 0 row 1 
        labelSearchbyId = new Label("Id: ");
        gridPaneTop.add(labelSearchbyId, 0, 1);

        // column 1 row 1
        textFieldSearchById = new TextField();
        textFieldSearchById.setPrefWidth(80);
        gridPaneTop.add(textFieldSearchById, 1, 1);

        // column 2 row 1
        gridPaneTop.add(buttonSearchById, 2, 1);

        // column 0-3 row 2
        textMessage = new Text();
        gridPaneTop.add(textMessage, 0, 2, 3, 1);

        // column 0 row 3 
        labelId = new Label("Id");
        labelId.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        gridPaneTop.add(labelId, 0, 3);

        // column 1-3 row 3
        labelSubject = new Label("Subject");
        labelSubject.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        gridPaneTop.add(labelSubject, 1, 3, 3, 1);

        // column 0 row 5
        labelTracker = new Label("Tracker");
        labelTracker.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        gridPaneTop.add(labelTracker, 0, 5);

        // column 1 row 5
        labelStatus = new Label("Status");
        labelStatus.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        gridPaneTop.add(labelStatus, 1, 5);
        
//      column 2 row 5
        labelAssignedTo=new Label("Assigned To");
        labelAssignedTo.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        gridPaneTop.add(labelAssignedTo,2,5);
    
        // column 3 row 5
        labelUpdated=new Label("Updated");
        labelUpdated.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        gridPaneTop.add(labelUpdated,3,5);

       //  column 0 row 7
        labelEstimatedTime=new Label("Estimated Time");
        labelEstimatedTime.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        gridPaneTop.add(labelEstimatedTime,0,7);
        
         // column 1 row 7
        labelSpentTime=new Label("Time Spent");
        labelSpentTime.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        gridPaneTop.add(labelSpentTime,1,7);
        
        // column 2 row 7
        labelTargetVersion=new Label("Target Version");
        labelTargetVersion.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        gridPaneTop.add(labelTargetVersion,2,7);

        // column 0 row 4         
        gridPaneTop.add(textId, 0, 4);

        // column 1-3 row 4  
        gridPaneTop.add(textSubject, 1, 4, 3, 1);

        // column 0 row 6   
        gridPaneTop.add(textTracker, 0, 6);

        // column 1 row 6  
        gridPaneTop.add(textStatus, 1, 6);

        // column 2 row 6
        gridPaneTop.add(textAssignedTo,2,6);

        // column 3 row 6
        gridPaneTop.add(textUpdated,3,6);
        
        // column 0 row 8
        gridPaneTop.add(textEstimatedTime,0,8);
       
        // column 1 row 8
        gridPaneTop.add(textSpentTime,1,8);

        // column 2 row 8
        gridPaneTop.add(textTargetVersion,2,8);
        
         // column 0 row 14
        gridPaneTop.add(timeTakenDisplayedLive(), 0, 14, 3, 1);

        // column 0 row 15
        gridPaneTop.add(buttonStart, 0, 15);

        // column 1 row 15 / interchangle with "buttonResume"
        if (isPauseButtonAlreadyClicked == false) {
            gridPaneTop.add(buttonPause, 1, 15);
        }
        // column 1 row 15 / interchangle with "buttonPause"
        if (isPauseButtonAlreadyClicked == true) {
            gridPaneTop.add(buttonResume, 1, 15);

        }
        // column 2 row 15
        gridPaneTop.add(buttonEnd, 2, 15);

        gridPaneTop.gridLinesVisibleProperty().set(true);
        return gridPaneTop;
    }

    public void refreshWindow() {
        layout1 = new BorderPane();
        layout1.setTop(addGridPane());
        scene1 = new Scene(layout1, 350, 600);
        window.setScene(scene1);
        window.show();

    }

// Time taken displayed on a Gui live
// http://stackoverflow.com/questions/13227809/displaying-changing-values-in-javafx-label
    public Label timeTakenDisplayedLive() {
        labelTotalTimeTaken.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        labelTotalTimeTaken.setText(watch.getActualspentTime());
                    }
                }
                ),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        return labelTotalTimeTaken;
    }

}
