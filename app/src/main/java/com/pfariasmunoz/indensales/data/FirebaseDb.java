package com.pfariasmunoz.indensales.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Pablo Farias on 16-04-17.
 */

public class FirebaseDb {

    public static final FirebaseDatabase sIndenDbRef = FirebaseDatabase.getInstance();
    public static final DatabaseReference sClientsRef = sIndenDbRef.getReference(DbContract.CLIENTS_ND);
    public static final DatabaseReference sArticlesRef = sIndenDbRef.getReference(DbContract.ARTICLES_ND);
    public static final DatabaseReference sClientAdressRef = sIndenDbRef.getReference(DbContract.CLIENT_ADRES_ND);
    public static final DatabaseReference sArticlesSalesRef = sIndenDbRef.getReference(DbContract.ARTICLES_SALES_ND);
    public static final DatabaseReference sSalesRef = sIndenDbRef.getReference(DbContract.SALES_ND);


    public static String getUid(DatabaseReference databaseReference) {
        String url = databaseReference.toString();
        if (url.contains("/")) {
            return url.substring(url.lastIndexOf("/") + 1, url.length());
        } else {
            return "Nothing found";
        }
    }
}
