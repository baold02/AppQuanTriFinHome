package com.example.appq.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.appq.R;
import com.example.appq.dao.AdminDao;
import com.example.appq.interface_.IAfterGetAllObject;
import com.example.appq.interface_.IAfterUpdateObject;
import com.example.appq.model.Admin;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseError;

public class EditAcountActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputEditText edtTenDangNhap;
    private TextInputEditText edtOldPassword;
    private TextInputEditText edtNewPassword;
    private TextInputEditText edtReNewPassword;
    private MaterialButton btnCancel;
    private MaterialButton btnChangePass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_acount);
        initView();
//        setUpData();
        setUpBtnCancel();
        setUpBtnChangePass();
    }

    private void setUpBtnChangePass() {

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = edtTenDangNhap.getText().toString().trim();
                String password = edtOldPassword.getText().toString().trim();
                String newPassword = edtNewPassword.getText().toString().trim();
                String reNewPassword = edtReNewPassword.getText().toString().trim();

                if (password.isEmpty() || newPassword.isEmpty() || reNewPassword.isEmpty()) {
                    Toast.makeText(EditAcountActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPassword.equals(reNewPassword)) {
                    Toast.makeText(EditAcountActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                AdminDao.getInstance().getAdminByUserName(userName, new IAfterGetAllObject() {
                    @Override
                    public void iAfterGetAllObject(Object obj) {
                        Admin admin = (Admin) obj;
                        if (admin != null && admin.getUserName() != null) {
                            if (password.equals(admin.getPassword())) {
                                admin.setPassword(newPassword);
                                AdminDao.getInstance().updateAdmin(admin, new IAfterUpdateObject() {
                                    @Override
                                    public void onSuccess(Object obj) {
                                        Toast.makeText(EditAcountActivity.this, "Đổi nật khẩu thành công", Toast.LENGTH_SHORT).show();
                                        clearForm(1);
                                    }

                                    @Override
                                    public void onError(DatabaseError error) {
                                        Toast.makeText(EditAcountActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(EditAcountActivity.this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(DatabaseError error) {

                    }
                });
            }
        });
    }

    private void clearForm(int type) {
        if (type == 0) {
            edtOldPassword.setText("");
        }
        edtNewPassword.setText("");
        edtReNewPassword.setText("");
    }

//    private void setUpData() {
//        edtTenDangNhap.setText(LoginActivity.loginedUserName);
//    }

    private void setUpBtnCancel() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearForm(0);
            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtReNewPassword = findViewById(R.id.edtReNewPassword);
        btnCancel = findViewById(R.id.btnCancel);
        btnChangePass = findViewById(R.id.btnChangePass);
    }
}