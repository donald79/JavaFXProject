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
