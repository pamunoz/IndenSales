package com.pfariasmunoz.indensales.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.ui.fragments.ArticlesFragment;
import com.pfariasmunoz.indensales.ui.fragments.SalesBarFragment;

import java.util.HashMap;
import java.util.Map;

public class AddSaleActivity extends AppCompatActivity {

    private Map<String, Integer> mArticlesMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sale);

        ArticlesFragment articlesFragment = (ArticlesFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fl_articles);
        SalesBarFragment salesBarFragment = (SalesBarFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fl_sales_bar);

    }
}
