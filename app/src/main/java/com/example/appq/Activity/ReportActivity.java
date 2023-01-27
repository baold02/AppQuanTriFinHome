package com.example.appq.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.appq.R;
import com.example.appq.adapter.AdapterReport;
import com.example.appq.model.RoomModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {
    RecyclerView rvRepotr;
    private ArrayList<RoomModel> roomModels;
    AdapterReport adapterReport;
    RoomModel roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        rvRepotr = findViewById(R.id.rvReport);
        loadFavorite();

    }

    private void loadFavorite() {
        roomId = new RoomModel();
        roomModels = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Report");
        ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        roomModels.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String roomId = "" + dataSnapshot.child("idRoom").getValue();

                            RoomModel roomModel = new RoomModel();
                            roomModel.setId(roomId);
                            roomModels.add(roomModel);
                        }
                        adapterReport = new AdapterReport(ReportActivity.this,roomModels);
                        rvRepotr.setAdapter(adapterReport);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d("", error.getMessage());
                    }
                });
    }
}