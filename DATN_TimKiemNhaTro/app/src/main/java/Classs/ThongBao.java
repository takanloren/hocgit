package Classs;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Windows 10 TIMT on 25-Nov-17.
 */

public class ThongBao implements Serializable {
    private String id;
    private String idNha;
    private String idNhaChiTiet;
    private String idThongBao;
    private String EmailUser;
    private Date ngayGuiTB;
    private String noiDungTB;
    private boolean trangthai;

    public ThongBao() {
    }

    public ThongBao(String id, String idNha, String idNhaChiTiet, String emailUser, Date ngayGuiTB, String noiDungTB, boolean trangthai,String idThongBao) {
        this.id = id;
        this.idNha = idNha;
        this.idNhaChiTiet = idNhaChiTiet;
        this.idThongBao=idThongBao;
        EmailUser = emailUser;
        this.ngayGuiTB = ngayGuiTB;
        this.noiDungTB = noiDungTB;
        this.trangthai = trangthai;
    }

    public String getIdThongBao() {
        return idThongBao;
    }

    public void setIdThongBao(String idThongBao) {
        this.idThongBao = idThongBao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdNha() {
        return idNha;
    }

    public void setIdNha(String idNha) {
        this.idNha = idNha;
    }

    public String getIdNhaChiTiet() {
        return idNhaChiTiet;
    }

    public void setIdNhaChiTiet(String idNhaChiTiet) {
        this.idNhaChiTiet = idNhaChiTiet;
    }

    public String getEmailUser() {
        return EmailUser;
    }

    public void setEmailUser(String emailUser) {
        EmailUser = emailUser;
    }

    public Date getNgayGuiTB() {
        return ngayGuiTB;
    }

    public void setNgayGuiTB(Date ngayGuiTB) {
        this.ngayGuiTB = ngayGuiTB;
    }

    public String getNoiDungTB() {
        return noiDungTB;
    }

    public void setNoiDungTB(String noiDungTB) {
        this.noiDungTB = noiDungTB;
    }

    public boolean isTrangthai() {
        return trangthai;
    }

    public void setTrangthai(boolean trangthai) {
        this.trangthai = trangthai;
    }
}
