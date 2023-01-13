package com.example.appq.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.appq.R;
import com.example.appq.interface_.OnClickItem;
import com.example.appq.interface_.OnLockUser;
import com.example.appq.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
   Context context;
    private List<UserModel> userList;
    private OnClickItem onClickItem;
    private OnLockUser onLockUser;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public UserAdapter(List<UserModel> userList, OnClickItem onClickItem, OnLockUser onLockUser) {
        this.userList = userList;
        this.onClickItem = onClickItem;
        this.onLockUser = onLockUser;
    }

    public void setData(List<UserModel> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, @SuppressLint("RecyclerView") int position) {
        UserModel user = userList.get(position);
        if(user == null) {
            return;
        }
        viewBinderHelper.bind(holder.item, String.valueOf(user.getUserID()));

//        if(user.isEnable()) {
//            holder.tvLock.setText("Lock");
//        } else {
//            holder.tvLock.setText("Unlock");
//        }
        holder.tvUserName.setText("UserName : " + user.getName());
        holder.tvSoDienThoai.setText("SĐT : " + user.getPhoneNumber());
        if(user.getAddress() != null && user.getAddress().length() > 0) {
            holder.tvDiaChi.setText("Địa chỉ: " + user.getAddress());
        } else {
            holder.tvDiaChi.setText("Chưa thêm địa chỉ");
        }


        String imgUser = user.getAvatar();
        if(imgUser != null) {
            Picasso.get()
                    .load(imgUser)
                    .into(holder.imgUser);
        }

        holder.itemUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickItem.onClickItem(user);
            }
        });

        holder.tvLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                    reference.child(user.getUserID()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(view.getContext(), "Đã xóa tài khoản", Toast.LENGTH_SHORT).show();
                                        notifyDataSetChanged();
                                    }
                                }
                            });


            }
        });
    }



    @Override
    public int getItemCount() {
        if(userList != null) {
            return userList.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        private SwipeRevealLayout item;
        private TextView tvLock;
        private LinearLayout itemUser;
        private ImageView imgUser;
        private TextView tvUserName;
        private TextView tvSoDienThoai;
        private TextView tvDiaChi;
        private ToggleButton btnEnable;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            tvLock = itemView.findViewById(R.id.tvLock);
            itemUser = itemView.findViewById(R.id.item_user);
            imgUser = itemView.findViewById(R.id.imgUser);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvSoDienThoai = itemView.findViewById(R.id.tvSoDienThoai);
            tvDiaChi = itemView.findViewById(R.id.tvDiaChi);
            btnEnable = itemView.findViewById(R.id.btnEnable);
        }
    }
}
