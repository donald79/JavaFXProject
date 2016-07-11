package com.strona2;



import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.lang.time.StopWatch;











public class Watch extends StopWatch {

    StopWatch timeAction;

    public Watch() {
        timeAction = new StopWatch();
    }

    public void start() {

        timeAction.reset();
        timeAction.start();
    }

    public void pause() {
        timeAction.suspend();
    }

    //Resume the stopwatch after a suspend.
    public void resume() {
        timeAction.resume();
    }

    public void end() {
        timeAction.stop();

        System.out.println(timeAction.toString());
    }

    public String getActualspentTime() {
        long timeSpent = timeAction.getTime();
        Date date = new Date(timeSpent);

//        DateFormat formatter = new SimpleDateFormat("h:mm");
//        String dateFormatted = formatter.format(date);
        long days = TimeUnit.MILLISECONDS.toDays(timeSpent);
        timeSpent -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(timeSpent);
        timeSpent -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeSpent);
        timeSpent -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeSpent);

        StringBuilder sb = new StringBuilder(64);
//        sb.append(days);
//        sb.append(" Days ");
        sb.append(hours);
        sb.append(" Hours ");
        sb.append(minutes);
        sb.append(" Minutes ");
        sb.append(seconds);
        sb.append(" Seconds");

        return (sb.toString());
//        return (dateFormatted);
    }

}
 // TIMELINE class 
// http://www.java2s.com/Tutorials/Java/JavaFX/1010__JavaFX_Timeline_Animation.htm








//
//http://stackoverflow.com/questions/13227809/displaying-changing-values-in-javafx-label
//
//import java.io.IOException;
//import java.net.URL;
//import javafx.animation.*;
//import javafx.event.*;
//import javafx.scene.control.Label;
//import javafx.util.Duration;
//
//import java.util.Calendar;
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//public class Test10 extends Application {
//  public static void main(String[] args) { launch(args); }
//  @Override public void start(Stage stage) throws IOException {
//    stage.setScene(new Scene(new DigitalClock(), 100, 50));
//    stage.show();
//  }
//}
//
///**
// * Creates a digital clock display as a simple label.
// * Format of the clock display is hh:mm:ss aa, where:
// * hh Hour in am/pm (1-12)
// * mm Minute in hour
// * ss Second in minute
// * aa Am/pm marker
// * Time is the system time for the local timezone.
// */
//class DigitalClock extends Label {
//  public DigitalClock() {
//    bindToTime();
//  }
//
//  // the digital clock updates once a second.
//  private void bindToTime() {
//    Timeline timeline = new Timeline(
//      new KeyFrame(Duration.seconds(0),
//        new EventHandler<ActionEvent>() {
//          @Override public void handle(ActionEvent actionEvent) {
//            Calendar time = Calendar.getInstance();
//            String hourString = StringUtilities.pad(2, ' ', time.get(Calendar.HOUR) == 0 ? "12" : time.get(Calendar.HOUR) + "");
//            String minuteString = StringUtilities.pad(2, '0', time.get(Calendar.MINUTE) + "");
//            String secondString = StringUtilities.pad(2, '0', time.get(Calendar.SECOND) + "");
//            String ampmString = time.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
//            setText(hourString + ":" + minuteString + ":" + secondString + " " + ampmString);
//          }
//        }
//      ),
//      new KeyFrame(Duration.seconds(1))
//    );
//    timeline.setCycleCount(Animation.INDEFINITE);
//    timeline.play();
//  }
//}
//
//class StringUtilities {
//  /**
//   * Creates a string left padded to the specified width with the supplied padding character.
//   * @param fieldWidth the length of the resultant padded string.
//   * @param padChar a character to use for padding the string.
//   * @param s the string to be padded.
//   * @return the padded string.
//   */
//  public static String pad(int fieldWidth, char padChar, String s) {
//    StringBuilder sb = new StringBuilder();
//    for (int i = s.length(); i < fieldWidth; i++) {
//      sb.append(padChar);
//    }
//    sb.append(s);
//
//    return sb.toString();
//  }
//}