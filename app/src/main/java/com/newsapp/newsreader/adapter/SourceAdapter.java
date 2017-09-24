package com.newsapp.newsreader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newsapp.newsreader.R;
import com.newsapp.newsreader.activity.SourceListActivity;
import com.newsapp.newsreader.model.Source;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by GLaDOS on 9/19/2017.
 */

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.ViewHolder> {

    private List<Source> mSources;
    private Context mContext;
    private SourceItemListener mItemListener;

    public SourceAdapter(SourceListActivity mContext, ArrayList<Source> mSources, SourceItemListener mItemListener) {
        this.mSources = mSources;
        this.mContext = mContext;
        this.mItemListener = mItemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.src_list_item, parent, false);
        return new ViewHolder(postView, this.mItemListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Source source = mSources.get(position);
        holder.tvTitle.setText(source.getName());
        holder.tvDesc.setText(source.getDescription());
        holder.tvCategory.setText(source.getCategory());
    }

    @Override
    public int getItemCount() {
        return mSources.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle, tvDesc, tvCategory;
        SourceItemListener sourceItemListener;

        ViewHolder(View itemView, SourceItemListener sourceItemListener) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_name);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
            tvCategory = (TextView) itemView.findViewById(R.id.tv_category);

            this.sourceItemListener = sourceItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Source source = getSource(getAdapterPosition());
            this.sourceItemListener.onPostClick(source);

            notifyDataSetChanged();
        }
    }

    public void updateSources(List<Source> sources) {
        mSources = sources;
        notifyDataSetChanged();
    }

    private Source getSource(int position) {
        return mSources.get(position);
    }

    public interface SourceItemListener {
        void onPostClick(Source src);
    }
}
