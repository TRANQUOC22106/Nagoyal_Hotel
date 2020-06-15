package com.quoctran.nagoyalhotel.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.quoctran.nagoyalhotel.activities.HotelDetailActivity;
import com.quoctran.nagoyalhotel.R;
import com.quoctran.nagoyalhotel.pojo.Hotel;
import com.squareup.picasso.Picasso;

public class HotelAdapter extends FirestoreRecyclerAdapter<Hotel, HotelAdapter.HotelHolder> {

    public HotelAdapter(@NonNull FirestoreRecyclerOptions<Hotel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final HotelHolder holder, int position, @NonNull final Hotel model) {
        final String idHotel = getSnapshots().getSnapshot(holder.getAdapterPosition()).getId();
        holder.textViewName.setText(model.getNameHotel());
        holder.textViewLocation.setText(model.getLocationHotel());
        holder.textViewPrice.setText(String.valueOf(model.getPriceHotel()));
        holder.btn_openLinkBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(model.getLink()));
                holder.itemView.getContext().startActivity(browserIntent);
            }
        });
        Picasso.get().load(model.getImageHotel()).into(holder.imgViewHotel);
        holder.cardViewItemHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentHotelDetail = new Intent(holder.itemView.getContext(), HotelDetailActivity.class);
                intentHotelDetail.putExtra("idHotel", idHotel);
                holder.itemView.getContext().startActivity(intentHotelDetail);
            }
        });


    }

    @NonNull
    @Override
    public HotelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hotel_item,
                parent, false);
        return new HotelHolder(v);
    }

    class HotelHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewLocation;
        TextView textViewPrice;
        CardView cardViewItemHotel;
        ImageView imgViewHotel;
        Button btn_openLinkBooking;
        public HotelHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tv_nameHotel);
            textViewLocation = itemView.findViewById(R.id.tv_descriptionHotel);
            textViewPrice = itemView.findViewById(R.id.tv_priceHotel);
            cardViewItemHotel = itemView.findViewById(R.id.cardView_itemHotel);
            imgViewHotel = itemView.findViewById(R.id.imv_imageHotel);
            btn_openLinkBooking = itemView.findViewById(R.id.btn_openLinkBooking);
        }
    }
}