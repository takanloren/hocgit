package Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.tungha.datn_timkiemnhatro.ChiTietBaiDang;
import com.tungha.datn_timkiemnhatro.R;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import Classs.LuuBai;
import Classs.Nha;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.tungha.datn_timkiemnhatro.MainActivity.db;
import static com.tungha.datn_timkiemnhatro.MainActivity.mAuth;
import static com.tungha.datn_timkiemnhatro.TrangChinh.InfoUser;

/**
 * Created by Windows 10 TIMT on 08-Nov-17.
 */

public class Adapter_DSNha_DaLuu extends RecyclerView.Adapter<Adapter_DSNha_DaLuu.ViewHolder> {
    ArrayList<Nha> arrNha ;
    Context context;
    String ngaydang,so,duong,phuong,quan,tinh;
    Nha nhaduocchon;
    Nha nhasexoa;

    FirebaseUser user = mAuth.getCurrentUser();

    public Adapter_DSNha_DaLuu(ArrayList<Nha> arrNha, Context context) {
        this.arrNha = arrNha;
        this.context = context;
    }
    @Override
    public Adapter_DSNha_DaLuu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_dsbaidaluu,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Adapter_DSNha_DaLuu.ViewHolder holder, int position) {
        Glide.with(context)
                .load(arrNha.get(position).getHinhAnh())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.progress_image_loading))
                .transition(DrawableTransitionOptions.withCrossFade(1222))
                .into(holder.imgHinhNha);
        holder.txtTenUser.setText(arrNha.get(position).getUser().getHoTen());
        String giathapnhat = NumberFormat.getNumberInstance(Locale.US).format(arrNha.get(position).getGiaThapNhat());
        holder.txtGiaTu.setText(giathapnhat+" VNĐ");
        holder.txtSoPhong.setText(String.valueOf(arrNha.get(position).getSoLuongPhong()));
        so = arrNha.get(position).getDiaChi().getSoNha();
        duong = arrNha.get(position).getDiaChi().getDuong();
        phuong = arrNha.get(position).getDiaChi().getPhuong();
        quan =arrNha.get(position).getDiaChi().getQuan();
        tinh = arrNha.get(position).getDiaChi().getThanhPho();
        holder.txtDiaChi.setText(so+" "+duong+","+phuong+","+quan+","+tinh);
        holder.rating.setRating(arrNha.get(position).getDanhGia().getDiemTrungBinh());
    }
    private void MoFormXemCTBai(){
        Intent i = new Intent(context, ChiTietBaiDang.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("dulieunha",nhaduocchon);
        i.putExtra("data",bundle);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
    private void XoaNhaDaLuu(String id){
        this.notifyDataSetChanged();
        db.collection("NhaDaLuu").document(user.getEmail()).collection("DSNhaDaLuu").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return arrNha.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnCreateContextMenuListener{
        ImageView imgHinhNha;
        TextView txtTenUser,txtDiaChi,txtSoPhong,txtGiaTu;
        RatingBar rating;
        public ViewHolder(View itemView) {
            super(itemView);

            imgHinhNha= (ImageView) itemView.findViewById(R.id.imgNha_itemDSLuu);
            txtDiaChi= (TextView) itemView.findViewById(R.id.txtDiaChi_itemDSLuu);
            txtGiaTu= (TextView) itemView.findViewById(R.id.txtGia_itemDSLuu);
            txtTenUser= (TextView) itemView.findViewById(R.id.txtNguoiDang_itemDSLuu);
            txtSoPhong= (TextView) itemView.findViewById(R.id.txtSL_itemDSLuu);
            rating= (RatingBar) itemView.findViewById(R.id.ratingBar_itemDSLuu);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nhaduocchon = arrNha.get(getAdapterPosition());
                    MoFormXemCTBai();
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
                            nhasexoa = arrNha.get(getAdapterPosition());
                            String idNhaSeXoa = nhasexoa.getId();
                            arrNha.remove(getAdapterPosition());
                            XoaNhaDaLuu(idNhaSeXoa);
                            return false;
                        }
                    });
                }
            });
        }


        @Override
        public boolean onLongClick(View v) {
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }
}
