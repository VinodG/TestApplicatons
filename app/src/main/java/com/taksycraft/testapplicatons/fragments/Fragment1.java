package com.taksycraft.testapplicatons.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taksycraft.testapplicatons.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1 extends Fragment {
    private  String TAG = "FRAGMENT1";
    private TextView tv;
    public Fragment1() {
        // Required empty public constructor
    }
    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"onCreate" );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG,"onCreateView" );
        return inflater.inflate(R.layout.fragment_fragment1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG,"onViewCreated" );
        tv =(TextView) view.findViewById(R.id.tv);
        tv.setText(TAG+"");
    }

    @Override
    public void onAttach(Context context) {
        Log.e(TAG,"onAttach" );
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"onResume" );
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG,"onStart" );
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG,"onStop" );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG,"onDestroyView" );
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG,"onPause" );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG,"onActivityCreated" );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG,"onDetach" );
    }


}
