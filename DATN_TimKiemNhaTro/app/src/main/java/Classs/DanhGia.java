package Classs;

import java.io.Serializable;

/**
 * Created by Windows 10 TIMT on 07-Nov-17.
 */

public class DanhGia implements Serializable {
    private String id;
    private int LuotDanhGia;
    private float DiemTrungBinh;
    private String idNha;

    public DanhGia() {
    }

    public DanhGia(int luotDanhGia, float diemTrungBinh, String idNha) {
        LuotDanhGia = luotDanhGia;
        DiemTrungBinh = diemTrungBinh;
        this.idNha = idNha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLuotDanhGia() {
        return LuotDanhGia;
    }

    public void setLuotDanhGia(int luotDanhGia) {
        LuotDanhGia = luotDanhGia;
    }

    public float getDiemTrungBinh() {
        return DiemTrungBinh;
    }

    public void setDiemTrungBinh(float diemTrungBinh) {
        DiemTrungBinh = diemTrungBinh;
    }

    public String getIdNha() {
        return idNha;
    }

    public void setIdNha(String idNha) {
        this.idNha = idNha;
    }
}
