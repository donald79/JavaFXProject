package com.strona2;

import com.taskadapter.redmineapi.RedmineException;
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
import javafx.scene.Scene;
import javafx.stage.Stage;


//https://dzone.com/articles/build-your-own-java-stopwatch
public class ProjectFX extends Application {

    RedmineData redmineData;
    Watch watch  = new Watch();;
    Stage window;
    Scene scene1;
    BorderPane layout1;
    GridPane gridPaneTop;
    Label labelSearchbyId;
    Button buttonStart, buttonPause, buttonResume, buttonEnd, buttonSearchById, buttonSearchAll;
    TextField textFieldSearchById;
    Text textMessage, textSearchById,textTimeSpent;
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

        layout1 = new BorderPane();
        layout1.setTop(addGridPane());
        scene1 = new Scene(layout1, 350, 600);
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
        } else if (button.equals(buttonSearchById)) {
            System.out.println("inside 'buttonSearchById'");
            System.out.println(redmineData.searchIssueById(Integer.parseInt(textFieldSearchById.getText())));
            textMessage.setText(redmineData.getExceptionMessage());
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
            try{
                watch.end();

            }
            catch(IllegalStateException e){
                System.out.println("from 'buttonEnd': "+e);
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

        
        // column 0-3 row 14        
        textTimeSpent = new Text(watch.getActualspentTime());
        textTimeSpent.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
//         gridPane.add(node, column Index,row Index, column span(rozpietosc), row span)
        gridPaneTop.add(textTimeSpent, 0, 14, 3, 1);
        
                
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
//    public void refreshTime(){
//        while (true){
//             textTimeSpent=new Text(watch.getActualspentTime());
//        }
//    }


}


class DigitalClock extends Label {
  public DigitalClock() {
    bindToTime();
  }

  // the digital clock updates once a second.
  private void bindToTime() {
    Timeline timeline = new Timeline(
      new KeyFrame(Duration.seconds(0),
        new EventHandler<ActionEvent>() {
          @Override public void handle(ActionEvent actionEvent) {
            Calendar time = Calendar.getInstance();
            String hourString = StringUtilities.pad(2, ' ', time.get(Calendar.HOUR) == 0 ? "12" : time.get(Calendar.HOUR) + "");
            String minuteString = StringUtilities.pad(2, '0', time.get(Calendar.MINUTE) + "");
            String secondString = StringUtilities.pad(2, '0', time.get(Calendar.SECOND) + "");
            String ampmString = time.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
            setText(hourString + ":" + minuteString + ":" + secondString + " " + ampmString);
          }
        }
      ),
      new KeyFrame(Duration.seconds(1))
    );
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }
}

class StringUtilities {
  /**
   * Creates a string left padded to the specified width with the supplied padding character.
   * @param fieldWidth the length of the resultant padded string.
   * @param padChar a character to use for padding the string.
   * @param s the string to be padded.
   * @return the padded string.
   */
  public static String pad(int fieldWidth, char padChar, String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = s.length(); i < fieldWidth; i++) {
      sb.append(padChar);
    }
    sb.append(s);

    return sb.toString();
  }
}



