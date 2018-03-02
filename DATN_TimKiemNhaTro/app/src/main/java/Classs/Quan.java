package Classs;

import java.io.Serializable;

/**
 * Created by Windows 10 TIMT on 28-Oct-17.
 */

public class Quan implements Serializable {
    private String quan;

    public Quan(String quan) {
        this.quan = quan;
    }

    public Quan() {
    }

    public String getQuan() {
        return quan;
    }

    public void setQuan(String quan) {
        this.quan = quan;
    }
}
