package com.kacstudios.game;

import jdk.vm.ci.meta.Local;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeEngine {
    private double timeModifier; //a modifier to add or remove from the current time
    private int dilation = -1;


    /**
     * @param timeOffset a constant (in seconds) indicated how much the time should be modified forward or backward
     *                     to reach the current game time.
     */
    public TimeEngine(long timeOffset){
        timeModifier = timeOffset;
    }
    public TimeEngine(){
        timeModifier = 0; // 0 second offset
    }

    /**
     * timeHook runs the logic to calculate all time dilations.
     * @param deltaSeconds is the amount of time that has passed since the last call to this method.
     */
    public void timeHook(float deltaSeconds){
        if (dilation > -1) timeModifier += (dilation - 1) * deltaSeconds;
    }

    /**
     * This sets the time to be dilated.
     *
     * @param multiplier the multiple of time units to pass per time unit.
     *                   (EX: dilation of 2 means that 2 seconds pass for every 1.
     *                   DOES NOTHING if the dilation is less than 0.
     */
    public void dilateTime(int multiplier) {
        if(multiplier < 0) return;
        dilation = multiplier;
    }

    public void undilateTime() {
        dilation = -1;
    }

    /**
     * Pauses the passage of time in the game
     */
    public void pause() {
        dilation = 0;
    }

    /**
     * Resumes the game with <b>NO TIME DILATION</b>
     */
    public void resume() {
        dilation = -1;
    }

    public LocalDateTime getDateTime(){
        return LocalDateTime.now().plusSeconds((long)timeModifier);
    }

    public LocalTime getTime() {
        return LocalDateTime.now().plusSeconds((long)timeModifier).toLocalTime();
    }

    public LocalDate getDate() {
        return LocalDateTime.now().plusSeconds((long)timeModifier).toLocalDate();
    }

    public Duration getDurationSince(LocalDateTime dateTime){
        return Duration.between(dateTime, getDateTime());
    }
    public Period getPeriodSince(LocalDate date) { return Period.between(getDate(), date); }

    public long getDaysSince(LocalDateTime time){
        return getDurationSince(time).toDays();
    }
    public long getDaysSince(LocalDate date){
        return getPeriodSince(date).getDays();
    }

    public long getMinutesSince(LocalDateTime dateTime) {
        return getDurationSince(dateTime).toMinutes();
    }

    public long getSecondsSince(LocalDateTime dateTime) {
        return ChronoUnit.SECONDS.between(dateTime, getDateTime());
    }

    public String getFormattedString(){
        return getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Output a formatted string version of the current time depending on the format provided
     * @param formatString A valid time format string like "yyyy-MM-dd HH:mm:ss"
     * @return formatted string
     */
    public String getFormattedString(String formatString){
        return getDateTime().format(DateTimeFormatter.ofPattern(formatString));
    }

    public int getDilation() {
        return dilation;
    }

    public double getOffset() {
        return timeModifier;
    }
}
