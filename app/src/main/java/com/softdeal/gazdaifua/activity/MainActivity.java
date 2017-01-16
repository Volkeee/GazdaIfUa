package com.softdeal.gazdaifua.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.softdeal.gazdaifua.R;
import com.softdeal.gazdaifua.fragment.AdvertisementFragment;
import com.softdeal.gazdaifua.fragment.WelcomeFragment;
import com.softdeal.gazdaifua.model.Category;
import com.softdeal.gazdaifua.service.ConnectionManager;

public class MainActivity extends AppCompatActivity
        implements AdvertisementFragment.OnFragmentInteractionListener, WelcomeFragment.OnFragmentInteractionListener {
    private ConnectionManager mConnectionManager;
    private FragmentManager mFragmentManager;
    private Integer mQuantity;
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mConnectionManager = new ConnectionManager(this);
        mFragmentManager = getSupportFragmentManager();
        mQuantity = 10;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_drawer_logo);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, getString(R.string.descr_floatbutton), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(view -> mDrawer.openDrawer(GravityCompat.START));

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                item -> {
                    selectDrawerItem(item);
                    return true;
                }
        );
        Fragment fragment = WelcomeFragment.newInstance(mQuantity);
        swapFragment(fragment, getString(R.string.title_latest));
    }

    @Override
    protected void onPostResume() {
        mConnectionManager.requestCategories();

        super.onPostResume();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent browserIntent = null;

        if (id == R.id.nav_developers) {
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConnectionManager.linkBuilder));
        } else if (id == R.id.nav_notary) {
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConnectionManager.linkNotary));
        } else if (id == R.id.nav_estimation) {
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConnectionManager.linkEstimations));
        } else if (id == R.id.nav_repair) {
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConnectionManager.linkRepair));
        } else if (id == R.id.nav_website) {
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConnectionManager.linkWebsite));
        } else if (id == R.id.nav_homepage) {
            Fragment fragment = WelcomeFragment.newInstance(mQuantity);
            swapFragment(fragment, getString(R.string.title_latest));
        }

        if (browserIntent != null) {
            startActivity(browserIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment;
        Integer categoryNumber;
        switch (menuItem.getItemId()) {
            case R.id.nav_1room:
                categoryNumber = 1;
                break;
            case R.id.nav_2rooms:
                categoryNumber = 2;
                break;
            case R.id.nav_5rooms:
                categoryNumber = 4;
                break;
            case R.id.nav_commercials:
                categoryNumber = 9;
                break;
            case R.id.nav_cottages:
                categoryNumber = 7;
                break;
            case R.id.nav_garages:
                categoryNumber = 8;
                break;
            case R.id.nav_ground_sections:
                categoryNumber = 6;
                break;
            case R.id.nav_mansions:
                categoryNumber = 5;
                break;
            case R.id.nav_3rooms:
                categoryNumber = 3;
                break;
            default:
                categoryNumber = 1;
        }
        fragment = AdvertisementFragment.newInstance(new Category(categoryNumber));
        swapFragment(fragment, menuItem.getTitle().toString());

        menuItem.setChecked(true);

        mDrawer.closeDrawers();
    }

    private void swapFragment(Fragment fragment, String title) {
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().replace(R.id.content_main, fragment, "AdsList").commit();
        setTitle(title);
    }
}
