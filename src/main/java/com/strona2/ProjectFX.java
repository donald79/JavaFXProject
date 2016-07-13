package com.strona2;

import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Project;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.animation.*;
import javafx.event.*;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javafx.application.Application;
import javafx.beans.DefaultProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;

import javafx.scene.control.Labeled;
import javafx.scene.control.PasswordField;

//https://dzone.com/articles/build-your-own-java-stopwatch
public class ProjectFX extends Application {

    RedmineData redmineData;
    Watch watch = new Watch();

    Stage window;
    Scene sceneLogin;
    Scene sceneMain;
    BorderPane layoutMain;
    GridPane layoutLogin;
    GridPane gridPaneTop;
    Label labelSelectProject,labelSearchbyId, labelTotalTimeTaken, labelId, labelSubject, labelTracker, labelStatus, labelAssignedTo, labelUpdated, labelEstimatedTime, labelSpentTime, labelTargetVersion;
    Button buttonLogOut, buttonStart, buttonPause, buttonResume, buttonEnd, buttonSearchById, buttonSearchAll;
    TextField textFieldSearchById;
    Text textMessage, textSearchById, textId, textSubject, textTracker, textStatus, textAssignedTo, textUpdated, textEstimatedTime, textSpentTime, textTargetVersion;
    String tempTime;
    List<Project> listOfProjects;
    List<String> listOfProjectsNames;
    String[] table={};
    boolean isPauseButtonAlreadyClicked = false;
    boolean isUserLoggedIn = false;

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

        // main window
        buttonLogOut = new Button("log out");
        buttonStart = new Button("start");
//        buttonStart.setMinSize(40, 30);
        buttonPause = new Button("pause");
//        buttonPause.setMinSize(40, 30);
        buttonResume = new Button("resume");
        buttonEnd = new Button("end");
//        buttonEnd.setMinSize(40, 30);
        buttonSearchById = new Button("search by Id");
        buttonSearchAll = new Button("search all");
        listOfProjects=new ArrayList<>();
        listOfProjectsNames=new ArrayList<>();

        buttonStart.setOnAction(e -> clickButtonAction(e));
        buttonPause.setOnAction(e -> clickButtonAction(e));
        buttonResume.setOnAction(e -> clickButtonAction(e));
        buttonEnd.setOnAction(e -> clickButtonAction(e));
        buttonSearchAll.setOnAction(e -> clickButtonAction(e));
        buttonSearchById.setOnAction(e -> clickButtonAction(e));
        buttonLogOut.setOnAction(e -> clickButtonAction(e));
        
        textId = new Text(null);
        textSubject = new Text(null);
        textTracker = new Text(null);
        textStatus = new Text(null);
        textSubject = new Text(null);
        textAssignedTo = new Text(null);
        textUpdated = new Text(null);
        textEstimatedTime = new Text(null);
        textSpentTime = new Text(null);
        textTargetVersion = new Text(null);

        layoutMain = new BorderPane();
        layoutMain.setTop(addGridPaneMain());

        // log in
        layoutLogin = new GridPane();
        layoutLogin.setAlignment(Pos.TOP_CENTER);
        layoutLogin.setHgap(10);
        layoutLogin.setVgap(10);
        layoutLogin.setPadding(new Insets(25, 25, 25, 25));
        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        layoutLogin.add(scenetitle, 0, 0, 2, 1);
        Label userName = new Label("User Name:");
        layoutLogin.add(userName, 0, 1);
        TextField login = new TextField();
        layoutLogin.add(login, 1, 1);
        Label labelPassword = new Label("Password:");
        layoutLogin.add(labelPassword, 0, 2);
        PasswordField passwordField = new PasswordField();
        layoutLogin.add(passwordField, 1, 2);
        Button buttonSignIn = new Button("Sign in");
        HBox layoutLoginButton = new HBox(10);
        layoutLoginButton.setAlignment(Pos.BOTTOM_RIGHT);
        layoutLoginButton.getChildren().add(buttonSignIn);
        layoutLogin.add(layoutLoginButton, 1, 4);
        layoutLogin.add(textMessage, 0, 5);
        
        buttonSignIn.setOnAction(e -> {
            textMessage.setText(null);
            redmineData.logingIn(login.getText(), passwordField.getText());
            textMessage.setText(redmineData.getExceptionMessage());
            if (redmineData.isLoginAndPasswordCorrect == true ) {
                
                
                listOfProjects=redmineData.getProjectsFromRedmine();
                                   
                  for( Project value: listOfProjects){
                    System.out.println("value.getName(): "+value.getName());
                     listOfProjectsNames.add(value.getName());
                }
                  table=listOfProjectsNames.toArray(table);
                  
                      System.out.println("==========================="+listOfProjectsNames);
                   window.setScene(sceneMain);
                   
            }
            
        });

        sceneMain = new Scene(layoutMain, 350, 600);
        sceneLogin = new Scene(layoutLogin, 350, 600);
//        window.setScene(sceneMain);
        window.setScene(sceneLogin);
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

            clearDataBeforeNextSearch();

            textMessage.setText(redmineData.getExceptionMessage());

            Issue tempIssue = redmineData.searchIssueById(Integer.parseInt(textFieldSearchById.getText()));

