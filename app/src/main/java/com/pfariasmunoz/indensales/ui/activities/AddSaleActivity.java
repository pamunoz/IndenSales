package com.pfariasmunoz.indensales.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Sale;
import com.pfariasmunoz.indensales.ui.fragments.ArticlesFragment;
import com.pfariasmunoz.indensales.ui.fragments.SalesBarFragment;
import com.pfariasmunoz.indensales.ui.viewholders.ArticleViewHolder;
import com.pfariasmunoz.indensales.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class AddSaleActivity extends AppCompatActivity {



    private Map<String, Integer> mArticlesMap = new HashMap<>();
    private String mClientId;
    private FirebaseUser mUser;
    private String userId;

    public String getClientId() {
        return mClientId;
    }

    public FirebaseUser getUser() {
        return mUser;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sale);

        mClientId = getIntent().getStringExtra(Constants.CLIENT_ID_KEY);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = mUser.getUid();
    }



    public void addArticle(String articleKey, ArticleViewHolder viewHolder) {
        if (!mArticlesMap.isEmpty()) {
            if (mArticlesMap.containsKey(articleKey)) {
                mArticlesMap.put(articleKey, mArticlesMap.get(articleKey) + 1);
            } else {
                mArticlesMap.put(articleKey, 1);
            }
        } else {
            mArticlesMap.put(articleKey, 1);
        }
        viewHolder.setTotalPrice(mArticlesMap.get(articleKey));
        viewHolder.setAmount(mArticlesMap.get(articleKey));
    }

    public void subtractArticle(String articleKey, ArticleViewHolder viewHolder) {
        if (mArticlesMap.size() == 0) {
            return;
        } else if (mArticlesMap.containsKey(articleKey)) {
            // if the amount of this article is 0, remove it from the map
            if (mArticlesMap.get(articleKey) <= 1) {
                mArticlesMap.remove(articleKey);
                viewHolder.setTotalPrice(0);
                viewHolder.setAmount(0);
            } else {
                mArticlesMap.put(articleKey, mArticlesMap.get(articleKey) - 1);
                viewHolder.setTotalPrice(mArticlesMap.get(articleKey));
                viewHolder.setAmount(mArticlesMap.get(articleKey));
            }
        } else {
            return;
        }
    }

    public Map<String, Integer> getArticlesMap() {
        return mArticlesMap;
    }

    public Sale createSale() {
        long currentTime = System.currentTimeMillis();
        return new Sale();
    }
}
