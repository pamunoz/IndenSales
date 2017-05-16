package com.pfariasmunoz.indensales.data;

import android.view.View;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Article;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.data.models.Sale;
import com.pfariasmunoz.indensales.ui.viewholders.ClientViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.http.PUT;

/**
 * Created by Pablo Farias on 16-04-17.
 */

public class FirebaseDb {

    public static final FirebaseDatabase sIndenDbRef = FirebaseDatabase.getInstance();
    public static final DatabaseReference sArticlesRef = sIndenDbRef.getReference(DbContract.ARTICLES_ND);
    public static final DatabaseReference sClientAdressRef = sIndenDbRef.getReference(DbContract.CLIENT_ADDRESS_ND);
    public static final DatabaseReference sClientsRef = sIndenDbRef.getReference(DbContract.CLIENTS_ND);
    public static final DatabaseReference sSaleReportRef = sIndenDbRef.getReference(DbContract.SALE_REPORTS);
    public static final DatabaseReference sArticlesSalesRef = sIndenDbRef.getReference(DbContract.ARTICLES_SALE_ND);
    public static final DatabaseReference sSalesRef = sIndenDbRef.getReference(DbContract.SALES_ND);

    public static String getUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


    public static String getUid(DatabaseReference databaseReference) {
        String url = databaseReference.toString();
        if (url.contains("/")) {
            return url.substring(url.lastIndexOf("/") + 1, url.length());
        } else {
            return "Nothing found";
        }
    }

    public static Query getClientsNameQuery(String newName) {
        return sClientsRef.orderByChild(DbContract.CLIENT_NAME_KEY).startAt(newName);
    }

    public static Query getClientsRutQuery(String newRut) {
        return sClientsRef.orderByChild(DbContract.CLIENT_RUT_KEY).startAt(newRut);
    }

    public static Query getArticlesCodeQuery(String newCode) {
        return sArticlesRef.orderByKey().equalTo(newCode);
    }

    public static Query getArticlesDescriptionQuery(String newDescription) {
        return sArticlesRef.orderByChild(DbContract.ARTICLES_DESCRIPTION_KEY).startAt(newDescription);
    }

}
