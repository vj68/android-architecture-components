package com.example.architecturefirst;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class},version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract  NoteDao noteDao();

    public static synchronized  NoteDatabase getInstance(Context context){
         if( instance == null){
             instance = Room.databaseBuilder(context.getApplicationContext(),
                     NoteDatabase.class,"notedb")
                     .fallbackToDestructiveMigration()
                     .addCallback(roomCallback)
                     .build();
         }
         return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void,Void,Void>{
        private NoteDao noteDao;
        private PopulateDBAsyncTask(NoteDatabase noteDatabase){
            this.noteDao = noteDatabase.noteDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            for(int i=0;i<100;i++)
            noteDao.insert(new Note("T"+i,"D"+i,i+1));

            return null;
        }
    }
}
