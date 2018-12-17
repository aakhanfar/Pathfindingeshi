package com.rd.ahmad.pathfindingeshi;

import android.os.Debug;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    FragmentTransaction transaction;

    Map <String,Fragment> fragments = new HashMap<String,Fragment>();

    View overlayView;
    RouteManager routeManager;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {



            switch (item.getItemId()) {
                case R.id.navigation_home:

                    loadHome();

                    return true;
                case R.id.navigation_dashboard:

                    loadDashboard();

                    return true;
                case R.id.navigation_map:

                    loadMap();

                    return true;
                case R.id.navigation_properties:
                    return true;
                case R.id.navigation_data:
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("c","Creating 5ara");
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();
        loadHome();

        routeManager = new RouteManager();

    }

    public void loadHome(){
        clearOverlay();
        transaction = fragmentManager.beginTransaction();

        if (fragments.get("Home") == null) {
            fragments.put("Home",new HomeFragment());
            transaction.replace(R.id.containfrg,fragments.get("Home")).commit();
        }else{
            transaction.replace(R.id.containfrg,fragments.get("Home")).commit();
        }
    }

    public void loadDashboard(){
        clearOverlay();
        transaction = fragmentManager.beginTransaction();

        if (fragments.get("Dashboard") == null) {
            DashboardFragment dashboardFragment = new DashboardFragment();
            dashboardFragment.mainActivity = this;
            fragments.put("Dashboard",new DashboardFragment());
            transaction.replace(R.id.containfrg,fragments.get("Dashboard")).commit();
        }else{
            transaction.replace(R.id.containfrg,fragments.get("Dashboard")).commit();
        }
    }

    public void loadMap(){
        clearOverlay();
        transaction = fragmentManager.beginTransaction();

        //if (fragments.get("Map") == null) {
            MapFragment frg = new com.rd.ahmad.pathfindingeshi.MapFragment();
            frg.getRouteManager(routeManager);
            fragments.put("Map",frg);
            transaction.replace(R.id.containfrg,fragments.get("Map")).commit();
        //}else{
            //transaction.replace(R.id.containfrg,fragments.get("Map")).commit();
        //}

    }

    public void loadOverlay(){
        clearOverlay();

        FrameLayout frameLayout = findViewById(R.id.overlayfrg);
        if (overlayView != null)
            frameLayout.addView(overlayView);

        transaction = fragmentManager.beginTransaction();

        if (fragments.get("Overlay") == null) {
            fragments.put("Overlay",new selectPointFragment());
            transaction.replace(R.id.overlayfrg,fragments.get("Overlay")).commit();
        }else{
            transaction.replace(R.id.overlayfrg,fragments.get("Overlay")).commit();
        }
    }

    void clearOverlay(){

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.overlayfrg,new Fragment()).commit();
        if (fragments.get("Overlay") != null)
            transaction.remove(fragments.get("Overlay"));
    }

    public void onClickSS(View v){

        DashboardFragment frg = (DashboardFragment)fragments.get("Dashboard");
        frg.mainActivity = this;
        frg.onClickSS(v);


    }

    public void onClickSE(View v) {

        DashboardFragment frg = (DashboardFragment)fragments.get("Dashboard");
        frg.mainActivity = this;
        frg.onClickSE(v);


    }

    public void onClickSP(View v){

        MapFragment frg = (MapFragment)fragments.get("Map");
        DashboardFragment frg2 = (DashboardFragment)fragments.get("Dashboard");
        selectPointFragment frg3 = (selectPointFragment)fragments.get("Overlay");
        frg3.onClickSP(v);
        frg.getMapCoordinates(frg2.selectingPoint);
        renameButton(frg2.selectingPoint);
        loadDashboard();

    }

    public void onClickCL(View v){

        MapFragment frg = (MapFragment)fragments.get("Map");
        DashboardFragment frg2 = (DashboardFragment)fragments.get("Dashboard");
        routeManager.Calculate();
        frg2.onClickCL(v);
    }

    public void renameButton(int i){

        MapFragment frg = (MapFragment)fragments.get("Map");
        DashboardFragment frg2 = (DashboardFragment)fragments.get("Dashboard");

        if (i == 1){
            Button buttonSS = frg2.view.findViewById(R.id.buttonSS);
            buttonSS.setText(frg.routeManager.startPoint.Lat + "," + frg.routeManager.startPoint.Lng);
        } else if (i == 2){
            Button buttonSE = frg2.view.findViewById(R.id.buttonSE);
            buttonSE.setText(frg.routeManager.endPoint.Lat + "," + frg.routeManager.endPoint.Lng);
        }
    }




}
