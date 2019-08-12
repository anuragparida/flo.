package com.example.flo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class photoFragment extends Fragment {

    DatabaseReference NoteDatabase;
    ListView listViewnotes;

    List<Note> NoteList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listViewnotes = (ListView) getActivity().findViewById(R.id.note_list);
        NoteList = new ArrayList<>();

        // Write a message to the database
        NoteDatabase = FirebaseDatabase.getInstance().getReference("Notes");
    }

    @Override
    public void onStart() {
        super.onStart();

        NoteDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                NoteList.clear();

                for (DataSnapshot NoteSnapshot : dataSnapshot.getChildren()) {
                    Note Note = NoteSnapshot.getValue(Note.class);

                    NoteList.add(Note);
                }

                NoteList adapter = new NoteList(getActivity(), NoteList);
                listViewnotes.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Retrieving Error", Toast.LENGTH_LONG).show();
            }
        });
    }



}
