package com.taksycraft.testapplicatons.retrofit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.taksycraft.testapplicatons.R;

import java.util.Vector;

public class CustomAdapter extends RecyclerView.Adapter< CustomAdapter.CustomViewHolder> {

    private Vector<RecordDO> dataList;
    private Context context;

    public CustomAdapter(Context context, Vector<RecordDO> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public   ProgressBar pbLoader;

        TextView txtTitle, tvId;
        private ImageView coverImage;

        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtTitle = mView.findViewById(R.id.title);
            tvId = mView.findViewById(R.id.tvId);
            pbLoader = (ProgressBar) mView.findViewById(R.id.pbLoader);
            coverImage = mView.findViewById(R.id.coverImage);
        }
    }

    @Override
    public CustomAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_with_image, null);
        return new CustomAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final  CustomAdapter.CustomViewHolder holder, int position) {
        RecordDO recordDO = dataList.get(position);
        holder.txtTitle.setText(recordDO.getTitle());
        holder.pbLoader.setVisibility(View.VISIBLE);
        holder.tvId.setText(recordDO.getId() + "");
        Glide.with(context).load(recordDO.getThumbnailUrl())
                .listener(
                        new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.pbLoader.setVisibility(View.GONE);
                                return false;
                            }
                        }
                )
                .into(holder.coverImage);
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;

    }
}
