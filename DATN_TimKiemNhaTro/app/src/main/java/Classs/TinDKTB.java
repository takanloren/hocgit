package Classs;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Windows 10 TIMT on 24-Nov-17.
 */

public class TinDKTB implements Serializable{
    private String id;
    private String Mailuser;
    private Date ngaydktin;
    private String TP;
    private String Quan;
    private String Duong;
    private int giatu;
    private int giaden;
    private int dttu;
    private int dtden;
    private int sophong;
    private String loaiphong;
    private Date ngaynhan;
    private int songuoi;
    private int soxe;
    private int hd;
    private boolean chungchu;
    private boolean nauan;
    private boolean san;
    private boolean santhuong;
    private boolean beprieng;
    private boolean toiletrieng;
    private boolean tv;
    private boolean wifi;
    private boolean maylanh;
    private boolean maygiat;
    private boolean chodauxe;
    private boolean anninh;
    private boolean status;
    private boolean statususer;

    public TinDKTB(String id, String mailuser, Date ngaydktin, String TP, String quan, String duong, int giatu, int giaden, int dttu, int dtden, int sophong, String loaiphong, Date ngaynhan, int songuoi, int soxe, int hd, boolean chungchu, boolean nauan, boolean san, boolean santhuong, boolean beprieng, boolean toiletrieng, boolean tv, boolean wifi, boolean maylanh, boolean maygiat, boolean chodauxe, boolean anninh,boolean status) {
        this.id = id;
        Mailuser = mailuser;
        this.ngaydktin = ngaydktin;
        this.TP = TP;
        Quan = quan;
        Duong = duong;
        this.giatu = giatu;
        this.giaden = giaden;
        this.dttu = dttu;
        this.dtden = dtden;
        this.sophong = sophong;
        this.loaiphong = loaiphong;
        this.ngaynhan = ngaynhan;
        this.songuoi = songuoi;
        this.soxe = soxe;
        this.hd = hd;
        this.chungchu = chungchu;
        this.nauan = nauan;
        this.san = san;
        this.santhuong = santhuong;
        this.beprieng = beprieng;
        this.toiletrieng = toiletrieng;
        this.tv = tv;
        this.wifi = wifi;
        this.maylanh = maylanh;
        this.maygiat = maygiat;
        this.chodauxe = chodauxe;
        this.anninh = anninh;
        this.status=status;
    }

    public TinDKTB() {
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMailuser() {
        return Mailuser;
    }

    public void setMailuser(String mailuser) {
        Mailuser = mailuser;
    }

    public Date getNgaydktin() {
        return ngaydktin;
    }

    public void setNgaydktin(Date ngaydktin) {
        this.ngaydktin = ngaydktin;
    }

    public String getTP() {
        return TP;
    }

    public void setTP(String TP) {
        this.TP = TP;
    }

    public String getQuan() {
        return Quan;
    }

    public void setQuan(String quan) {
        Quan = quan;
    }

    public String getDuong() {
        return Duong;
    }

    public void setDuong(String duong) {
        Duong = duong;
    }

    public int getGiatu() {
        return giatu;
    }

    public void setGiatu(int giatu) {
        this.giatu = giatu;
    }

    public int getGiaden() {
        return giaden;
    }

    public void setGiaden(int giaden) {
        this.giaden = giaden;
    }

    public int getDttu() {
        return dttu;
    }

    public void setDttu(int dttu) {
        this.dttu = dttu;
    }

    public int getDtden() {
        return dtden;
    }

    public void setDtden(int dtden) {
        this.dtden = dtden;
    }

    public int getSophong() {
        return sophong;
    }

    public void setSophong(int sophong) {
        this.sophong = sophong;
    }

    public String getLoaiphong() {
        return loaiphong;
    }

    public void setLoaiphong(String loaiphong) {
        this.loaiphong = loaiphong;
    }

    public Date getNgaynhan() {
        return ngaynhan;
    }

    public void setNgaynhan(Date ngaynhan) {
        this.ngaynhan = ngaynhan;
    }

    public int getSonguoi() {
        return songuoi;
    }

    public void setSonguoi(int songuoi) {
        this.songuoi = songuoi;
    }

    public int getSoxe() {
        return soxe;
    }

    public void setSoxe(int soxe) {
        this.soxe = soxe;
    }

    public int getHd() {
        return hd;
    }

    public void setHd(int hd) {
        this.hd = hd;
    }

    public boolean isChungchu() {
        return chungchu;
    }

    public void setChungchu(boolean chungchu) {
        this.chungchu = chungchu;
    }

    public boolean isNauan() {
        return nauan;
    }

    public void setNauan(boolean nauan) {
        this.nauan = nauan;
    }

    public boolean isSan() {
        return san;
    }

    public void setSan(boolean san) {
        this.san = san;
    }

    public boolean isSanthuong() {
        return santhuong;
    }

    public void setSanthuong(boolean santhuong) {
        this.santhuong = santhuong;
    }

    public boolean isBeprieng() {
        return beprieng;
    }

    public void setBeprieng(boolean beprieng) {
        this.beprieng = beprieng;
    }

    public boolean isToiletrieng() {
        return toiletrieng;
    }

    public void setToiletrieng(boolean toiletrieng) {
        this.toiletrieng = toiletrieng;
    }

    public boolean isTv() {
        return tv;
    }

    public void setTv(boolean tv) {
        this.tv = tv;
    }

    public boolean isWifi() {
        return wifi;
    }

    public void setWifi(boolean wifi) {
        this.wifi = wifi;
    }

    public boolean isMaylanh() {
        return maylanh;
    }

    public void setMaylanh(boolean maylanh) {
        this.maylanh = maylanh;
    }

    public boolean isMaygiat() {
        return maygiat;
    }

    public void setMaygiat(boolean maygiat) {
        this.maygiat = maygiat;
    }

    public boolean isChodauxe() {
        return chodauxe;
    }

    public void setChodauxe(boolean chodauxe) {
        this.chodauxe = chodauxe;
    }

    public boolean isAnninh() {
        return anninh;
    }

    public void setAnninh(boolean anninh) {
        this.anninh = anninh;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        return super.equals(obj);
    }
}
