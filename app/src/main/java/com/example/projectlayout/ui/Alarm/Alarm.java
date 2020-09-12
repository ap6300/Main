package com.example.projectlayout.ui.Alarm;

public class Alarm {

    int id;
    int hour;
    int min;
    String description;
    boolean alarmOn;
    boolean recurring;
    boolean mon;
    boolean tue;
    boolean wed;
    boolean thur;
    boolean fri;
    boolean sat;
    boolean sun;
    byte [] image;

    public Alarm(int id,int hour,int min, String description,boolean alarmOn, boolean recurring, boolean mon, boolean tue, boolean wed, boolean thur, boolean fri, boolean sat, boolean sun, byte [] image)
    {
        this.id=id;
        this.hour = hour;
        this.min = min;
        this.description = description;
        this.alarmOn = alarmOn;
        this.recurring = recurring;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thur= thur;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
        this.image = image;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public boolean isMon() {
        return mon;
    }

    public void setMon(boolean mon) {
        this.mon = mon;
    }

    public boolean isTue() {
        return tue;
    }

    public void setTue(boolean tue) {
        this.tue = tue;
    }

    public boolean isWed() {
        return wed;
    }

    public void setWed(boolean wed) {
        this.wed = wed;
    }

    public boolean isThur() {
        return thur;
    }

    public void setThur(boolean thur) {
        this.thur = thur;
    }

    public boolean isFri() {
        return fri;
    }

    public void setFri(boolean fri) {
        this.fri = fri;
    }

    public boolean isSat() {
        return sat;
    }

    public void setSat(boolean sat) {
        this.sat = sat;
    }

    public boolean isSun() {
        return sun;
    }

    public void setSun(boolean sun) {
        this.sun = sun;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean isAlarmOn() {
        return alarmOn;
    }

    public void setAlarmOn(boolean alarmOn) {
        this.alarmOn = alarmOn;
    }
}
