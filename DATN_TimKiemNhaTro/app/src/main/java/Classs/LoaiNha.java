package Classs;

import java.io.Serializable;

/**
 * Created by Windows 10 TIMT on 07-Nov-17.
 */

public class LoaiNha implements Serializable {
    private String TenLoai;

    public LoaiNha() {
    }

    public LoaiNha(String tenLoai) {
        TenLoai = tenLoai;
    }

    public String getTenLoai() {
        return TenLoai;
    }

    public void setTenLoai(String tenLoai) {
        TenLoai = tenLoai;
    }
}
