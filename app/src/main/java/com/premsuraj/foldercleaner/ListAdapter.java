package com.premsuraj.foldercleaner;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Premsuraj
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ItemHolder> {
    private final OnDeleteClickListener mClickListener;
    private List<String> mItems;

    public ListAdapter(List<String> items, OnDeleteClickListener listener) {
        this.mItems = items;
        this.mClickListener = listener;
    }

    public void setItems(List<String> items) {
        this.mItems = items;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.mItemValue.setText(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView mItemValue;
        ImageView mDeleteButton;

        ItemHolder(View itemView) {
            super(itemView);
            mDeleteButton = (ImageView) itemView.findViewById(R.id.btn_delete);
            mItemValue = (TextView) itemView.findViewById(R.id.txt_item);
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mClickListener != null) {
                        mClickListener.onDeleteClicked(getAdapterPosition(),
                                mItems.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClicked(int position, String itemValue);
    }
}
