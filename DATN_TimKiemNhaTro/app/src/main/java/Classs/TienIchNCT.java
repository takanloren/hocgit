package Classs;


import java.io.Serializable;

/**
 * Created by Windows 10 TIMT on 07-Nov-17.
 */

public class TienIchNCT implements Serializable {
    private String id;
    private Boolean BepNauAn;
    private Boolean NhaVSRieng;
    private Boolean ChoDeXe;
    private Boolean BaoVe;
    private Boolean MayLanh;
    private Boolean MayGiat;
    private Boolean SanPhoiDo;
    private Boolean SanThuong;
    private Boolean TruyenHinhCap;
    private Boolean Wifi;

    public TienIchNCT() {
    }

    public TienIchNCT(Boolean bepNauAn, Boolean nhaVSRieng, Boolean choDeXe, Boolean baoVe, Boolean mayLanh, Boolean mayGiat, Boolean sanPhoiDo, Boolean truyenHinhCap, Boolean wifi,Boolean sanThuong) {
        BepNauAn = bepNauAn;
        NhaVSRieng = nhaVSRieng;
        ChoDeXe = choDeXe;
        BaoVe = baoVe;
        MayLanh = mayLanh;
        MayGiat = mayGiat;
        SanPhoiDo = sanPhoiDo;
        TruyenHinhCap = truyenHinhCap;
        Wifi = wifi;
        SanThuong=sanThuong;
    }

    public Boolean getSanThuong() {
        return SanThuong;
    }

    public void setSanThuong(Boolean sanThuong) {
        SanThuong = sanThuong;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getBepNauAn() {
        return BepNauAn;
    }

    public void setBepNauAn(Boolean bepNauAn) {
        BepNauAn = bepNauAn;
    }

    public Boolean getNhaVSRieng() {
        return NhaVSRieng;
    }

    public void setNhaVSRieng(Boolean nhaVSRieng) {
        NhaVSRieng = nhaVSRieng;
    }

    public Boolean getChoDeXe() {
        return ChoDeXe;
    }

    public void setChoDeXe(Boolean choDeXe) {
        ChoDeXe = choDeXe;
    }

    public Boolean getBaoVe() {
        return BaoVe;
    }

    public void setBaoVe(Boolean baoVe) {
        BaoVe = baoVe;
    }

    public Boolean getMayLanh() {
        return MayLanh;
    }

    public void setMayLanh(Boolean mayLanh) {
        MayLanh = mayLanh;
    }

    public Boolean getMayGiat() {
        return MayGiat;
    }

    public void setMayGiat(Boolean mayGiat) {
        MayGiat = mayGiat;
    }

    public Boolean getSanPhoiDo() {
        return SanPhoiDo;
    }

    public void setSanPhoiDo(Boolean sanPhoiDo) {
        SanPhoiDo = sanPhoiDo;
    }

    public Boolean getTruyenHinhCap() {
        return TruyenHinhCap;
    }

    public void setTruyenHinhCap(Boolean truyenHinhCap) {
        TruyenHinhCap = truyenHinhCap;
    }

    public Boolean getWifi() {
        return Wifi;
    }

    public void setWifi(Boolean wifi) {
        Wifi = wifi;
    }
}
