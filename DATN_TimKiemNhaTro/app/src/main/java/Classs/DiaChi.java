package Classs;

import java.io.Serializable;

/**
 * Created by Windows 10 TIMT on 21-Oct-17.
 */

public class DiaChi implements Serializable {
    private String ThanhPho;
    private String Quan;
    private String Phuong;
    private String Duong;
    private String SoNha;

    public DiaChi() {
    }

    public DiaChi(String thanhPho, String quan, String phuong, String duong, String soNha) {
        ThanhPho = thanhPho;
        Quan = quan;
        Phuong = phuong;
        Duong = duong;
        SoNha = soNha;
    }

    public String getThanhPho() {
        return ThanhPho;
    }

    public void setThanhPho(String thanhPho) {
        ThanhPho = thanhPho;
    }

    public String getQuan() {
        return Quan;
    }

    public void setQuan(String quan) {
        Quan = quan;
    }

    public String getPhuong() {
        return Phuong;
    }

    public void setPhuong(String phuong) {
        Phuong = phuong;
    }

    public String getDuong() {
        return Duong;
    }

    public void setDuong(String duong) {
        Duong = duong;
    }

    public String getSoNha() {
        return SoNha;
    }

    public void setSoNha(String soNha) {
        SoNha = soNha;
    }
}

