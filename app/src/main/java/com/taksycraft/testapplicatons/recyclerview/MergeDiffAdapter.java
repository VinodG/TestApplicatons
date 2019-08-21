package com.taksycraft.testapplicatons.recyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.common.NameDO;

import java.util.Vector;

//https://www.youtube.com/watch?v=xPPMygGxiEo
public class MergeDiffAdapter extends ListAdapter<NameDO, MergeDiffAdapter.CommonHolder> {
    private String TAG = MergeDiffAdapter.class.getSimpleName();
    public Vector<NameDO> vec = new Vector<NameDO>();

    public MergeDiffAdapter()
    {
        super(new DiffUtil.ItemCallback<NameDO>() {
            @Override
            public boolean areItemsTheSame(@NonNull NameDO oldItem, @NonNull NameDO newItem) {
//                boolean flag = Integer.parseInt(oldItem.id)==Integer.parseInt(newItem.id);
                boolean flag =  oldItem.id.equals(newItem.id);
                if(!flag)
                Log.e("MergeDiffAdapter", new Exception().getStackTrace()[0].getMethodName()+" "+flag);
                return flag ;
            }

            @Override
            public boolean areContentsTheSame(@NonNull NameDO oldItem, @NonNull NameDO newItem) {
                return  oldItem.name.equals(newItem.name);
            }
//
//            @Nullable
//            @Override
//            public Object getChangePayload(@NonNull NameDO oldItem, @NonNull NameDO newItem) {
////                return super.getChangePayload(oldItem, newItem);
//                return null;
//            }
        });
    }

    @NonNull
    @Override
    public MergeDiffAdapter.CommonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_rv_item, null);
        return new MergeDiffAdapter.CommonHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MergeDiffAdapter.CommonHolder holder, final int position) {
        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName()+" "+position);
        NameDO obj = getItem(position);
        holder.getTextView().setText(obj.id+" "+obj.name);
    }

//    @Override
//    public void onBindViewHolder(@NonNull CommonHolder holder, int position, @NonNull List<Object> payloads) {
//        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName()+" "+position+" payload");
//        super.onBindViewHolder(holder, position, payloads);
//    }

    public class CommonHolder extends RecyclerView.ViewHolder {
        private TextView tv;
        View view = null;

        public View getParentView() {
            return view;
        }

        public TextView getTextView() {
            return tv;
        }

        public CommonHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tv = (TextView) view.findViewById(R.id.tvMessage);
        }
    }
}

