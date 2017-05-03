package com.pfariasmunoz.indensales.data;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pfariasmunoz.indensales.data.models.Article;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.data.models.Sale;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Pablo Farias on 16-04-17.
 */

public class FirebaseDb {

    public static final FirebaseDatabase sIndenDbRef = FirebaseDatabase.getInstance();
    public static final DatabaseReference sClientsRef = sIndenDbRef.getReference(DbContract.CLIENTS_ND);
    public static final DatabaseReference sArticlesRef = sIndenDbRef.getReference(DbContract.ARTICLES_ND);
    public static final DatabaseReference sClientAdressRef = sIndenDbRef.getReference(DbContract.CLIENT_ADRES_ND);
    public static final DatabaseReference sArticlesSalesRef = sIndenDbRef.getReference(DbContract.ARTICLES_SALE_ND);
    public static final DatabaseReference sSalesRef = sIndenDbRef.getReference(DbContract.SALES_ND);


    public static String getUid(DatabaseReference databaseReference) {
        String url = databaseReference.toString();
        if (url.contains("/")) {
            return url.substring(url.lastIndexOf("/") + 1, url.length());
        } else {
            return "Nothing found";
        }
    }

    public void createArticleSale(Article article, Sale sale) {
        ArrayList<ArticleSale> articleSales = new ArrayList<>();

    }

    public Sale createSale(String currentClientId, FirebaseUser user, String currentAddressId) {
        boolean isApproved = false;
        long currentTime = System.currentTimeMillis();
        String stringCurrentTime = String.valueOf(currentTime);
        String clientId = currentClientId;
        String addressId = currentAddressId;
        String userId = user.getUid();
        long total = 0;
        return new Sale(isApproved, stringCurrentTime, clientId, addressId, userId, total);
    }

    public void createSale(
            HashMap<String,
            Integer> articlesMap,
            String clientId,
            String clientAddressId, String userId,
            Long totalPrice) {
        boolean aprob = false;
        long currentTime = System.currentTimeMillis();
        String time = String.valueOf(currentTime);
        Sale sale =  new Sale(aprob, time, clientId, clientAddressId, userId, totalPrice);
        FirebaseDb.sSalesRef.push().setValue(sale);
    }

    public void addAllTotals(HashMap<String, Integer> map, long totalSalePrice) {

    }
}
