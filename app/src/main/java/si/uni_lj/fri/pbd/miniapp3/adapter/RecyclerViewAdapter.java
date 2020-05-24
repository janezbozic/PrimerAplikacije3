package si.uni_lj.fri.pbd.miniapp3.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import si.uni_lj.fri.pbd.miniapp3.R;
import si.uni_lj.fri.pbd.miniapp3.adapter.async.AsyncImageDownload;
import si.uni_lj.fri.pbd.miniapp3.models.RecipeSummaryIM;
import si.uni_lj.fri.pbd.miniapp3.ui.DetailsActivity;

// Adapter for recycledViews
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private RecyclerViewAdapter.ItemClickListener mClickListener;
    private ArrayList<RecipeSummaryIM> mData;
    Context context;
    Boolean fav;

    public RecyclerViewAdapter(Context context, ArrayList<RecipeSummaryIM> data, boolean fav) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        this.fav = fav;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_grid_item, parent, false);
        return new RecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        RecipeSummaryIM summary = mData.get(position);
        holder.textView.setText(summary.getStrMeal());
        if (!fav) {
            //Retrieving / downloading image from API
            AsyncImageDownload asyncImageDownload = new AsyncImageDownload(holder.imageView, context);
            asyncImageDownload.execute(summary.getStrMealThumb());
        }
        else {
            try {
                //Retrieving image from database
                byte [] encodeByte= Base64.decode(summary.getImageBitmap(),Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                holder.imageView.setImageBitmap(bitmap);
            } catch(Exception e) {
                e.getMessage();
            }
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                Bundle b = new Bundle();
                b.putBoolean("fav", fav);
                b.putLong("id", Long.parseLong(summary.getIdMeal()));
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
    }

    // ViewHolder for item in recycledview
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) mClickListener.onItemClick(itemView, getAdapterPosition());
                }
            });
            textView = itemView.findViewById(R.id.text_view_content);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}