            textId.setText(Integer.toString(tempIssue.getId()));
            textSubject.setText(tempIssue.getSubject());
            textTracker.setText(tempIssue.getTracker().getName());
            textStatus.setText(tempIssue.getStatusName());
            textAssignedTo.setText(tempIssue.getAssignee().getFullName());
            textUpdated.setText(formatDate(tempIssue.getUpdatedOn()));
            textEstimatedTime.setText(tempIssue.getEstimatedHours().toString());
            textSpentTime.setText(tempIssue.getSpentHours().toString());
            textTargetVersion.setText(tempIssue.getTargetVersion().getName());

        } else if (button.equals(buttonStart)) {
            watch.start();

        } else if (button.equals(buttonPause) && isPauseButtonAlreadyClicked == false) {

            watch.pause();
            System.out.println("time - paused: " + watch.getActualspentTime());
            isPauseButtonAlreadyClicked = true;
//            refreshWindow();

        } else if (button.equals(buttonResume) && isPauseButtonAlreadyClicked == true) {
            watch.resume();
            System.out.println("time - resumed: " + watch.getActualspentTime());
            isPauseButtonAlreadyClicked = false;
//            refreshWindow();
            
        } else if (button.equals(buttonEnd)) {
            try {
                watch.end();
            } catch (IllegalStateException e) {
                System.out.println("from 'buttonEnd': " + e);
            }
        }
        else if(button.equals(buttonLogOut)){
                 redmineData.logingIn(null, null);
                 if (redmineData.isLoginAndPasswordCorrect == false) {
                window.setScene(sceneLogin);
            }
        }
        System.out.println("time: " + watch.getActualspentTime());
    }

    public GridPane addGridPaneMain() {

        gridPaneTop = new GridPane();
        gridPaneTop.setHgap(5);
        gridPaneTop.setVgap(5);
        gridPaneTop.setPadding(new Insets(10, 10, 10, 10));

        
        // column 0-2 row 0  
        labelSelectProject=new Label ("Select Project");
        labelSelectProject.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        gridPaneTop.add(labelSelectProject, 0, 0, 2, 1);
       
         // column 0 row 1 
         
//https://www.youtube.com/watch?v=K3CenJ2bMok

// https://www.youtube.com/watch?v=yLzfZhJTaa8


ObservableList<List> observableList=FXCollections.observableArrayList();
observableList.addAll(listOfProjectsNames);




   ChoiceBox<String> choiceBox = new ChoiceBox<>();

    
       
            for(List value: observableList){
                  choiceBox.setItems(value);
            }

            gridPaneTop.add(choiceBox,0,1);

//            
//         
     
        
//        // column 0-3 row 0        
//        textSearchById = new Text("Search by Id");
//        textSearchById.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
////         gridPane.add(node, column Index,row Index, column span(rozpietosc), row span)
//        gridPaneTop.add(textSearchById, 0, 0, 3, 1);
//
//         column 3 row 0      
        gridPaneTop.add(buttonLogOut, 3, 0);
//
////         column 0 row 1 
//        labelSearchbyId = new Label("Id: ");
//        gridPaneTop.add(labelSearchbyId, 0, 1);
//
//        // column 1 row 1
//        textFieldSearchById = new TextField();
//        textFieldSearchById.setPrefWidth(80);
//        gridPaneTop.add(textFieldSearchById, 1, 1);
//
//        // column 2 row 1
//        gridPaneTop.add(buttonSearchById, 2, 1);

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
        labelAssignedTo = new Label("Assigned To");
        labelAssignedTo.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        gridPaneTop.add(labelAssignedTo, 2, 5);

        // column 3 row 5
        labelUpdated = new Label("Updated");
        labelUpdated.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        gridPaneTop.add(labelUpdated, 3, 5);

        //  column 0 row 7
        labelEstimatedTime = new Label("Estimated Time");
        labelEstimatedTime.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        gridPaneTop.add(labelEstimatedTime, 0, 7);

        // column 1 row 7
        labelSpentTime = new Label("Time Spent");
        labelSpentTime.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        gridPaneTop.add(labelSpentTime, 1, 7);

        // column 2 row 7
        labelTargetVersion = new Label("Target Version");
        labelTargetVersion.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        gridPaneTop.add(labelTargetVersion, 2, 7);

        // column 0 row 4         
        gridPaneTop.add(textId, 0, 4);

        // column 1-3 row 4  
        gridPaneTop.add(textSubject, 1, 4, 3, 1);

        // column 0 row 6   
        gridPaneTop.add(textTracker, 0, 6);

        // column 1 row 6  
        gridPaneTop.add(textStatus, 1, 6);

        // column 2 row 6
        gridPaneTop.add(textAssignedTo, 2, 6);

        // column 3 row 6
        gridPaneTop.add(textUpdated, 3, 6);

        // column 0 row 8
        gridPaneTop.add(textEstimatedTime, 0, 8);

        // column 1 row 8
        gridPaneTop.add(textSpentTime, 1, 8);

        // column 2 row 8
        gridPaneTop.add(textTargetVersion, 2, 8);

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
        layoutMain = new BorderPane();
        layoutMain.setTop(addGridPaneMain());
        sceneMain = new Scene(layoutMain, 350, 600);
        window.setScene(sceneMain);
        window.show();

    }

    public String formatDate(Date date) {
        String formatedDate;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd/MM/yyyy K:m a", Locale.ENGLISH);
        formatedDate = formatter.format(date);
        return formatedDate;
    }

    public void clearDataBeforeNextSearch() {
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
