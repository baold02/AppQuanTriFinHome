package com.example.appq.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appq.R;
import com.example.appq.interface_.IClickItemUserListener;
import com.example.appq.model.RoomModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RoomHostAdapter extends RecyclerView.Adapter<RoomHostAdapter.ViewHolder> {
    private  Context context;
    private List<RoomModel> roomModelList;
    private IClickItemUserListener iClickItemUserListener;

    public RoomHostAdapter(Context context, List<RoomModel> roomModelList, IClickItemUserListener listener) {
        this.context = context;
        this.roomModelList = roomModelList;
        this.iClickItemUserListener = listener;
        notifyDataSetChanged();
    }
    public RoomHostAdapter(Context context, List<RoomModel> roomModelList) {
        this.context = context;
        this.roomModelList = roomModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RoomModel roomModel = roomModelList.get(position);
        if (roomModel == null) {
            return;
        }
        holder.tvName.setText(roomModel.getName());
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(locale);
        if (roomModel.getPrice() != null){

            holder.tvPrice.setText(currencyFormat.format(Integer.parseInt (roomModel.getPrice())) + " VNĐ/Phòng");
        }

        if (roomModel.isBrowser() == false) {
           holder.tvTrangThai.setText("Chưa Xác thực");
        }
        holder.tvAddress.setText(roomModel.getAddress());
        Picasso.get().load(roomModel.getImg()).placeholder(R.mipmap.ic_launcher).into(holder.imgRoom);


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            deleteRoom(roomModel);
            notifyDataSetChanged();
            }
        });
        holder.container.setOnClickListener(v -> {
            iClickItemUserListener.onClickItemRoom(roomModel);
        });


    }


    private void deleteRoom(RoomModel roomModel) {
        AlertDialog.Builder  builder =new AlertDialog.Builder(context);
        builder.setTitle("Delete Room")
                .setMessage("Bạn Chắc chắn Muốn Xóa Phòng trọ này chứ")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Room");
                        reference.child(roomModel.getId())
                                .removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "xóa thành công", Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }
                }).
                setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    @Override
    public int getItemCount() {
        return roomModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout container;
        private AppCompatImageView imgRoom;
        private TextView tvName, tvPrice, tvAddress,tvTrangThai;
        private ToggleButton btnEnable;
        private ImageButton btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRoom = itemView.findViewById(R.id.imgRoom);
            tvAddress = itemView.findViewById(R.id.tvAddressRoom);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvName = itemView.findViewById(R.id.tvNameRoom);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            container = itemView.findViewById(R.id.containerRoom);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
        }
    }
}
