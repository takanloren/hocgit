package Classs;

import java.io.Serializable;

/**
 * Created by Windows 10 TIMT on 07-Nov-17.
 */

public class GiaTien implements Serializable{
    private String id;
    private int TienNuoc;
    private int TienDien;
    private int TienDauXe;
    private int TienDonDep;
    private int TienWifi;
    private int TienTV;

    public GiaTien() {
    }

    public GiaTien(int tienNuoc, int tienDien, int tienDauXe, int tienDonDep, int tienWifi, int tienTV) {
        this.id = id;
        TienNuoc = tienNuoc;
        TienDien = tienDien;
        TienDauXe = tienDauXe;
        TienDonDep = tienDonDep;
        TienWifi = tienWifi;
        TienTV = tienTV;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTienNuoc() {
        return TienNuoc;
    }

    public void setTienNuoc(int tienNuoc) {
        TienNuoc = tienNuoc;
    }

    public int getTienDien() {
        return TienDien;
    }

    public void setTienDien(int tienDien) {
        TienDien = tienDien;
    }

    public int getTienDauXe() {
        return TienDauXe;
    }

    public void setTienDauXe(int tienDauXe) {
        TienDauXe = tienDauXe;
    }

    public int getTienDonDep() {
        return TienDonDep;
    }

    public void setTienDonDep(int tienDonDep) {
        TienDonDep = tienDonDep;
    }

    public int getTienWifi() {
        return TienWifi;
    }

    public void setTienWifi(int tienWifi) {
        TienWifi = tienWifi;
    }

    public int getTienTV() {
        return TienTV;
    }

    public void setTienTV(int tienTV) {
        TienTV = tienTV;
    }
}
