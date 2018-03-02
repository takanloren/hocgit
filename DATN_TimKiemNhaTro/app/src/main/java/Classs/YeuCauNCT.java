package Classs;

import java.io.Serializable;

/**
 * Created by Windows 10 TIMT on 07-Nov-17.
 */

public class YeuCauNCT implements Serializable {
    private String id;
    private int SoLuongNguoi;
    private int SoLuongXe;
    private String GioGiac;
    private Boolean ChoNauAn;
    private int HopDong;

    public YeuCauNCT() {
    }

    public YeuCauNCT(int soLuongNguoi, int soLuongXe, String gioGiac, Boolean choNauAn, int hopDong) {
        SoLuongNguoi = soLuongNguoi;
        SoLuongXe = soLuongXe;
        GioGiac = gioGiac;
        ChoNauAn = choNauAn;
        HopDong = hopDong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSoLuongNguoi() {
        return SoLuongNguoi;
    }

    public void setSoLuongNguoi(int soLuongNguoi) {
        SoLuongNguoi = soLuongNguoi;
    }

    public int getSoLuongXe() {
        return SoLuongXe;
    }

    public void setSoLuongXe(int soLuongXe) {
        SoLuongXe = soLuongXe;
    }

    public String getGioGiac() {
        return GioGiac;
    }

    public void setGioGiac(String gioGiac) {
        GioGiac = gioGiac;
    }

    public Boolean getChoNauAn() {
        return ChoNauAn;
    }

    public void setChoNauAn(Boolean choNauAn) {
        ChoNauAn = choNauAn;
    }

    public int getHopDong() {
        return HopDong;
    }

    public void setHopDong(int hopDong) {
        HopDong = hopDong;
    }
}
