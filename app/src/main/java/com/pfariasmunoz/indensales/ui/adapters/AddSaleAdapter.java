package com.pfariasmunoz.indensales.ui.adapters;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Article;

import java.util.List;

/**
 * Created by Pablo Farias on 02-05-17.
 */

public class AddSaleAdapter
        extends RecyclerView.Adapter<AddSaleAdapter.ArticleViewHolder>
        {

    private List<Article> mArticles;

    public AddSaleAdapter(List<Article> articleList) {
        this.mArticles = articleList;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

            @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Article article = mArticles.get(position);
        holder.mArticlePriceTextView.setText(article.getPrecio());
        holder.mArticleDescriptionTextView.setText(article.getDescripcion());
        holder.mArticleTotalPriceTextView.setText(article.getPrecio());
        holder.mArticleAmountTextView.setText(String.valueOf(0));
    }
    public class ArticleViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{
        TextView mArticleDescriptionTextView;
        TextView mArticlePriceTextView;
        TextView mArticleAmountTextView;
        TextView mArticleTotalPriceTextView;

        // Buttons
        ImageView mArticleImageView;
        ImageButton mAddArticleImageButton;
        ImageButton mSubstracArticleImageButton;


        public ArticleViewHolder(final View itemView) {
            super(itemView);
            mArticleDescriptionTextView = (TextView) itemView.findViewById(R.id.tv_article_description);
            mArticlePriceTextView = (TextView) itemView.findViewById(R.id.tv_article_price);
            mArticleAmountTextView = (TextView) itemView.findViewById(R.id.tv_article_amount);
            mArticleTotalPriceTextView = (TextView) itemView.findViewById(R.id.tv_article_total_price);

            // references for buttons
            mArticleImageView = (ImageView) itemView.findViewById(R.id.iv_article_image);
            mAddArticleImageButton = (ImageButton) itemView.findViewById(R.id.imb_up_arrow);
            mSubstracArticleImageButton = (ImageButton) itemView.findViewById(R.id.imb_down_arrow);

            // set listeners
            mArticleImageView.setOnClickListener(this);
            mAddArticleImageButton.setOnClickListener(this);
            mSubstracArticleImageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == mArticleImageView.getId()) {
                Toast.makeText(v.getContext(), "You touched the picture", Toast.LENGTH_SHORT).show();
            } else if (v.getId() == mAddArticleImageButton.getId()) {
                Toast.makeText(v.getContext(), "You tried to ADD amount", Toast.LENGTH_SHORT).show();
            } else if (v.getId() == mSubstracArticleImageButton.getId()) {
                Toast.makeText(v.getContext(), " you tried to SUBTRACT amount", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public boolean onLongClick(View v) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle ("Hello Dialog")
                    .setMessage ("LONG CLICK DIALOG WINDOW FOR ICON " + String.valueOf(getAdapterPosition()))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            builder.create().show();
            return true;
        }
    }
}
