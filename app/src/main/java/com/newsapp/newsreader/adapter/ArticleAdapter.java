package com.newsapp.newsreader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.newsapp.newsreader.R;
import com.newsapp.newsreader.activity.ArticleListActivity;
import com.newsapp.newsreader.model.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GLaDOS on 9/19/2017.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private List<Article> mArticles;
    private Context mContext;
    private ArticleItemListener mItemListener;

    public ArticleAdapter(ArticleListActivity mContext, ArrayList<Article> mArticles, ArticleItemListener mItemListener) {
        this.mArticles = mArticles;
        this.mContext = mContext;
        this.mItemListener = mItemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View postView = inflater.inflate(R.layout.article_list_item, parent, false);
        return new ViewHolder(postView, this.mItemListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Article article = mArticles.get(position);
        holder.tvTitle.setText(article.getTitle());
        holder.tvDesc.setText(article.getDescription());
        holder.tvAuthor.setText(article.getAuthor());

        Glide.with(mContext).load(article.getUrlToImage())
                .thumbnail(0.2f)
                .placeholder(R.drawable.default_placeholder)
                .error(R.drawable.error_image)
                .crossFade()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitle, tvDesc, tvAuthor;
        private ImageView imageView;
        ArticleItemListener sourceItemListener;

        ViewHolder(View itemView, ArticleItemListener sourceItemListener) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_name);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            imageView = (ImageView) itemView.findViewById(R.id.img_view);
            this.sourceItemListener = sourceItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Article article = getArticle(getAdapterPosition());
            this.sourceItemListener.onArticleClick(article);

            notifyDataSetChanged();
        }
    }

    public void updateArticles(List<Article> articles) {
        mArticles = articles;
        notifyDataSetChanged();
    }

    private Article getArticle(int position) {
        return mArticles.get(position);
    }

    public interface ArticleItemListener {
        void onArticleClick(Article article);
    }
}
