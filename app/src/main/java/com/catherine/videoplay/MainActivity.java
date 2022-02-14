package com.catherine.videoplay;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;

import com.catherine.videoplay.model.Destination;
import com.catherine.videoplay.model.User;
import com.catherine.videoplay.ui.login.UserManager;
import com.catherine.videoplay.util.AppConfig;
import com.catherine.videoplay.util.NavGraphBuilder;
import com.catherine.videoplay.view.AppBottomBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private NavController navController;
    private AppBottomBar navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
/*       AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();*/
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

//        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController = NavHostFragment.findNavController(fragment);

        navView.setOnNavigationItemSelectedListener(this);
        /*NavigationUI.setupWithNavController(navView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        navController.handleDeepLink(getIntent());*/
        NavGraphBuilder.build(navController, this, fragment.getId());
        navView.setOnNavigationItemSelectedListener(this);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        HashMap<String, Destination> destConfig = AppConfig.getDestConfig();
        Iterator<Map.Entry<String, Destination>> iterator = destConfig.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Destination> entry = iterator.next();
            Destination value = entry.getValue();
            if (value != null &&
                    !UserManager.get().isLogin() && value.isNeedLogin()
                    && value.getId() == item.getItemId()) {
                UserManager.get().login(this).observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        navView.setSelectedItemId(item.getItemId());
                    }
                });
                return false;
            }
        }
        navController.navigate(item.getItemId());
        return !TextUtils.isEmpty(item.getTitle());
    }
}