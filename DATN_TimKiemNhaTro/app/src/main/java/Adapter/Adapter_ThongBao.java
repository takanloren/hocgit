package Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tungha.datn_timkiemnhatro.ChiTietBaiDang_ThongBao;
import com.tungha.datn_timkiemnhatro.FormXemTinDaDK;
import com.tungha.datn_timkiemnhatro.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Classs.ThongBao;
import Classs.TinDKTB;

import static com.tungha.datn_timkiemnhatro.MainActivity.db;
import static com.tungha.datn_timkiemnhatro.TrangChinh.arrNha;

/**
 * Created by Windows 10 TIMT on 08-Nov-17.
 */

public class Adapter_ThongBao extends RecyclerView.Adapter<Adapter_ThongBao.ViewHolder>{
    ArrayList<ThongBao> arrTB ;
    Context context;
    boolean check_nha_ton_tai=false;
    ThongBao TB;
    String ngaydang;
    int vitriclick;
    SimpleDateFormat iftoday = new SimpleDateFormat("'Hôm nay, lúc 'HH:mm");
    SimpleDateFormat ifinyear = new SimpleDateFormat("dd' tháng 'MM' lúc 'HH:mm");
    SimpleDateFormat ifpastyear = new SimpleDateFormat("dd' tháng 'MM' năm 'yyyy' lúc 'HH:mm");
    ///////
    SimpleDateFormat today = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat year = new SimpleDateFormat("yyyy");
    public Adapter_ThongBao(ArrayList<ThongBao> arrTB, Context context) {
        this.arrTB = arrTB;
        this.context = context;
    }
    @Override
    public Adapter_ThongBao.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_thongbao,parent,false);
        return new Adapter_ThongBao.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
         holder.txtnoidung.setText(arrTB.get(position).getNoiDungTB());
        String layngaycuanha = today.format(arrTB.get(position).getNgayGuiTB());
        String laynamcuanha = year.format(arrTB.get(position).getNgayGuiTB());
        ///////
        Date date_today = new Date();
        String ngayhientai = today.format(date_today);
        String namhientai = year.format(date_today);
        if(layngaycuanha.compareTo(ngayhientai)==0){
            ngaydang = iftoday.format(arrTB.get(position).getNgayGuiTB());
        }else if(laynamcuanha.compareTo(namhientai)==0){
            ngaydang=ifinyear.format(arrTB.get(position).getNgayGuiTB());
        }else {
            ngaydang=ifpastyear.format(arrTB.get(position).getNgayGuiTB());
        }
        holder.txtngay.setText(ngaydang);
        if(arrTB.get(position).isTrangthai()==false){
            holder.txttrangthai.setText("Chưa xem");
        }else
            holder.txttrangthai.setText("Đã xem");
    }
    private void UpdateTrangThaiTB(ThongBao tb){
        db.collection("DSThongBao").document(tb.getId()).update("trangthai",true);
    }
    private void XemNha(){
        Intent i = new Intent(context,ChiTietBaiDang_ThongBao.class);
        Bundle b= new Bundle();
        b.putString("idnct",TB.getIdNhaChiTiet());
        b.putString("idnha",TB.getIdNha());
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("bundle",b);
        context.startActivity(i);
    }
    private void XoaThongBao(String id) {
        this.notifyDataSetChanged();
        db.collection("DSThongBao").document(id).delete();
    }
    private boolean KiemTraTrangThaiNha(){
        for(int i=0;i<arrNha.size();i++){
            if(arrNha.get(i).getId().compareTo(TB.getIdNha())==0)
                if(arrNha.get(i).getTrangThai()==false){
                    return false;
                }
        }
        return true;
    }
    public void ThongBao(String thongbao){
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle("Thông báo");
        b.setMessage(thongbao);
        b.setPositiveButton("Biết rồi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.setNegativeButton("Xóa thông báo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                arrTB.remove(vitriclick);
                XoaThongBao(TB.getId());
            }
        });
        b.create().show();
    }
    @Override
    public int getItemCount() {
        return arrTB.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnCreateContextMenuListener{
        TextView txtnoidung,txtngay,txttrangthai;
        public ViewHolder(View itemView) {
            super(itemView);
            txtnoidung= (TextView) itemView.findViewById(R.id.txtNoiDung_itemTB);
            txtngay= (TextView) itemView.findViewById(R.id.txtNgayNhanTB_itemTB);
            txttrangthai= (TextView) itemView.findViewById(R.id.txtTrangThai_itemTB);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check_nha_ton_tai=false;
                    TB = arrTB.get(getAdapterPosition());
                    vitriclick = getAdapterPosition();
                    if(KiemTraTrangThaiNha()==true) {
                        for (int i = 0; i < arrNha.size(); i++) {
                            if (arrNha.get(i).getId().compareTo(TB.getIdNha()) == 0) {
                                check_nha_ton_tai = true;
                                break;
                            }
                        }
                        if (check_nha_ton_tai == true) {
                            if (TB.isTrangthai() == false) {
                                UpdateTrangThaiTB(TB);
                            }
                            XemNha();
                        } else
                            ThongBao("Bài đăng đã bị xóa , không thể xem!");
                    }else {
                        ThongBao("Bài đăng này đã vi phạm quy định và đang bị khóa");
                    }

                }
            });
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    menu.add(Menu.NONE,0,Menu.NONE,"Xóa").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            TB = arrTB.get(getAdapterPosition());
                            arrTB.remove(getAdapterPosition());
                            XoaThongBao(TB.getId());
                            return false;
                        }
                    });
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }


}
