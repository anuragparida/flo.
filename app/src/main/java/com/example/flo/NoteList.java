package com.example.flo;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NoteList extends ArrayAdapter<Note> {

    private Activity context;
    private List<Note> NoteList;

    public NoteList(Activity context, List<Note> NoteList) {
        super(context, R.layout.note_layout, NoteList);
        this.context = context;
        this.NoteList = NoteList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.note_layout, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textView2);
        TextView textViewInt = (TextView) listViewItem.findViewById(R.id.textView3);

        Note note = NoteList.get(position);

        textViewName.setText(note.noteDate);
        textViewInt.setText(note.noteContent);

        return listViewItem;
    }
}












