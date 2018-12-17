package com.rd.ahmad.pathfindingeshi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class DashboardFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public MainActivity mainActivity;
    public int selectingPoint;

    View view;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view==null)
            view = inflater.inflate(R.layout.fragment_dashboared, container, false);
        return view;
    }

    public void onClickSS(View v){

        mainActivity.loadMap();
        mainActivity.loadOverlay();
        selectingPoint = 1;

    }

    public void onClickSE(View v){

        mainActivity.loadMap();
        mainActivity.loadOverlay();
        selectingPoint = 2;

    }

    public void onClickCL(View v){
        mainActivity.loadMap();
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
