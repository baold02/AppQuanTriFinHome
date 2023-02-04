package com.example.appq.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.appq.R;
import com.example.appq.adapter.RoomAdapterHome;
import com.example.appq.adapter.RoomHostAdapter;
import com.example.appq.model.RoomModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllRoomActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private RoomHostAdapter roomAdapter;
    private List<RoomModel> mRoomModel;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_room);
        recyclerView = findViewById(R.id.rcvRoomMain);
        mRoomModel = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Room");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mRoomModel.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    RoomModel roomModel = dataSnapshot.getValue(RoomModel.class);
                    if (roomModel.isBrowser() == false){
                        mRoomModel.add(roomModel);
                        roomAdapter = new RoomHostAdapter(AllRoomActivity.this, mRoomModel, roomModel1 -> onClickGoToDetail(roomModel1));
//                    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(AllRoomActivity.this);
//                    recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setAdapter(roomAdapter);
                    }


                }
//                    loaderDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", error.getMessage());
            }
        });
    }
    private  void onClickGoToDetail(RoomModel roomModel){
        Intent intent = new Intent(AllRoomActivity.this, ChiTietRoomActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Room", roomModel);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}