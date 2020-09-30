package com.example.projectlayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;
    private AppBarConfiguration beforeLoginAppBarConfiguration;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //init
        drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_alarm,R.id.nav_dream,R.id.nav_wants,R.id.bookFragment)
                .setOpenableLayout(drawer)
                .build();

        //set up another configuration for before login
        beforeLoginAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.bookFragment,R.id.loginFragment)
                .setOpenableLayout(drawer)
                .build();


        //init
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("Check","");

        //check the user login and show different navigation drawer for not login and logged in
        if(name.equals("Login")){
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);
        }else{
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.login_drawer);
            NavigationUI.setupActionBarWithNavController(this, navController, beforeLoginAppBarConfiguration);


            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    //a promo toast msg for locked function and set up navigation for home,book and login
                    switch(id)
                    {
                        case R.id.alarm:
                        case R.id.wants:
                        case R.id.dream:
                            drawer.close();
                            Toast.makeText(getBaseContext() , "Please login to gain full access ", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.nav_home:
                            drawer.close();
                            navController.navigate(R.id.nav_home);
                            break;
                        case R.id.bookFragment:
                            drawer.close();
                            navController.navigate(R.id.bookFragment);
                            break;
                        case R.id.loginFragment:
                            drawer.close();
                            navController.navigate(R.id.loginFragment);
                            break;
                    }

                    return true;
                }

            });
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("Check","");
        if(name.equals("Login")){
            return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                    || super.onSupportNavigateUp();
        }else{
            return NavigationUI.navigateUp(navController, beforeLoginAppBarConfiguration)
                    || super.onSupportNavigateUp();
        }

    }
}

