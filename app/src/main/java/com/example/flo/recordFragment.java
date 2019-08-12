package com.example.flo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class recordFragment extends Fragment {

    public ImageView gifcont;
    public boolean gifplaying = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_record, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        gifcont = getActivity().findViewById(R.id.recordgif);
        gifcont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!gifplaying){
                    Glide.with(recordFragment.this).load(R.drawable.record).into((ImageView) getActivity().findViewById(R.id.recordgif));
                    gifplaying = !gifplaying;
                }
                else{
                    gifcont.setImageResource(R.drawable.mic_static);
                    gifplaying = !gifplaying;
                }
            }
        });
        gifcont.setImageResource(R.drawable.mic_static);
    }


}
