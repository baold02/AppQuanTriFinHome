package com.example.appq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appq.R;
import com.example.appq.interface_.IClickItemUserListener;
import com.example.appq.model.RoomModel;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RoomAdapterHome extends RecyclerView.Adapter<RoomAdapterHome.RoomViewholder> implements Filterable {
    Context context;
    private List<RoomModel> listFull;
    private List<RoomModel> list;
    private IClickItemUserListener iClickItemUserListener;

    public RoomAdapterHome(Context context, List<RoomModel> list, IClickItemUserListener listener) {
        this.context = context;
        this.list = list;
        this.iClickItemUserListener = listener;
        listFull = new ArrayList<>(list);
    }



    @NonNull
    @Override
    public RoomViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room2, parent, false);
        RoomViewholder roomViewholder = new RoomViewholder(view);
        return roomViewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewholder holder, int position) {
        RoomModel roomModel = list.get(position);
        if (roomModel == null) {
            return;
        }
        holder.tvName.setText(roomModel.getName());
        holder.tvTime.setText("Ngày đăng: " +roomModel.getTime());
        Locale locale = new Locale("vi", "VN");
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(locale);
        if (roomModel.getPrice() != null){
            holder.tvPrice.setText(currencyFormat.format(Integer.parseInt (roomModel.getPrice())) + " VNĐ/Phòng");
        }
        holder.tvAddress.setText(roomModel.getAddress());
//        Glide.with(context).load(roomModel.getImg()).into(holder.imgRoom);
        Picasso.get().load(roomModel.getImg()).into(holder.imgRoom);
        holder.container.setOnClickListener(v -> {
            iClickItemUserListener.onClickItemRoom(roomModel);
        });
    }

    @Override
    public int getItemCount() {
        if (list != null)
            return list.size();
        else
            return 0;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RoomModel> filterlist =new ArrayList<>();
            String pattrn = constraint.toString();
            if(pattrn == null || pattrn.isEmpty()){
                filterlist.addAll(listFull);
            }
            else{
                for(RoomModel roomModel : listFull){
                    if(roomModel.getAddress().toLowerCase().contains(pattrn.toLowerCase())){
                        filterlist.add(roomModel);
                    }
                }
                list = filterlist;
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values= filterlist;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list = (List<RoomModel>) results.values;
            notifyDataSetChanged();
        }
    };


    public class RoomViewholder extends RecyclerView.ViewHolder {
        private LinearLayout container;
        private ImageView imgRoom;
        private TextView tvName, tvPrice, tvAddress,tvTime;
        public RoomViewholder(@NonNull View itemView) {
            super(itemView);
            imgRoom = itemView.findViewById(R.id.imgHome);
            tvAddress = itemView.findViewById(R.id.tvDiaChi);
            tvPrice = itemView.findViewById(R.id.tvGia);
            tvName = itemView.findViewById(R.id.tvName);
//            tvTime = itemView.findViewById(R.id.tvNgayDang);
            container = itemView.findViewById(R.id.linearHome);
        }
    }
}
