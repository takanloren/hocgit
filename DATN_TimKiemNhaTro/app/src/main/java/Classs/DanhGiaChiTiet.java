package Classs;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Windows 10 TIMT on 07-Nov-17.
 */

public class DanhGiaChiTiet implements Serializable {
    private String id;
    private float Diem;
    private Date NgayDang;
    private User user;
    private String idNha;

    public DanhGiaChiTiet() {
    }

    public DanhGiaChiTiet(String id,float diem, Date ngayDang, User user,String idNha) {
        this.id = id;
        Diem = diem;
        NgayDang = ngayDang;
        this.user = user;
        this.idNha=idNha;
    }

    public String getIdNha() {
        return idNha;
    }

    public void setIdNha(String idNha) {
        this.idNha = idNha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getDiem() {
        return Diem;
    }

    public void setDiem(float diem) {
        Diem = diem;
    }

    public Date getNgayDang() {
        return NgayDang;
    }

    public void setNgayDang(Date ngayDang) {
        NgayDang = ngayDang;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
