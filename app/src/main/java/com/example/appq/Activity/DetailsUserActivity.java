package com.example.appq.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.appq.R;
import com.example.appq.adapter.RoomHostAdapter;
import com.example.appq.dao.UserDao;
import com.example.appq.interface_.IAfterGetAllObject;
import com.example.appq.interface_.IAfterUpdateObject;
import com.example.appq.model.RoomModel;
import com.example.appq.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsUserActivity extends AppCompatActivity {
    private CircleImageView imgUser;
    private TextView tvUserName;
    private TextView tvHoTen;
    private TextView tvDiaChi;
    private TextView tvSDT;
    private TextView tvGioHang;
    private TextView tvSanPhamYeuThich;
    private TextView tvHoatDong;
    private ToggleButton btnEnable;
    private Toolbar toolbar;
    private RecyclerView rcvGioHang;
    private RecyclerView rcvSanPhamYT;
    DatabaseReference RoomRef, UserRef;
    private int countRoom = 0;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String userId;
    private List<RoomModel> roomModelList;
    private RoomHostAdapter roomAdapter;
    RecyclerView recyclerView;
    String  userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_user);
        initView();
//        setUpToolbar();
        setUpDuLieu();
        initRoom();
    }

    private void setUpDuLieu() {
             userName = getIntent().getStringExtra("id");
            UserDao.getInstance().getUserByUserNameListener(userName, new IAfterGetAllObject() {
                @Override
                public void iAfterGetAllObject(Object obj) {
                    UserModel user = (UserModel) obj;
                    if (user.getUserID() != null) {
                        buildComponent(user);
                    }
                }

                @Override
                public void onError(DatabaseError error) {

                }
            });
    }

    private void initRoom() {

        roomModelList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Room");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    RoomModel roomModel = dataSnapshot.getValue(RoomModel.class);
                    assert roomModel != null;
                    if (Objects.equals(roomModel.getUid(),userName)) {
                        roomModelList.add(roomModel);
                        roomAdapter = new RoomHostAdapter(DetailsUserActivity.this, roomModelList, roomModel1 -> {
                            onClickGoToDetail(roomModel1);
                        });
                        recyclerView.setAdapter(roomAdapter);
                        recyclerView.setHasFixedSize(true);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "onCancelled: " + error.getMessage());
            }
        });
    }

    private  void onClickGoToDetail(RoomModel roomModel){
            Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Room", roomModel);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void buildComponent(UserModel user) {
        setUpTongButton(user);
        Picasso.get()
                .load(user.getAvatar())
                .into(imgUser);

        tvUserName.setText(user.getName());
        tvSDT.setText("Số điện thoại : " + user.getPhoneNumber());

        if (user.getName() != null && user.getUserID().length() > 0) {
            tvHoTen.setText("Họ tên : " + user.getName());
        } else {
            tvHoTen.setText("Chưa cập nhật");
        }

        if (user.getAddress() != null && user.getAddress().length() > 0) {
            tvDiaChi.setText("Địa chỉ : " + user.getAddress());
        } else {
            tvDiaChi.setText("Chưa cập nhật");
        }

        RoomRef = FirebaseDatabase.getInstance().getReference().child("Users").child("favorites");
        RoomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    countRoom = (int) snapshot.getChildrenCount();
                    tvSanPhamYeuThich.setText(Integer.toString(countRoom)+"  Favorite");
                }else {
                    tvSanPhamYeuThich.setText("0 Favorite");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        if (user.isEnable()) {
            btnEnable.setChecked(true);
            tvHoatDong.setText("Đang hoạt động");
            btnEnable.setBackgroundColor(Color.RED);

        } else {
            btnEnable.setChecked(false);
            tvHoatDong.setText("Ngừng hoạt động");
            btnEnable.setBackgroundColor(Color.GREEN);
        }

    }

    private void initView() {
        imgUser = findViewById(R.id.imgUser);
        tvUserName = findViewById(R.id.tvUserName);
        tvHoTen = findViewById(R.id.tvHoTen);
        tvDiaChi = findViewById(R.id.tvDiaChi);
        tvSDT = findViewById(R.id.tvSDT);
        tvSanPhamYeuThich = findViewById(R.id.tvSanPhamYeuThich);
        tvHoatDong = findViewById(R.id.tvHoatDong);
        btnEnable = findViewById(R.id.btnEnable);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rvRoom);
        rcvSanPhamYT = findViewById(R.id.rcvSanPhamYT);
    }



    private void setUpTongButton(UserModel user) {
        btnEnable.setOnClickListener(v -> {
            if (!btnEnable.isChecked()) {
                user.setEnable(false);
                UserDao.getInstance().updateUser(user, user.toMapLock(), new IAfterUpdateObject() {
                    @Override
                    public void onSuccess(Object obj) {
//                        OverUtils.makeToast(getContext(), "Khóa thành công user");
                        Toast.makeText(DetailsUserActivity.this, "Khóa thành công user", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });
            }
            if (btnEnable.isChecked()) {
                user.setEnable(true);
                UserDao.getInstance().updateUser(user, user.toMapLock(), new IAfterUpdateObject() {
                    @Override
                    public void onSuccess(Object obj) {
//                        OverUtils.makeToast(getContext(), "Mở khóa thành công user");
                        Toast.makeText(DetailsUserActivity.this, "Mở khóa thành công user", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });
            }
        });
    }
}