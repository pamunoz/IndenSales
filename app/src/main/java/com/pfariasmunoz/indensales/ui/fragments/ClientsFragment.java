package com.pfariasmunoz.indensales.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.ui.activities.MainActivity;
import com.pfariasmunoz.indensales.ui.adapters.ClientsAdapter;
import com.pfariasmunoz.indensales.ui.viewholders.ClientViewHolder;
import com.pfariasmunoz.indensales.utils.MathHelper;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientsFragment extends Fragment {

    public static final String TAG = ClientsFragment.class.getSimpleName();

    private RecyclerView mClientRecyclerView;
    private ClientsAdapter mClientAdapter;
    private ProgressBar mLoadingIndicatorProgressBar;
    private MainActivity mActivity;
    private Query mQuery;
    private String mDefaultQuery;

    // client info to start the sales
    public static final String CLIENT_ID_KEY = "client_id_key";

    public ClientsFragment() {
        // Required empty public constructor
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.recycler_view, container, false);
    }

    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        mQuery = FirebaseDb.sClientsRef.limitToFirst(30);
        mClientRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_content);
        mLoadingIndicatorProgressBar = (ProgressBar) rootView.findViewById(R.id.pb_loading_indicator);
        mClientRecyclerView.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mClientRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mClientRecyclerView.getContext(), layoutManager.getOrientation());
        mClientRecyclerView.addItemDecoration(dividerItemDecoration);

        mClientAdapter = new ClientsAdapter(getActivity(), mQuery);
        mClientRecyclerView.setAdapter(mClientAdapter);

    }

    // This event fires 2nd, before views are created for the fragment
    // The onCreate method is called when the Fragment instance is being created,
    // or re-created. Use onCreate for any standard setup that does not
    // require the activity to be fully created
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //setupAdapter();
    }

    @Override
    public void onPause() {
        super.onPause();
        mClientAdapter.cleanup();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mClientAdapter.cleanup();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_search) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setQueryHint(getActivity().getResources().getString(R.string.search_clients_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!TextUtils.isEmpty(newText)) {
                        if (MathHelper.isNumeric(newText)) {
                            Query numberQuery = FirebaseDb.getClientsRutQuery(newText);
                            mClientAdapter = new ClientsAdapter(getActivity(), numberQuery);
                        } else {
                            String text = newText.toUpperCase();
                            Query nameQuery = FirebaseDb.getClientsNameQuery(text);
                            mClientAdapter = new ClientsAdapter(getActivity(), nameQuery);
                        }
                        mClientAdapter.notifyDataSetChanged();
                        mClientRecyclerView.swapAdapter(mClientAdapter, false);

                    }
                    return false;
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

}
