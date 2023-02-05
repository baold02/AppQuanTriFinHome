package com.example.appq.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.appq.R;
import com.example.appq.dao.RoomDao;
import com.example.appq.dao.UserDao;
import com.example.appq.interface_.IAfterUpdateObject;
import com.example.appq.model.RoomModel;
import com.example.appq.model.UserModel;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class ChiTietRoomActivity extends AppCompatActivity {
    RoomModel roomModel;
    private Button btnEnable;
    private TextView tvName,tvDiaChi,tvGia,tvMota;
    ImageView imgPhong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_room);
        Bundle bundle = getIntent().getExtras();
        btnEnable = findViewById(R.id.btnEnable);
        if (bundle == null) {
            return;
        }
        roomModel = (RoomModel) bundle.get("Room");
        setUpTongButton(roomModel);
        initView();
    }

    private void initView(){
        tvName = findViewById(R.id.tvNameDuyet);
        tvDiaChi = findViewById(R.id.tvDT);
        tvGia = findViewById(R.id.tvGiaDuyet);
        tvMota = findViewById(R.id.tvMoTaD);
        imgPhong = findViewById(R.id.imgDuyetP);


        Picasso.get().load(roomModel.getImg()).into(imgPhong);
        tvName.setText(roomModel.getName());
        tvDiaChi.setText(roomModel.getAddress());
        tvMota.setText(roomModel.getDescription());
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(locale);
        if (roomModel.getPrice() != null){
            tvGia.setText(currencyFormat.format(Integer.parseInt (roomModel.getPrice())) + " VNĐ/Phòng");
        }
    }

    private void setUpTongButton(RoomModel user) {
        btnEnable.setOnClickListener(v -> {
                user.setBrowser(true);
                RoomDao.getInstance().updateRoom(user, user.toMapBrowser(), new IAfterUpdateObject() {
                    @Override
                    public void onSuccess(Object obj) {
                        Toast.makeText(ChiTietRoomActivity.this, "Đã Duyệt Phòng", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });

        });
    }
}