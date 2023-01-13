package com.example.appq.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.appq.R;
import com.example.appq.dao.UserDao;
import com.example.appq.interface_.IAfterGetAllObject;
import com.example.appq.interface_.IAfterUpdateObject;
import com.example.appq.model.UserModel;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

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
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_user);
        initView();
//        setUpToolbar();
        setUpDuLieu();
//        setUpGioHangList();
//        setUpProductList();
    }

    private void setUpDuLieu() {
            String userName = getIntent().getStringExtra("id");
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
        tvGioHang = findViewById(R.id.tvGioHang);
        tvSanPhamYeuThich = findViewById(R.id.tvSanPhamYeuThich);
        tvHoatDong = findViewById(R.id.tvHoatDong);
        btnEnable = findViewById(R.id.btnEnable);
        toolbar = findViewById(R.id.toolbar);
        rcvGioHang = findViewById(R.id.rcvGioHang);
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