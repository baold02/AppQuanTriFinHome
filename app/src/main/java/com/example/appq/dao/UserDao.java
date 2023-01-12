package com.example.appq.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.appq.interface_.IAfterGetAllObject;
import com.example.appq.interface_.IAfterInsertObject;
import com.example.appq.interface_.IAfterUpdateObject;
import com.example.appq.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDao {
    private static UserDao instance;

    private UserDao() {
    }

    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }


    public void getAllUserListener(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference()
                .child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<UserModel> userList = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data != null) {
                        UserModel user = data.getValue(UserModel.class);
                        userList.add(user);
                    }
                }
                iAfterGetAllObject.iAfterGetAllObject(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                iAfterGetAllObject.onError(error);
            }
        });
    }

    public void getAllUser(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("Users").get()
                .addOnSuccessListener(dataSnapshot -> {
                    List<UserModel> userList = new ArrayList<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if (data != null) {
                            UserModel user = data.getValue(UserModel.class);
                            userList.add(user);
                        }
                    }
                    iAfterGetAllObject.iAfterGetAllObject(userList);
                })
                .addOnFailureListener(e -> Log.e("TAG", "onFailure: "));
    }

    public void getAllUser2(IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("user").get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()) {
                            DataSnapshot dataSnapshot = task.getResult();
                            if(dataSnapshot != null) {
                                List<UserModel> userList = new ArrayList<>();
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    if (data != null) {
                                        UserModel user = data.getValue(UserModel.class);
                                        userList.add(user);
                                    }
                                }
                                iAfterGetAllObject.iAfterGetAllObject(userList);
                            }
                        }
                    }
                });
    }

    public void insertUser(UserModel user, IAfterInsertObject iAfterInsertObject) {
        FirebaseDatabase.getInstance().getReference().child("user").child(user.getName())
                .setValue(user, (error, ref) -> {
                    if (error == null) {
                        iAfterInsertObject.onSuccess(user);
                    } else {
                        iAfterInsertObject.onError(error);
                    }
                });
    }

    public void updateUser(UserModel user, Map<String, Object> map, IAfterUpdateObject iAfterUpdateObject) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUserID())
                .updateChildren(map, (error, ref) -> {
                    if (error == null) {
                        iAfterUpdateObject.onSuccess(user); // trả về user đã được update
                    } else {
                        iAfterUpdateObject.onError(error);
                    }
                });
    }

    public void updateUser(UserModel user, Map<String, Object> map) {
        FirebaseDatabase.getInstance().getReference().child("user").child(user.getName())
                .updateChildren(map);
    }


    public void getUserByUserName(String userName, IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("user").child(userName)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot != null) {
                    UserModel user = dataSnapshot.getValue(UserModel.class);
                    iAfterGetAllObject.iAfterGetAllObject(user);
                } else {
                    iAfterGetAllObject.iAfterGetAllObject(new UserModel());
                }
            }
        });
    }

    public void getUserByUserNameListener(String userId, IAfterGetAllObject iAfterGetAllObject) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel user = snapshot.getValue(UserModel.class);
                        iAfterGetAllObject.iAfterGetAllObject(user);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iAfterGetAllObject.onError(error);
                    }
                });
    }


//    public void getGioHangOfUser(User user, IAfterGetAllObject iAfterGetAllObject) {
//        FirebaseDatabase.getInstance().getReference().child("user").child(user.getUsername())
//                .child("gio_hang").get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                DataSnapshot dataSnapshot = task.getResult();
//                List<GioHang> gioHangList = new ArrayList<>();
//                if (dataSnapshot != null) {
//                    for (DataSnapshot data : dataSnapshot.getChildren()) {
//                        GioHang gioHang = data.getValue(GioHang.class);
//                        gioHangList.add(gioHang);
//                    }
//                }
//                iAfterGetAllObject.iAfterGetAllObject(gioHangList);
//            }
//
//        });
//
//    }


//    public void getSanPhamYeuThichOfUser(User user, IAfterGetAllObject iAfterGetAllObject) {
//        FirebaseDatabase.getInstance().getReference()
//                .child("user").child(user.getUsername()).child("ma_sp_da_thich")
//                .get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        DataSnapshot snapshot = task.getResult();
//                        if(snapshot != null) {
//                            List<String> sanPhamYeuThichList = new ArrayList<>();
//                            for (DataSnapshot data : snapshot.getChildren()) {
//                                String maSP = data.getValue(String.class);
//                                sanPhamYeuThichList.add(maSP);
//                            }
//                            iAfterGetAllObject.iAfterGetAllObject(sanPhamYeuThichList);
//                        }
//                    }
//                });
//    }
}
