package com.project.sagi.queue;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.project.sagi.queue.Fragment.Search;
import com.project.sagi.queue.Fragment.SignUp;
import com.project.sagi.queue.Fragment.SignUpBusiness;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DrawerLayout.DrawerListener {

    private Fragment _currentFragment;
    private SignUp _SignUp;
    public SignUp GetSignUp() {
        if (_SignUp == null) {
            _SignUp = new SignUp();
        }
        return _SignUp;
    }

    private SignUpBusiness _SignUpBusiness;

    public SignUpBusiness GetSignUpBusiness() {
        if (_SignUpBusiness == null) {
            _SignUpBusiness = new SignUpBusiness();
        }
        return _SignUpBusiness;
    }

    private Search _Search;

    public Search GetSearch() {
        if (_Search == null) {
            _Search = new Search();
        }
        return _Search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clsGlobalHelper.setLocalLanguage(getBaseContext());
        setContentView(R.layout.activity_main_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.addDrawerListener(this);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        _currentFragment = GetSearch();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.mainFrame, _currentFragment).commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (_currentFragment instanceof SignUpBusiness){
            ((SignUpBusiness)_currentFragment).setImage(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
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
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_sign_up) {
            _currentFragment = GetSignUp();
        } else if (id == R.id.nav_sign_up_business) {
            _currentFragment = GetSignUpBusiness();
        } else if (id == R.id.nav_search) {
            _currentFragment = GetSearch();
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.mainFrame, _currentFragment).commit();
        //getFragmentManager().getFragments()
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
        clsGlobalHelper.closeKeyboard(drawerView, this);
    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {}

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {}

    @Override
    public void onDrawerStateChanged(int newState) {}
}
