package com.example.appq.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.appq.R;
import com.example.appq.dao.RoomDao;
import com.example.appq.dao.UserDao;
import com.example.appq.interface_.IAfterUpdateObject;
import com.example.appq.model.RoomModel;
import com.example.appq.model.UserModel;
import com.google.firebase.database.DatabaseError;

public class ChiTietRoomActivity extends AppCompatActivity {
    RoomModel roomModel;
    private Button btnEnable;

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
    }

    private void setUpTongButton(RoomModel user) {
        btnEnable.setOnClickListener(v -> {
                user.setBrowser(true);
                RoomDao.getInstance().updateRoom(user, user.toMapBrowser(), new IAfterUpdateObject() {
                    @Override
                    public void onSuccess(Object obj) {
                        Toast.makeText(ChiTietRoomActivity.this, "Đã Duyệt Phòng", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ChiTietRoomActivity.this,AllRoomActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });

        });
    }
}