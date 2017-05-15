package com.pfariasmunoz.indensales.ui.activities;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.ui.fragments.ClientsFragment;
import com.pfariasmunoz.indensales.ui.fragments.SalesFragment;
import com.pfariasmunoz.indensales.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    private static final String ANONYMOUS = "anonymous";
    private String mUserName = ANONYMOUS;


    // views for the drawer views
    private TextView mNavBarUserEmailTextView;
    private TextView mNavBarUserNameTextView;
    private ImageView mNavBarUserPhotoImageView;
    private DrawerLayout mDrawerLayout;

    // Firebase authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    // recuest code for firebase sign in
    // this is a flag for when we return from starting the activity for the result
    public static final int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Allow persistence of the data
        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        // Create Navigation drawer and inlfate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }




        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Initialize Firebase components
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Initialize the authentication statle listener of firebase
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // the user is signed in
                    onSignedInInitialize(user);
                    String section1 = getResources().getString(R.string.message_when_signed_in_part1);
                    String section2 = getResources().getString(R.string.message_when_signed_in_part2);
                    String message = section1 + user.getDisplayName() + section2;
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                } else {
                    onSignedOutCleanup();
                    // the user is signed out, so, launch the sign in flow
                    startSignInFlow();
                }
            }
        };

    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ClientsFragment(), "Clients");
        adapter.addFragment(new SalesFragment(), "My Sales");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void onSignedOutCleanup() {
        mUserName = ANONYMOUS;
    }

    private void onSignedInInitialize(FirebaseUser user) {
        if (user != null) {
            mUserName = user.getDisplayName();
            String userEmail = user.getEmail();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            mNavBarUserEmailTextView = (TextView) headerView.findViewById(R.id.tv_email_nav_bar);
            mNavBarUserNameTextView = (TextView) headerView.findViewById(R.id.tv_user_name_nav_bar);
            mNavBarUserPhotoImageView = (ImageView) headerView.findViewById(R.id.imv_user_photo);
            if (!TextUtils.isEmpty(userEmail)) {
                mNavBarUserEmailTextView.setText(userEmail);
            }
            if (!TextUtils.isEmpty(mUserName)) {
                mNavBarUserNameTextView.setText(mUserName);
            }

            if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl().toString()).into(mNavBarUserPhotoImageView);
            }
        }

    }

    private void startSignInFlow() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setProviders(Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()))
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Signed In!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Signed In Canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_log_out) {
            // Handle singing out of the app
            AuthUI.getInstance().signOut(this);
        } else if (id == R.id.nav_articles_fragemnt) {
            //initializeFragment(new ArticlesFragment());
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * This method start the sales activity if the number of adresses is less than 2.
     * That's because if it is more than 2, it starts a nother fragment with the list
     * of adresses to choose of the client with the id provided.
     * @param numberOfAdresses
     * @param clientId
     */
    public void startSalesActivity(long numberOfAdresses, String clientId, String addressId) {
        if (numberOfAdresses < 2) {
            Intent intent = new Intent(this, CreateSaleActivity.class);
            intent.putExtra(Constants.CLIENT_ID_KEY, clientId);
            if (addressId != null) {
                intent.putExtra(Constants.ADDRESS_ID_KEY, addressId);
            }
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ClientAddressesActivity.class);
            intent.putExtra(Constants.CLIENT_ID_KEY, clientId);
            startActivity(intent);
        }
    }



}
