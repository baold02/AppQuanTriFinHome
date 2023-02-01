package com.example.appq.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appq.Activity.ReportActivity;
import com.example.appq.R;
import com.example.appq.model.Report;
import com.example.appq.model.RoomModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdapterReport extends RecyclerView.Adapter<AdapterReport.HolderRoomFavorite> {
    private Context  context;
    private ArrayList<RoomModel> roomModels;
    public AdapterReport(Context context, ArrayList<RoomModel> roomModels) {
        this.context = context;
        this.roomModels = roomModels;
    }

    @NonNull
    @Override
    public HolderRoomFavorite onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_home, parent, false);
        return new HolderRoomFavorite(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRoomFavorite holder, int position) {
      RoomModel roomModel = roomModels.get(position);
      List<Report> report = new ArrayList<>();
        Report report1 = new Report();

      DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference("Report");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Report idRoom = dataSnapshot.getValue(Report.class);
                    assert idRoom != null;
                    if (Objects.equals(idRoom.getIdRoom(),roomModel.getId())) {
                        String report = "" + dataSnapshot.child("report").getValue();
                        holder.tvBc.setText(report);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "onCancelled: " + error.getMessage());
            }
        });
      loadRoom(roomModel,holder);
        holder.imgXoa.setOnClickListener(v -> {
            //add favorite | xóa favorite
            deleteRoom(roomModel);
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


    private void loadRoom(RoomModel roomModel, HolderRoomFavorite holder) {
        String roomId = roomModel.getId();
        Log.d("aaa","id:"+roomId);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Room");
        ref.child(roomId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String roomTitle = ""+snapshot.child("name").getValue();
                        String roomAdr = ""+snapshot.child("address").getValue();
                        String roomPrice = ""+snapshot.child("price").getValue();
                        String roomImg = ""+snapshot.child("img").getValue();
                        String uid = ""+snapshot.child("uid").getValue();


//                        roomModel.setFavorite(true);
                        roomModel.setTitle(roomAdr);
                        roomModel.setName(roomTitle);
                        roomModel.setImg(roomImg);
                        roomModel.setUid(uid);
//
                        holder.tvName.setText(roomTitle);
                        holder.tvPrice.setText(roomPrice);
                        holder.tvAddress.setText(roomAdr);
                        Picasso.get().load(roomImg).into(holder.imgRoom);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("", error.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return roomModels.size();
    }

    class HolderRoomFavorite extends RecyclerView.ViewHolder{
        private ConstraintLayout container;
        private AppCompatImageView imgRoom, imgXoa;
        private TextView tvName, tvPrice, tvAddress, tvBc;
        private AppCompatCheckBox btnFavorite;
        public HolderRoomFavorite(@NonNull View itemView) {
            super(itemView);
            imgRoom = itemView.findViewById(R.id.imgHome);
            tvAddress = itemView.findViewById(R.id.tvDiaChi);
            tvPrice = itemView.findViewById(R.id.tvGia);
            tvName = itemView.findViewById(R.id.tvName);
            tvBc = itemView.findViewById(R.id.tvBc);
            imgXoa = itemView.findViewById(R.id.imgXoa);

        }
    }
}
