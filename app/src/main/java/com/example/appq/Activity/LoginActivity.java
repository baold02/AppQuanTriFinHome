package com.example.appq.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.appq.R;
import com.example.appq.dao.AdminDao;
import com.example.appq.interface_.IAfterGetAllObject;
import com.example.appq.interface_.IDone;
import com.example.appq.model.Admin;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseError;

public class LoginActivity extends AppCompatActivity {
    private ImageView imageView2;
    private TextInputLayout tILEdtTenDangNhap;
    private TextInputEditText edtTenDangNhap;
    private TextInputLayout textInputLayout;
    private TextInputEditText edtMatKhau;
    private AppCompatCheckBox chkLuuMatKhau;
    private AppCompatButton btnDangNhap;
    private AppCompatButton btnHuyDangNhap;

    public static String loginedUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        readInfo();

        btnHuyDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearForm();
            }
        });

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenDangNhap = edtTenDangNhap.getText().toString().trim();
                String matKhau = edtMatKhau.getText().toString().trim();
                boolean remember;
                if(chkLuuMatKhau.isChecked()) {
                    remember = true;
                } else {
                    remember = false;
                }
                if(tenDangNhap.isEmpty() || matKhau.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_LONG).show();
                    return;
                }
                verifyAccount(tenDangNhap, matKhau, done -> {
                    if(done) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        remember(tenDangNhap, matKhau, remember);
                        loginedUserName = tenDangNhap;
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });


    }

    private void verifyAccount(String tenDangNhap, String matKhau, IDone iDone) {
        AdminDao.getInstance().getAdminByUserName(tenDangNhap, new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                if(obj != null) {
                    Admin admin = (Admin) obj;
                    if(admin.getUserName() == null) {
                        Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                        iDone.onDone(false);
                    } else {
                        if(!matKhau.equals(admin.getPassword())) {
                            Toast.makeText(LoginActivity.this, "Mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                            iDone.onDone(false);
                        } else {
                            iDone.onDone(true);
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onError(DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Lỗi", Toast.LENGTH_SHORT).show();
                iDone.onDone(false);
            }
        });
    }

    private void clearForm() {
        edtTenDangNhap.setText("");
        edtMatKhau.setText("");
        chkLuuMatKhau.setChecked(false);
    }

    private void readInfo() {
        SharedPreferences sharedPreferences = getSharedPreferences("FILE_LOGIN", Context.MODE_PRIVATE);
        boolean remember = sharedPreferences.getBoolean("remember", false);
        String tenDangNhap = sharedPreferences.getString("username", "");
        String matKhau = sharedPreferences.getString("password", "");
        if(remember) {
            edtTenDangNhap.setText(tenDangNhap);
            edtMatKhau.setText(matKhau);
            chkLuuMatKhau.setChecked(true);
        }
    }

    private void remember(String tenDangNhap, String matKhau, boolean remember) {
        SharedPreferences sharedPreferences = getSharedPreferences("FILE_LOGIN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(remember) {
            editor.putString("username", tenDangNhap);
            editor.putString("password", matKhau);
            editor.putBoolean("remember", remember);
            editor.apply();
        } else {
            editor.clear();
        }
    }

    private void initView() {
        imageView2 = findViewById(R.id.imageView2);
        tILEdtTenDangNhap = findViewById(R.id.tIL_edtTenDangNhap);
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        textInputLayout = findViewById(R.id.textInputLayout);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        chkLuuMatKhau = findViewById(R.id.chkLuuMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnHuyDangNhap = findViewById(R.id.btnHuyDangNhap);
    }
}