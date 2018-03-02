package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.tungha.datn_timkiemnhatro.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Classs.BinhLuan;
import Classs.DanhGiaChiTiet;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.tungha.datn_timkiemnhatro.MainActivity.mAuth;

/**
 * Created by Windows 10 TIMT on 08-Nov-17.
 */

public class Adapter_DSDanhGia extends RecyclerView.Adapter<Adapter_DSDanhGia.ViewHolder> {
    ArrayList<DanhGiaChiTiet> arrDG ;
    Context context;
    DanhGiaChiTiet dgct;
    String ngaydang;
    FirebaseUser user = mAuth.getCurrentUser();

    SimpleDateFormat iftoday = new SimpleDateFormat("'Hôm nay, lúc 'HH:mm");
    SimpleDateFormat ifinyear = new SimpleDateFormat("dd' tháng 'MM' lúc 'HH:mm");
    SimpleDateFormat ifpastyear = new SimpleDateFormat("dd'/'MM'/'yyyy', lúc 'HH:mm");
    ///////
    SimpleDateFormat today = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat year = new SimpleDateFormat("yyyy");
    public Adapter_DSDanhGia(ArrayList<DanhGiaChiTiet> arrDG, Context context) {
        this.arrDG = arrDG;
        this.context = context;
    }
    @Override
    public Adapter_DSDanhGia.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_dg,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Adapter_DSDanhGia.ViewHolder holder, int position) {

        Glide.with(context)
                .load(arrDG.get(position).getUser().getHinhAnh())
                .into(holder.imgHinhUser);
        holder.txtTenUser.setText(arrDG.get(position).getUser().getHoTen());

        ///////
        String layngaycuanha = today.format(arrDG.get(position).getNgayDang());
        String laynamcuanha = year.format(arrDG.get(position).getNgayDang());
        ///////
        Date date_today = new Date();
        String ngayhientai = today.format(date_today);
        String namhientai = year.format(date_today);
        if(layngaycuanha.compareTo(ngayhientai)==0){
            ngaydang = iftoday.format(arrDG.get(position).getNgayDang());
        }else if(laynamcuanha.compareTo(namhientai)==0){
            ngaydang=ifinyear.format(arrDG.get(position).getNgayDang());
        }else {
            ngaydang=ifpastyear.format(arrDG.get(position).getNgayDang());
        }
        holder.txtNgayDang.setText(ngaydang);
        holder.ratingBar.setRating(arrDG.get(position).getDiem());


    }

    @Override
    public int getItemCount() {
        return arrDG.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imgHinhUser;
        TextView txtTenUser,txtNgayDang;
        RatingBar ratingBar;
        public ViewHolder(View itemView) {
            super(itemView);
            imgHinhUser = (CircleImageView) itemView.findViewById(R.id.imgUser_DG);
            txtTenUser = (TextView) itemView.findViewById(R.id.txtUser_DG);
            txtNgayDang = (TextView) itemView.findViewById(R.id.txtNgayDang_DG);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingbar_itemDanhGia);
        }
    }
}
