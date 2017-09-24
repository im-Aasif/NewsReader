package com.newsapp.newsreader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newsapp.newsreader.R;
import com.newsapp.newsreader.activity.CategoryListActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by GLaDOS on 9/19/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<String> mCategories;
    private Context mContext;
    private CategoryItemListener mItemListener;

    public CategoryAdapter(CategoryListActivity mContext, ArrayList<String> mCategories, CategoryItemListener mItemListener) {
        this.mCategories = mCategories;
        this.mContext = mContext;
        this.mItemListener = mItemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.category_list_item, parent, false);
        return new ViewHolder(postView, this.mItemListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String category = mCategories.get(position);
        holder.tvCategory.setText(category);
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvCategory;
        CategoryItemListener categoryItemListener;

        ViewHolder(View itemView, CategoryItemListener categoryItemListener) {
            super(itemView);
            tvCategory = (TextView) itemView.findViewById(R.id.tv_category);

            this.categoryItemListener = categoryItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String category = getCategory(getAdapterPosition());
            this.categoryItemListener.onCategoryClick(category);

            notifyDataSetChanged();
        }
    }

    public void updateCategories(Set<String> categories) {
        ArrayList<String> categoryList = new ArrayList<>(categories);
        mCategories = categoryList;
        notifyDataSetChanged();
    }

    private String getCategory(int position) {
        return mCategories.get(position);
    }

    public interface CategoryItemListener {
        void onCategoryClick(String category);
    }
}
