package object;

import java.io.Serializable;

/**
 * Created by Eike on 04.07.2017.
 */
public class ConfigObject implements Serializable {

    private boolean doUpdate = true;
    private boolean multiClock = false;
    private boolean autostop = true;
    private int autostopinterval = 15;
    private int autostopMinTime = 1;
    private int autostopRushHour = 18;
    private int cssIndex = 0;

    public boolean isDoUpdate() {
        return doUpdate;
    }

    public void setDoUpdate(boolean doUpdate) {
        this.doUpdate = doUpdate;
    }

    public boolean isMultiClock() {
        return multiClock;
    }

    public void setMultiClock(boolean multiClock) {
        this.multiClock = multiClock;
    }

    public boolean isAutostop() {
        return autostop;
    }

    public void setAutostop(boolean autostop) {
        this.autostop = autostop;
    }

    public int getAutostopinterval() {
        return autostopinterval;
    }

    public void setAutostopinterval(int autostopinterval) {
        this.autostopinterval = autostopinterval;
    }

    public int getAutostopMinTime() {
        return autostopMinTime;
    }

    public void setAutostopMinTime(int autostopMinTime) {
        this.autostopMinTime = autostopMinTime;
    }

    public int getAutostopRushHour() {
        return autostopRushHour;
    }

    public void setAutostopRushHour(int autostopRushHour) {
        this.autostopRushHour = autostopRushHour;
    }

    public int getCssIndex() {
        return cssIndex;
    }

    public void setCssIndex(int cssIndex) {
        this.cssIndex = cssIndex;
    }
}
