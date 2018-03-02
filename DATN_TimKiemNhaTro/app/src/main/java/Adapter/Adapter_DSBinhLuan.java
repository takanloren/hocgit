package Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.tungha.datn_timkiemnhatro.ChiTietBaiDang;
import com.tungha.datn_timkiemnhatro.R;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Classs.BinhLuan;
import Classs.LuuBai;
import Classs.Nha;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.tungha.datn_timkiemnhatro.MainActivity.db;
import static com.tungha.datn_timkiemnhatro.MainActivity.mAuth;

import static com.tungha.datn_timkiemnhatro.TrangChinh.switchTabs;

/**
 * Created by Windows 10 TIMT on 08-Nov-17.
 */

public class Adapter_DSBinhLuan extends RecyclerView.Adapter<Adapter_DSBinhLuan.ViewHolder> {
    ArrayList<BinhLuan> arrBL ;
    Context context;
    BinhLuan bl;
    String ngaydang,so,duong,phuong,quan,tinh;
    FirebaseUser user = mAuth.getCurrentUser();

    SimpleDateFormat iftoday = new SimpleDateFormat("'Hôm nay, lúc 'HH:mm");
    SimpleDateFormat ifinyear = new SimpleDateFormat("dd' tháng 'MM' lúc 'HH:mm");
    SimpleDateFormat ifpastyear = new SimpleDateFormat("dd'/'MM'/'yyyy', lúc 'HH:mm");
    ///////
    SimpleDateFormat today = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat year = new SimpleDateFormat("yyyy");
    public Adapter_DSBinhLuan(ArrayList<BinhLuan> arrBL, Context context) {
        this.arrBL = arrBL;
        this.context = context;
    }
    @Override
    public Adapter_DSBinhLuan.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_bl,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Adapter_DSBinhLuan.ViewHolder holder, int position) {
        if(arrBL.get(position).isTrangThai()==false){
            holder.cmt.setVisibility(View.GONE);
        }
        Glide.with(context)
                .load(arrBL.get(position).getUser().getHinhAnh())
                .into(holder.imgHinhUser);
        holder.txtTenUser.setText(arrBL.get(position).getUser().getHoTen());

        ///////
        String layngaycuanha = today.format(arrBL.get(position).getNgayDang());
        String laynamcuanha = year.format(arrBL.get(position).getNgayDang());
        ///////
        Date date_today = new Date();
        String ngayhientai = today.format(date_today);
        String namhientai = year.format(date_today);
        if(layngaycuanha.compareTo(ngayhientai)==0){
            ngaydang = iftoday.format(arrBL.get(position).getNgayDang());
        }else if(laynamcuanha.compareTo(namhientai)==0){
            ngaydang=ifinyear.format(arrBL.get(position).getNgayDang());
        }else {
            ngaydang=ifpastyear.format(arrBL.get(position).getNgayDang());
        }
        holder.txtNgayDang.setText(ngaydang);
        holder.txtNoiDung.setText(arrBL.get(position).getNoiDung());


    }

    @Override
    public int getItemCount() {
        return arrBL.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imgHinhUser;
        TextView txtTenUser,txtNgayDang,txtNoiDung;
        LinearLayout cmt;
        public ViewHolder(View itemView) {
            super(itemView);
            imgHinhUser = (CircleImageView) itemView.findViewById(R.id.imgAvatar_BL);
            txtTenUser = (TextView) itemView.findViewById(R.id.txtUser_BL);
            txtNgayDang = (TextView) itemView.findViewById(R.id.txtNgayDang_BL);
            txtNoiDung = (TextView) itemView.findViewById(R.id.txtNoiDungBL_BL);
            cmt= (LinearLayout) itemView.findViewById(R.id.binhluancuauser_BL);
        }
    }
}
