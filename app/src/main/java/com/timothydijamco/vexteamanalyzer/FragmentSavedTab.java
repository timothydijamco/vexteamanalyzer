package com.timothydijamco.vexteamanalyzer;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentSearchTab.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentSearchTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSavedTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab, container, false);
        TextView textview = (TextView) view.findViewById(R.id.tabtextview);
        textview.setText("Saved"); // TODO: Set to string resource
        return view;
    }

}
