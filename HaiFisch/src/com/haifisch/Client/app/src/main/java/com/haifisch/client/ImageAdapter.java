package com.haifisch.client;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private final List<String> mValues;
    private final OnImageInteractionListener mListener;

    public ImageAdapter(List<String> items, OnImageInteractionListener listener) {

        mValues = items;

        mListener = listener;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        return null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_pic_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mValues.get(position);
        Picasso.with(holder.mView.getContext())
                .load(mValues.get(position))
                .into((ImageView) holder.mView.findViewById(R.id.imageView));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.OnImageInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
        }
    }
}
