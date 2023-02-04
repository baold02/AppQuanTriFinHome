package com.example.appq.dao;

import com.example.appq.interface_.IAfterUpdateObject;
import com.example.appq.model.RoomModel;
import com.example.appq.model.UserModel;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class RoomDao {
    private static RoomDao instance;

    private RoomDao() {
    }

    public static RoomDao getInstance() {
        if (instance == null) {
            instance = new RoomDao();
        }
        return instance;
    }

    public void updateRoom(RoomModel room, Map<String, Object> map, IAfterUpdateObject iAfterUpdateObject) {
        FirebaseDatabase.getInstance().getReference().child("Room").child(room.getId())
                .updateChildren(map, (error, ref) -> {
                    if (error == null) {
                        iAfterUpdateObject.onSuccess(room); // trả về user đã được update
                    } else {
                        iAfterUpdateObject.onError(error);
                    }
                });
    }
}
