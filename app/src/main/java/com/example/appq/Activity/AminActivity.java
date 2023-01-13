package com.example.appq.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.appq.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AminActivity extends AppCompatActivity {
    TextView txtSumViewsAdminView, txtSumHostsAdminView, txtSumRoomsAdminView;

    LinearLayout lnLtRoomsAdminView, lnLtHostsAdminView, lnLtReportsAdminView, lnLtRoomsWaitForApprovalAdminView;
    DatabaseReference RoomRef, UserRef;
    private int countRoom = 0;
    private int countUser = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amin);
        initControl();
        readUser();
        lnLtRoomsWaitForApprovalAdminView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AminActivity.this,AcountAdminActivity.class);
                startActivity(intent);
            }
        });
        lnLtHostsAdminView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AminActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        RoomRef = FirebaseDatabase.getInstance().getReference().child("Room");
        RoomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.exists()){
                   countRoom = (int) snapshot.getChildrenCount();
                   txtSumRoomsAdminView.setText(Integer.toString(countRoom)+"  Room");
               }else {
                   txtSumRoomsAdminView.setText("0 Room");
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void initControl() {
        txtSumHostsAdminView = (TextView) findViewById(R.id.txt_sum_hosts_admin_view);
        txtSumRoomsAdminView = (TextView) findViewById(R.id.txt_sum_rooms_admin_view);

        lnLtRoomsAdminView = (LinearLayout) findViewById(R.id.lnLt_rooms_admin_view);
        lnLtHostsAdminView = (LinearLayout) findViewById(R.id.lnLt_hosts_admin_view);
        lnLtReportsAdminView = (LinearLayout) findViewById(R.id.lnLt_reports_admin_view);
        lnLtRoomsWaitForApprovalAdminView = (LinearLayout) findViewById(R.id.lnLt_rooms_wait_for_approval_admin_view);
    }

    private void readUser(){
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    countUser = (int) snapshot.getChildrenCount();
                    txtSumHostsAdminView.setText(Integer.toString(countUser)+"  User");
                }else {
                    txtSumRoomsAdminView.setText("0 User");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}