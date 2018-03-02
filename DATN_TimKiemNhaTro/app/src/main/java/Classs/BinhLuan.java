package Classs;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Windows 10 TIMT on 07-Nov-17.
 */

public class BinhLuan implements Serializable{
    private String id;
    private Date NgayDang;
    private String NoiDung;
    private boolean TrangThai;
    private User user;
    private String idNha;

    public BinhLuan() {
    }

    public BinhLuan(String id,Date ngayDang, String noiDung, boolean trangThai, User user, String idNha) {
        this.id=id;
        NgayDang = ngayDang;
        NoiDung = noiDung;
        TrangThai = trangThai;
        this.user = user;
        this.idNha = idNha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getNgayDang() {
        return NgayDang;
    }

    public void setNgayDang(Date ngayDang) {
        NgayDang = ngayDang;
    }

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        NoiDung = noiDung;
    }

    public boolean isTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(boolean trangThai) {
        TrangThai = trangThai;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getIdNha() {
        return idNha;
    }

    public void setIdNha(String idNha) {
        this.idNha = idNha;
    }
}
