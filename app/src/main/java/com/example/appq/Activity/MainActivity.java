package com.example.appq.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.appq.R;
import com.example.appq.adapter.UserAdapter;
import com.example.appq.dao.UserDao;
import com.example.appq.interface_.IAfterGetAllObject;
import com.example.appq.interface_.IAfterUpdateObject;
import com.example.appq.interface_.OnClickItem;
import com.example.appq.interface_.OnLockUser;
import com.example.appq.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickItem, OnLockUser {
    private Toolbar toolbar;
    private RecyclerView rcvDanhSachUser;



    private List<UserModel> userList;
    private UserAdapter userAdapter;
    FirebaseAuth firebaseAuth;
    UserModel userModel = new UserModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setUpListUser();
        firebaseAuth = FirebaseAuth.getInstance();

    }


    private void setUpListUser() {
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, this, this);
        rcvDanhSachUser.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rcvDanhSachUser.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        rcvDanhSachUser.setAdapter(userAdapter);
        getUserList();
    }

    private void getUserList() {
        UserDao.getInstance().getAllUser(new IAfterGetAllObject() {
            @Override
            public void iAfterGetAllObject(Object obj) {
                userList = (List<UserModel>) obj;
                userAdapter.setData(userList);
            }

            @Override
            public void onError(DatabaseError error) {

            }

        });
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        rcvDanhSachUser = findViewById(R.id.rcvDanhSachUser);
    }

    @Override
    public void onClickItem(Object obj) {
        UserModel user = (UserModel) obj;
        Intent intent = new Intent(MainActivity.this,DetailsUserActivity.class);
//        Fragment fragment = new DetailUserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", user.getUserID());
       intent.putExtras(bundle);
       startActivity(intent);

    }

    @Override
    public void onUpdateItem(Object obj) {

    }

    @Override
    public void onDeleteItem(Object obj) {

    }

    @Override
    public void onLock(UserModel user) {
        firebaseAuth.getCurrentUser();
            UserDao.getInstance().updateUser(user, user.toMap(), new IAfterUpdateObject() {
                @Override
                public void onSuccess(Object obj) {
                    Toast.makeText(MainActivity.this, "thành công", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(DatabaseError error) {
                    Toast.makeText(MainActivity.this, "That bai", Toast.LENGTH_SHORT).show();
                }
            });
        }




}