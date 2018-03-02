package Classs;

import java.io.BufferedOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Windows 10 TIMT on 07-Nov-17.
 */

public class Nha implements Serializable{
    private String id;
    private String MoTa;
    private String SoDienThoai;
    private int SoLuongPhong;
    private Date NgayDang;
    private Boolean ChungChu;
    private String HinhAnh;
    private Boolean TrangThai;
    private DiaChi DiaChi;
    private User User;
    private GiaTien GiaTien;
    private String LoaiNha;
    private DanhGia DanhGia;
    private int GiaThapNhat;
    private boolean daluu=false;
    private double Latitude;
    private double Longitude;
    public Nha() {
    }

    public Nha(String moTa, String soDienThoai, int soLuongPhong, Date ngayDang, Boolean chungChu, String hinhAnh, Boolean trangThai, Classs.DiaChi diaChi, Classs.User user, Classs.GiaTien giaTien, String loaiNha, Classs.DanhGia danhGia, int giaThapNhat, boolean daluu, double latitude, double longitude) {
        MoTa = moTa;
        SoDienThoai = soDienThoai;
        SoLuongPhong = soLuongPhong;
        NgayDang = ngayDang;
        ChungChu = chungChu;
        HinhAnh = hinhAnh;
        TrangThai = trangThai;
        DiaChi = diaChi;
        User = user;
        GiaTien = giaTien;
        LoaiNha = loaiNha;
        DanhGia = danhGia;
        GiaThapNhat = giaThapNhat;
        this.daluu = daluu;
        Latitude = latitude;
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public boolean isDaluu() {
        return daluu;
    }

    public void setDaluu(boolean daluu) {
        this.daluu = daluu;
    }

    public int getGiaThapNhat() {
        return GiaThapNhat;
    }

    public void setGiaThapNhat(int giaThapNhat) {
        GiaThapNhat = giaThapNhat;
    }

    public Classs.DanhGia getDanhGia() {
        return DanhGia;
    }

    public void setDanhGia(Classs.DanhGia danhGia) {
        DanhGia = danhGia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }

    public String getSoDienThoai() {
        return SoDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        SoDienThoai = soDienThoai;
    }

    public int getSoLuongPhong() {
        return SoLuongPhong;
    }

    public void setSoLuongPhong(int soLuongPhong) {
        SoLuongPhong = soLuongPhong;
    }

    public Date getNgayDang() {
        return NgayDang;
    }

    public void setNgayDang(Date ngayDang) {
        NgayDang = ngayDang;
    }

    public Boolean getChungChu() {
        return ChungChu;
    }

    public void setChungChu(Boolean chungChu) {
        ChungChu = chungChu;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }

    public Boolean getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        TrangThai = trangThai;
    }

    public Classs.DiaChi getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(Classs.DiaChi diaChi) {
        DiaChi = diaChi;
    }

    public Classs.User getUser() {
        return User;
    }

    public void setUser(Classs.User user) {
        User = user;
    }

    public Classs.GiaTien getGiaTien() {
        return GiaTien;
    }

    public void setGiaTien(Classs.GiaTien giaTien) {
        GiaTien = giaTien;
    }

    public String getLoaiNha() {
        return LoaiNha;
    }

    public void setLoaiNha(String loaiNha) {
        LoaiNha = loaiNha;
    }
}
