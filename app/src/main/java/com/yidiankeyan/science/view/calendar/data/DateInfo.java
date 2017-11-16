package com.yidiankeyan.science.view.calendar.data;

public class DateInfo {
    private int date;
    private boolean isThisMonth;
    private boolean isWeekend;
    private boolean isHoliday;
    private String NongliDate;

    private boolean isSign;

    public boolean isSign() {
        return isSign;
    }

    public void setSign(boolean sign) {
        isSign = sign;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getNongliDate() {
        return NongliDate;
    }

    public void setNongliDate(String nongliDate) {
        NongliDate = nongliDate;
    }

    public boolean isThisMonth() {
        return isThisMonth;
    }

    public void setThisMonth(boolean isThisMonth) {
        this.isThisMonth = isThisMonth;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean isHoliday) {
        this.isHoliday = isHoliday;
    }

    public boolean isWeekend() {
        return isWeekend;
    }

    public void setWeekend(boolean isWeekend) {
        this.isWeekend = isWeekend;
    }

    private boolean readed;

    public boolean isReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }

    private boolean isToday;

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }
}
