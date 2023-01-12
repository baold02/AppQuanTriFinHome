package com.example.appq.interface_;

import com.google.firebase.database.DatabaseError;

public interface IAfterUpdateObject {
    void onSuccess(Object obj);
    void onError(DatabaseError error);
}
