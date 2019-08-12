package com.example.flo;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class msgFragment extends Fragment {

    private msgAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_msg, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        /*TextView adddata = getView().findViewById(R.id.addme);*/
        RecyclerView recyclerView = getView().findViewById(R.id.med_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new msgAdapter(getActivity(), getAllItems());
        recyclerView.setAdapter(mAdapter);
        mAdapter.swapCursor(getAllItems());
        /*Cursor cursor = getActivity().getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, "date DESC");

        int x = 0;
        String totaldata = "";
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx) + "\n";
                }
                x+=1;
                // use msgData
                totaldata += "\n\n" + msgData;
            } while (cursor.moveToNext() && x<10);
        } else {
            // empty box, no SMS
        }
        adddata.setText(totaldata);*/
    }

    private Cursor getAllItems() {
        //String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
        return getActivity().getContentResolver().query(Uri.parse("content://sms/inbox"), null, "(address LIKE '+919560188133' OR address LIKE '+919891016946') AND SUBSTR(body, 1, 1) LIKE '$'", null, "date DESC");
    }
}
