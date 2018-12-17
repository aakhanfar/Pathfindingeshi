package com.rd.ahmad.pathfindingeshi;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback {


    private OnFragmentInteractionListener mListener;


    SupportMapFragment mapFragment;
    //MapView mapFragment;
    GoogleMap Map;
    View mView;
    RouteManager routeManager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        if (mView == null){
            mView = inflater.inflate(R.layout.fragment_map, container, false);

        }
        if (googleServicesCheck()) {
            initializeMap();
        }
        return mView;

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        Map = googleMap;

        if (routeManager.startPoint.Lat != 0){
            googleMap.addMarker(new MarkerOptions().position(routeManager.startPoint.getLatLng()).title("First station"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(routeManager.startPoint.getLatLng()));
        }
        if (routeManager.endPoint.Lat != 0){
            googleMap.addMarker(new MarkerOptions().position(routeManager.endPoint.getLatLng()).title("Last station"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(routeManager.endPoint.getLatLng()));
        }

        if (routeManager.stations.size() != 0){
            PolylineOptions line = new PolylineOptions();
            for (RouteManager.Station s : routeManager.stations){
                line.clickable(true).add(s.getLatLng());
            }

            Map.addPolyline(line);

        }

    }


    private void initializeMap() {

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //mapFragment = mView.findViewById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

    }



    private boolean googleServicesCheck() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(getContext());

        if (isAvailable == ConnectionResult.SUCCESS){
            return true;
        } else if (api.isUserResolvableError(isAvailable)){
            Toast.makeText(getContext(),"Google service not available", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getContext(),"Google service not available", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    
    public void getMapCoordinates(int i){
        if (i == 1) {
            routeManager.startPoint.setLocation(Map.getCameraPosition().target.latitude,Map.getCameraPosition().target.longitude);
            Map.addMarker(new MarkerOptions().position(Map.getCameraPosition().target).title("Starting point"));
        }else if (i == 2){
            routeManager.endPoint.setLocation(Map.getCameraPosition().target.latitude,Map.getCameraPosition().target.longitude);
            Map.addMarker(new MarkerOptions().position(Map.getCameraPosition().target).title("Marker in Sydney"));
        }
    }

    public void getRouteManager(RouteManager r){
        routeManager = r;
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {



        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
