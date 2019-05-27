package com.taksycraft.testapplicatons.retrofit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.recyclerview.RecyclerViewActivity;

import java.util.Vector;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
//    https://medium.com/@prakash_pun/retrofit-a-simple-android-tutorial-48437e4e5a23

public class RetrofitActivity extends AppCompatActivity {

    private RecyclerView rv;
    private ProgressBar pb;
    private CustomAdapter adapter;
    private String TAG = RetrofitActivity.class.getSimpleName() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        rv = (RecyclerView)findViewById(R.id.rv);
        pb = (ProgressBar) findViewById(R.id.pb);
        pb.setVisibility(View.VISIBLE);
        rv.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));

        GetService service  = RetrofitClient.getClient().create(GetService.class);
        Call<Vector<RecordDO>> callList = service.getAllRecords();
        callList.enqueue(new Callback<Vector<RecordDO>>() {
            @Override
            public void onResponse(Response<Vector<RecordDO>> response, Retrofit retrofit) {
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
                pb.setVisibility(View.GONE);
                if(response!=null)
                    bindToRecyclerView(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, new Exception().getStackTrace()[0].getMethodName());
                Toast.makeText(RetrofitActivity.this," error is occured", Toast.LENGTH_LONG).show();
                pb.setVisibility(View.GONE);
            }
        });
    }

    private void bindToRecyclerView(final Vector<RecordDO> vec) {
        adapter = new CustomAdapter(RetrofitActivity.this,vec);
        rv.setAdapter(adapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
//                new DBClient();
//                DBClient.getInstance().getDatabase().getRecordDAO().insert(vec.get(0));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });


    }

    public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

        private Vector<RecordDO> dataList;
        private Context context;

        public CustomAdapter(Context context,Vector<RecordDO> dataList){
            this.context = context;
            this.dataList = dataList;
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {

            public final View mView;
            private final ProgressBar pbLoader;

            TextView txtTitle,tvId;
            private ImageView coverImage;

            CustomViewHolder(View itemView) {
                super(itemView);
                mView = itemView;

                txtTitle = mView.findViewById(R.id.title);
                tvId= mView.findViewById(R.id.tvId);
                pbLoader=(ProgressBar) mView.findViewById(R.id.pbLoader);
                pbLoader.getIndeterminateDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);
                coverImage = mView.findViewById(R.id.coverImage);
            }
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.item_with_image, null);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final CustomViewHolder holder, int position) {
            RecordDO recordDO = dataList.get(position);
            holder.txtTitle.setText(recordDO .getTitle());
            holder.pbLoader.setVisibility(View.VISIBLE);
            holder.tvId.setText(recordDO .getId()+"");
            Glide.with(context).load(recordDO .getThumbnailUrl())
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
            return dataList!=null ? dataList.size(): 0 ;
        }
    }
}
