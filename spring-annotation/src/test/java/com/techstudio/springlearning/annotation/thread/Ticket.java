package com.techstudio.springlearning.annotation.thread;

/**
 * @author lj
 * @date 2020/1/19
 */
public class Ticket {
    private int total;
    private volatile boolean keepRunning;

    public Ticket(int total, boolean keepRunning) {
        this.total = total;
        this.keepRunning = keepRunning;
    }

    public boolean isKeepRunning() {
        return keepRunning;
    }

    public void setKeepRunning(boolean keepRunning) {
        this.keepRunning = keepRunning;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void reduce() {
        total = total - 1;
    }
}
