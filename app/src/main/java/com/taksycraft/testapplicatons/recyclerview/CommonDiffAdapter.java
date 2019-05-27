package com.taksycraft.testapplicatons.recyclerview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.taksycraft.testapplicatons.R;
import com.taksycraft.testapplicatons.common.NameDO;

import java.util.List;
import java.util.Vector;


public class CommonDiffAdapter extends RecyclerView.Adapter<CommonDiffAdapter.CommonHolder> {
    private String TAG = CommonDiffAdapter.class.getSimpleName();
    private CommonDiffAdapter.ListListener listener;
    public Vector<NameDO> vec = new Vector<NameDO>();

    public CommonDiffAdapter(Vector<NameDO> vec, CommonDiffAdapter.ListListener listener) {
        this.vec = vec;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommonDiffAdapter.CommonHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.common_rv_item, null);
        return new CommonDiffAdapter.CommonHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommonDiffAdapter.CommonHolder holder, final int position) {
        listener.onBindViewHolder(holder, position);
        holder.getParentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(vec.get(position), position);
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull CommonHolder holder, int position, @NonNull List<Object> payloads) {
        Log.e(TAG, new Exception().getStackTrace()[0].getMethodName()+" with payloads "+position );
        if(payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads);
        else {
            onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return vec != null ? vec.size() : 0;
    }

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


    public class MyDiffUtils extends DiffUtil.Callback {

        private Vector<NameDO> oldList;
        private Vector<NameDO> newList;

        public MyDiffUtils(Vector<NameDO> oldList, Vector<NameDO> newList) {
            this.newList = newList;
            this.oldList = oldList;
        }

        @Override
        public int getOldListSize() {
            return oldList != null ? oldList.size() : 0;
        }

        @Override
        public int getNewListSize() {
            return newList != null ? newList.size() : 0;
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return ((NameDO) newList.get(newItemPosition)).id
                    .equalsIgnoreCase(((NameDO) oldList.get(oldItemPosition)).id);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + "  " + oldItemPosition
            + " - " + newItemPosition);
            return  !((NameDO) newList.get(newItemPosition)).name
                    .equalsIgnoreCase(((NameDO) oldList.get(oldItemPosition)).name);
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {

            NameDO newModel = newList.get(newItemPosition);
            NameDO oldModel = oldList.get(oldItemPosition);
            boolean isTrue=false;
            if (!newModel.name .equalsIgnoreCase(oldModel.name))
                  isTrue = true;
            Log.e(TAG, new Exception().getStackTrace()[0].getMethodName() + " old " + oldItemPosition
                    + " new positiotn " + newItemPosition+"  "+isTrue);
            if(isTrue)
                return newModel;
            else
                return super.getChangePayload(oldItemPosition, newItemPosition);
        }
    }

    public void updateList(final Vector<NameDO> vecNew) {
        MyDiffUtils diffUtils = new MyDiffUtils(vec, vecNew);
        final DiffUtil.DiffResult diffResult =   DiffUtil.calculateDiff(diffUtils,true );
        diffResult.dispatchUpdatesTo(CommonDiffAdapter.this);
        vec.clear();
        vec.addAll(vecNew);
    }

    public void refresh(Vector<NameDO> vec) {
        this.vec = vec;
        notifyDataSetChanged();
    }

    public interface ListListener {
        public void onBindViewHolder(CommonDiffAdapter.CommonHolder holder, int position);

        public void onItemClick(Object object, int position);
    }

}

