package Classs;

import android.net.Uri;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Windows 10 TIMT on 07-Nov-17.
 */

public class NhaChiTiet implements Serializable {
    private String id;
    private Date NgayNhan;
    private int TienCoc;
    private int DienTich;
    private int SoLuongPhong;
    private int GiaThue;
    private TienIchNCT TienIch;
    private YeuCauNCT YeuCau;
    private String idNha;

    public NhaChiTiet() {
    }

    public NhaChiTiet(Date ngayNhan, int tienCoc, int dienTich, int soLuongPhong, int giaThue, TienIchNCT tienIch, YeuCauNCT yeuCau, String idNha) {
        NgayNhan = ngayNhan;
        TienCoc = tienCoc;
        DienTich = dienTich;
        SoLuongPhong = soLuongPhong;
        GiaThue = giaThue;
        TienIch = tienIch;
        YeuCau = yeuCau;
        this.idNha = idNha;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getNgayNhan() {
        return NgayNhan;
    }

    public void setNgayNhan(Date ngayNhan) {
        NgayNhan = ngayNhan;
    }

    public int getTienCoc() {
        return TienCoc;
    }

    public void setTienCoc(int tienCoc) {
        TienCoc = tienCoc;
    }

    public int getDienTich() {
        return DienTich;
    }

    public void setDienTich(int dienTich) {
        DienTich = dienTich;
    }

    public int getSoLuongPhong() {
        return SoLuongPhong;
    }

    public void setSoLuongPhong(int soLuongPhong) {
        SoLuongPhong = soLuongPhong;
    }

    public int getGiaThue() {
        return GiaThue;
    }

    public void setGiaThue(int giaThue) {
        GiaThue = giaThue;
    }

    public TienIchNCT getTienIch() {
        return TienIch;
    }

    public void setTienIch(TienIchNCT tienIch) {
        TienIch = tienIch;
    }

    public YeuCauNCT getYeuCau() {
        return YeuCau;
    }

    public void setYeuCau(YeuCauNCT yeuCau) {
        YeuCau = yeuCau;
    }

    public String getIdNha() {
        return idNha;
    }

    public void setIdNha(String idNha) {
        this.idNha = idNha;
    }
}
