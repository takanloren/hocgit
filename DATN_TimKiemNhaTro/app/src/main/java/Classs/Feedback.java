package Classs;

import java.util.Date;

/**
 * Created by Windows 10 TIMT on 21-Nov-17.
 */

public class Feedback {
    private String id;
    private String Email;
    private String HoTen;
    private String ChuDe;
    private String NoiDung;
    private Date NgayDang;
    private boolean TrangThaiUser;
    private boolean TrangThaiAdmin;

    public Feedback(String id, String email, String hoTen, String chuDe, String noiDung, Date ngayDang, boolean trangThaiUser, boolean trangThaiAdmin) {
        this.id = id;
        Email = email;
        HoTen = hoTen;
        ChuDe = chuDe;
        NoiDung = noiDung;
        NgayDang = ngayDang;
        TrangThaiUser = trangThaiUser;
        TrangThaiAdmin = trangThaiAdmin;
    }

    public Feedback() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getChuDe() {
        return ChuDe;
    }

    public void setChuDe(String chuDe) {
        ChuDe = chuDe;
    }

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        NoiDung = noiDung;
    }

    public Date getNgayDang() {
        return NgayDang;
    }

    public void setNgayDang(Date ngayDang) {
        NgayDang = ngayDang;
    }

    public boolean isTrangThaiUser() {
        return TrangThaiUser;
    }

    public void setTrangThaiUser(boolean trangThaiUser) {
        TrangThaiUser = trangThaiUser;
    }

    public boolean isTrangThaiAdmin() {
        return TrangThaiAdmin;
    }

    public void setTrangThaiAdmin(boolean trangThaiAdmin) {
        TrangThaiAdmin = trangThaiAdmin;
    }
}
