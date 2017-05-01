package com.pfariasmunoz.indensales.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Article;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.data.models.Sale;
import com.pfariasmunoz.indensales.ui.fragments.ArticlesFragment;
import com.pfariasmunoz.indensales.ui.fragments.SalesBarFragment;
import com.pfariasmunoz.indensales.ui.viewholders.ArticleViewHolder;
import com.pfariasmunoz.indensales.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AddSaleActivity extends AppCompatActivity {



    private Map<String, Integer> mArticlesMap = new HashMap<>();
    private ArrayList<ArticleSale> mArticleSales = new ArrayList<>();
    private String mClientId;
    private FirebaseUser mUser;
    private String mUserId;
    private String mClientAddressId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sale);

        mClientId = getIntent().getStringExtra(Constants.CLIENT_ID_KEY);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserId = mUser.getUid();
        mClientAddressId = getIntent().getStringExtra(Constants.ADDRESS_ID_KEY);
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

    public String getClientId() {
        return mClientId;
    }

    public FirebaseUser getUser() {
        return mUser;
    }

    public String getUserId() {
        return mUserId;
    }

    public Sale createSale() {
        boolean aprob = false;
        long currentTime = System.currentTimeMillis();
        String time = String.valueOf(currentTime);
        String clientId = mClientId;
        String clientAddressId = mClientAddressId;
        String userId = mUserId;
        Long totalPrice = getTotalPrice();
        return new Sale(aprob, time, clientId, clientAddressId, userId, totalPrice);
    }

    public void addArticleSale(Article article, String articleId) {
        Iterator it = mArticlesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int amount = mArticlesMap.get(articleId);
            long price = Long.valueOf(article.getPrecio());
            Long totalPrice = amount * price;
            ArticleSale articleSale = new ArticleSale(amount, totalPrice);
            mArticleSales.add(articleSale);
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    private Long getTotalPrice() {
        Long result = 0L;
        for (ArticleSale articleSale : mArticleSales) {
            result += articleSale.getTotal();
        }
        return result;
    }

}
