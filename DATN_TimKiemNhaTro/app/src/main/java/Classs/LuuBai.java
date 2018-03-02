package Classs;

import java.io.Serializable;

/**
 * Created by Windows 10 TIMT on 12-Nov-17.
 */

public class LuuBai implements Serializable {
    private String idNha;

    public LuuBai(String idNha) {
        this.idNha = idNha;
    }

    public LuuBai() {
    }

    public String getIdNha() {
        return idNha;
    }

    public void setIdNha(String idNha) {
        this.idNha = idNha;
    }
}
