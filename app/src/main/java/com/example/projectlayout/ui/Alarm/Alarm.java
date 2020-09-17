package com.example.projectlayout.ui.Alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.projectlayout.Receiver;

import java.util.Calendar;

public class Alarm {

    public static final String MONDAY = "MONDAY";
    public static final String TUESDAY = "TUESDAY";
    public static final String WEDNESDAY = "WEDNESDAY";
    public static final String THURSDAY = "THURSDAY";
    public static final String FRIDAY = "FRIDAY";
    public static final String SATURDAY = "SATURDAY";
    public static final String SUNDAY = "SUNDAY";
    public static final String RECURRING = "RECURRING";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String ID = "ID";



    private int id;
    private int hour;
    private int min;
    private String description;
    boolean alarmOn;
    private boolean recurring;
    private boolean mon;
    private boolean tue;
    private boolean wed;
    private boolean thur;
    private boolean fri;
    private boolean sat;
    private boolean sun;
    private byte [] image;

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

    int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    int getMin() {
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

    boolean isRecurring() {
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

    public void schedule(Context context) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, Receiver.class);
        intent.putExtra(RECURRING, recurring);
        intent.putExtra(MONDAY, mon);
        intent.putExtra(TUESDAY, tue);
        intent.putExtra(WEDNESDAY, wed);
        intent.putExtra(THURSDAY, thur);
        intent.putExtra(FRIDAY, fri);
        intent.putExtra(SATURDAY, sat);
        intent.putExtra(SUNDAY, sun);
        intent.putExtra(DESCRIPTION,description);
        intent.putExtra(ID,id);


        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, id, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // if alarm time has already passed, increment day by 1
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        if (!recurring) {
            String toastText = null;
            try {
                toastText = String.format("One Time Alarm scheduled for %s at %02d:%02d",  toDay(calendar.get(Calendar.DAY_OF_WEEK)), hour, min, id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

            alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    alarmPendingIntent
            );
        } else {
            @SuppressLint("DefaultLocale") String toastText = String.format("Recurring Alarm  scheduled for %s at %02d:%02d",  getRecurringDaysText(), hour, min, id);
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

            final long RUN_DAILY = 24 * 60 * 60 * 1000;
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    RUN_DAILY,
                    alarmPendingIntent
            );
        }




        this.alarmOn = true;
    }


    public static final String toDay(int day) throws Exception {
        switch (day) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
        }
        throw new Exception("Could not locate day");
    }


    public String getRecurringDaysText() {
        if (!recurring) {
            return null;
        }

        String days = "";
        if (mon) {
            days += "Mon ";
        }
        if (tue) {
            days += "Tue ";
        }
        if (wed) {
            days += "Wed ";
        }
        if (thur) {
            days += "Thur ";
        }
        if (fri) {
            days += "Fri ";
        }
        if (sat) {
            days += "Sat ";
        }
        if (sun) {
            days += "Sun ";
        }

        return days;
    }

}
