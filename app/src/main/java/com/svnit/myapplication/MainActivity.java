package com.svnit.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    ActivityResultLauncher<Intent> activityResultLauncherForAddNotes;
    ActivityResultLauncher<Intent> activityResultLauncherForUpdateNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerActivityForAddNotes();
        setActivityResultLauncherForUpdateNotes();

        RecyclerView recyclerView = findViewById(R.id.recyle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(NoteViewModel.class);

        noteViewModel.getAllNotes().observe(this, adapter::setNotes);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNote(viewHolder.getAdapterPosition()));
                Toast.makeText(getApplicationContext(),"Note is deleted",Toast.LENGTH_LONG).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(note -> {
            Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
            intent.putExtra("title", note.getTitle());
            intent.putExtra("description",note.getDescription());
            intent.putExtra("id", note.getId());

            activityResultLauncherForUpdateNotes.launch(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.new_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.top_menu) {
            Intent intent = new Intent(MainActivity.this, Add_note_activity.class);
            //startActivity(intent);
            activityResultLauncherForAddNotes.launch(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void setActivityResultLauncherForUpdateNotes(){
        activityResultLauncherForUpdateNotes = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            int resultCode = result.getResultCode();
            Intent data = result.getData();
            if(resultCode == RESULT_OK && data != null){
                String title = data.getStringExtra("titleLast");
                String description = data.getStringExtra("descriptionLast");
                int id = data.getIntExtra("noteId",-1);

                Note note = new Note(title,description);
                note.setId(id);
                noteViewModel.update(note);
            }


        });
    }


    public void registerActivityForAddNotes(){
        activityResultLauncherForAddNotes = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            int resultCode = result.getResultCode();
            Intent data = result.getData();
            if(resultCode == RESULT_OK && data != null){
                String title = data.getStringExtra("noteTitle");
                String description = data.getStringExtra("noteDescription");

                Note note = new Note(title,description);
                noteViewModel.insert(note);

            }
        });
    }
}