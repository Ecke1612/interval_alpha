package object;

import java.io.Serializable;

/**
 * Created by Eike on 04.07.2017.
 */
public class ConfigObject implements Serializable {

    private boolean doUpdate = true;
    private boolean multiClock = false;
    private boolean autostop = true;

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
}
