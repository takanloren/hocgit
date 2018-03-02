package Classs;

import java.io.Serializable;

/**
 * Created by Windows 10 TIMT on 21-Oct-17.
 */

public class User implements Serializable {
    private String MatKhau;
    private String HoTen;
    private String SoDienThoai;
    private String Email;
    private DiaChi DiaChi;
    private boolean GioiTinh;
    private boolean TrangThai;
    private boolean PhanQuyen;
    private String HinhAnh;
    private boolean DaDKTin=false;

    public User() {
    }

    public User(String hoTen, String soDienThoai, String email, DiaChi diaChi, boolean gioiTinh, boolean trangThai, boolean phanQuyen, String hinhAnh) {
        HoTen = hoTen;
        SoDienThoai = soDienThoai;
        Email = email;
        DiaChi = diaChi;
        GioiTinh = gioiTinh;
        TrangThai = trangThai;
        PhanQuyen = phanQuyen;
        HinhAnh = hinhAnh;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String matKhau) {
        MatKhau = matKhau;
    }

    public boolean isDaDKTin() {
        return DaDKTin;
    }

    public void setDaDKTin(boolean daDKTin) {
        DaDKTin = daDKTin;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getSoDienThoai() {
        return SoDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        SoDienThoai = soDienThoai;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public DiaChi getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(DiaChi diaChi) {
        DiaChi = diaChi;
    }

    public boolean isGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        GioiTinh = gioiTinh;
    }

    public boolean isTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(boolean trangThai) {
        TrangThai = trangThai;
    }

    public boolean isPhanQuyen() {
        return PhanQuyen;
    }

    public void setPhanQuyen(boolean phanQuyen) {
        PhanQuyen = phanQuyen;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }
}
