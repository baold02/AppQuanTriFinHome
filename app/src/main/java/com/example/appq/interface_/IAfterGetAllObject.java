package com.example.appq.interface_;

import com.google.firebase.database.DatabaseError;

public interface IAfterGetAllObject {
    void iAfterGetAllObject(Object obj);
    void onError(DatabaseError error);
}
