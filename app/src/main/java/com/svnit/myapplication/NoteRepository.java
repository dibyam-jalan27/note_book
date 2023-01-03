package com.svnit.myapplication;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {
    private NoteDAO noteDAO;
    private LiveData<List<Note>> notes;

    ExecutorService service = Executors.newSingleThreadExecutor();

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application);
        noteDAO = database.noteDAO();
        notes = noteDAO.getAllNotes();


    }

    public void insert(Note note){
        service.execute(new Runnable() {
            @Override
            public void run() {
                noteDAO.insert(note);
            }
        });
        //new InsertNoteAsyncTask(noteDAO).execute(note);
    }

    public void delete(Note note){
        service.execute(new Runnable() {
            @Override
            public void run() {
                noteDAO.delete(note);
            }
        });
        //new DeleteNoteAsyncTask(noteDAO).execute(note);
    }

    public void update(Note note){
        service.execute(new Runnable() {
            @Override
            public void run() {
                noteDAO.update(note);
            }
        });
        //new UpdateNoteAsyncTask(noteDAO).execute(note);
    }

    public LiveData<List<Note>> getAllNotes(){
        return notes;
    }
/*
    private static class InsertNoteAsyncTask extends AsyncTask<Note,Void,Void>{

        private NoteDAO noteDAO;
        private InsertNoteAsyncTask(NoteDAO noteDAO){
            this.noteDAO = noteDAO;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note,Void,Void>{

        private NoteDAO noteDAO;
        private UpdateNoteAsyncTask(NoteDAO noteDAO){
            this.noteDAO = noteDAO;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note,Void,Void>{

        private NoteDAO noteDAO;
        private DeleteNoteAsyncTask(NoteDAO noteDAO){
            this.noteDAO = noteDAO;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.delete(notes[0]);
            return null;
        }
    }

 */
}
