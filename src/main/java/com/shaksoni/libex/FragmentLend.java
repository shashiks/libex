package com.shaksoni.libex;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shaksoni on 9/22/17.
 */

public class FragmentLend extends Fragment {


    public FragmentLend(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_searchbook, container, false);

        return rootView;
    }

}
