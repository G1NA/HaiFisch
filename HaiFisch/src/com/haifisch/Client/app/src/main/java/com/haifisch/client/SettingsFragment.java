package com.haifisch.client;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class SettingsFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void onSet(View v) {
        String num = ((EditText) getActivity().findViewById(R.id.num_text)).getText().toString();
        try {
            int actual = Integer.parseInt(num);
            if (actual > 0)
                Master.topK = actual;
        } catch (Exception e) {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
