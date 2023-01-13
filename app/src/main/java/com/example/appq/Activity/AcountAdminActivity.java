package com.example.appq.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.appq.R;

public class AcountAdminActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tvChinhSuaTaiKhoan;
    private TextView tvXemTaiKhoan;
    private TextView tvLogOut;
    private CardView cardview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acount_admin);
        initView();

        tvChinhSuaTaiKhoan.setOnClickListener(v -> {
            Intent intent= new Intent(AcountAdminActivity.this,EditAcountActivity.class);
            startActivity(intent);
        });

        tvLogOut.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = AcountAdminActivity.this.getSharedPreferences("FILE_LOGIN", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(AcountAdminActivity.this, LoginActivity.class);
            startActivity(intent);
            AcountAdminActivity.this.finish();
        });
    }



    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        tvChinhSuaTaiKhoan = findViewById(R.id.tvChinhSuaTaiKhoan);
        tvXemTaiKhoan = findViewById(R.id.tvXemTaiKhoan);
        tvLogOut = findViewById(R.id.tvLogOut);
        cardview = findViewById(R.id.cardview);

    }
}