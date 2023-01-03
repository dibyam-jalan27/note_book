package com.svnit.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    EditText title;
    EditText description;
    Button cancel;
    Button save;
    int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Update Note");
        setContentView(R.layout.activity_update);

        title = findViewById(R.id.editTextTitleUpdate);
        description = findViewById(R.id.editTextDescriptionUpdate);
        cancel = findViewById(R.id.buttonCancelUpdate);
        save = findViewById(R.id.buttonSaveUpdate);

        getData();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Edit has been cancelled",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNote();
            }
        });
    }

    private void updateNote() {

        String titleLast = title.getText().toString();
        String descriptionLast = title.getText().toString();
        Intent intent = new Intent();
        intent.putExtra("titleLast",titleLast);
        intent.putExtra("descriptionLast",descriptionLast);
        if (noteId != -1){
            intent.putExtra("noteId",noteId);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    public void getData(){
        Intent i = getIntent();
        int noteId = i.getIntExtra("id", -1);
        String notetitle = i.getStringExtra("title");
        String notedescription = i.getStringExtra("description");

        title.setText(notetitle);
        description.setText(notedescription);
    }
}