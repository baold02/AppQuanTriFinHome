package com.example.appq.interface_;

import com.google.firebase.database.DatabaseError;

public interface IAfterInsertObject {
    void onSuccess(Object obj);
    void onError(DatabaseError exception);
}
