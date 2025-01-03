package com.kacstudios.game.utilities;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeEngine {
    private static LocalDateTime currentTime;
    private static int dilation = 1;

    /**
     * @param startTime a constant (in seconds) indicated how much the time should be modified forward or backward
     *                     to reach the current game time.
     */
    public static void Init(LocalDateTime startTime){
        currentTime = startTime;
    }
    public static void Init(){
        currentTime = LocalDateTime.now();
    }

    /**
     * timeHook runs the logic to calculate all time dilations.
     * @param deltaSeconds is the amount of time that has passed since the last call to this method.
     */
    public static void act(float deltaSeconds){
        if (dilation > -1) {
            currentTime = currentTime.plusNanos((long)(dilation * deltaSeconds * Math.pow(10, 9)));
        }
    }

    /**
     * This sets the time to be dilated.
     *
     * @param multiplier the multiple of time units to pass per time unit.
     *                   (EX: dilation of 2 means that 2 seconds pass for every 1.
     *                   DOES NOTHING if the dilation is less than 0. 0 freezes time.
     */
    public static void dilateTime(int multiplier) {
        if(multiplier < 0) return;
        dilation = multiplier;
    }

    public static void undilateTime() {
        dilation = 1;
    }

    /**
     * Pauses the passage of time in the game
     */
    public static void pause() {
        dilation = 0;
    }

    /**
     * Resumes the game with <b>NO TIME DILATION</b>
     */
    public static void resume() {
        dilation = 1;
    }

    public static LocalDateTime getDateTime(){
        return currentTime;
    }

    public static LocalTime getTime() {
        return currentTime.toLocalTime();
    }

    public static LocalDate getDate() {
        return currentTime.toLocalDate();
    }

    public static Duration getDurationSince(LocalDateTime dateTime){
        return Duration.between(dateTime, getDateTime());
    }
    public static Period getPeriodSince(LocalDate date) { return Period.between(getDate(), date); }

    public static long getDaysSince(LocalDateTime time){
        return getDurationSince(time).toDays();
    }
    public static long getDaysSince(LocalDate date){
        return getPeriodSince(date).getDays();
    }

    public static long getMinutesSince(LocalDateTime dateTime) {
        return getDurationSince(dateTime).toMinutes();
    }

    public static long getSecondsSince(LocalDateTime dateTime) {
        return ChronoUnit.SECONDS.between(dateTime, getDateTime());
    }

    public static String getFormattedString(){
        return getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Output a formatted string version of the current time depending on the format provided
     * @param formatString A valid time format string like "yyyy-MM-dd HH:mm:ss"
     * @return formatted string
     */
    public static String getFormattedString(String formatString){
        return getDateTime().format(DateTimeFormatter.ofPattern(formatString));
    }

    public static int getDilation() {
        return dilation;
    }
}
