package com.example.flo;

public class Note {

    String noteID;
    String noteDate;
    String noteTime;
    String noteContent;

    public Note() {

    }

    public Note(String noteID, String noteDate, String noteTime, String noteContent) {
        this.noteID = noteID;
        this.noteDate = noteDate;
        this.noteTime = noteTime;
        this.noteContent = noteContent;
    }

    public String getnoteID() {
        return noteID;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public String getnoteTime() {
        return noteTime;
    }

    public String getnoteContent() {
        return noteContent;
    }
}
