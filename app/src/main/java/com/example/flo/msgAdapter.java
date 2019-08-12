package com.example.flo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.flo.R;

public class msgAdapter extends RecyclerView.Adapter<msgAdapter.msgViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public msgAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class msgViewHolder extends RecyclerView.ViewHolder {

        public TextView msgnameText;
        public TextView submitext;
        public TextView buttonNavig;

        public msgViewHolder(@NonNull View itemView) {
            super(itemView);

            msgnameText = itemView.findViewById(R.id.textview_placename_item);
            buttonNavig = itemView.findViewById(R.id.navigate_btn);
            submitext = itemView.findViewById(R.id.textview_submit_item);

        }
    }

    @NonNull
    @Override
    public msgAdapter.msgViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.msg_item, viewGroup, false);
        return new msgAdapter.msgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull msgAdapter.msgViewHolder msgViewHolder, int i) {
        if(!mCursor.moveToPosition(i)) {
            return;
        }//mCursor.getString(2)
        try{
            String x = mCursor.getString(12);
            int indexofspaceplace;
            indexofspaceplace = x.indexOf("#");
            if(mCursor.getString(2).equals("+919891016946")){
                msgViewHolder.msgnameText.setText("Rachna Ma'am");
            }
            else{
                msgViewHolder.msgnameText.setText("Sangeeta Ma'am");
            }

            msgViewHolder.buttonNavig.setText(x.substring(1,indexofspaceplace));
            msgViewHolder.submitext.setText(x.substring(indexofspaceplace+1));
            msgViewHolder.itemView.setElevation(i+5);
        } catch (Exception e){

        }

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if(mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if(newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
