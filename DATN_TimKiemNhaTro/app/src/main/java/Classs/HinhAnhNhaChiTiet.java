package Classs;

import java.io.Serializable;

/**
 * Created by Windows 10 TIMT on 07-Nov-17.
 */

public class HinhAnhNhaChiTiet implements Serializable {
    private String id;
    private String idNhaChiTiet;
    private String HinhAnh;

    public HinhAnhNhaChiTiet() {
    }

    public HinhAnhNhaChiTiet(String idNhaChiTiet, String hinhAnh) {
        this.idNhaChiTiet = idNhaChiTiet;
        HinhAnh = hinhAnh;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdNhaChiTiet() {
        return idNhaChiTiet;
    }

    public void setIdNhaChiTiet(String idNhaChiTiet) {
        this.idNhaChiTiet = idNhaChiTiet;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }
}
