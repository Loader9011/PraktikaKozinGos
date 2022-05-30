package com.example.praktikakozin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText edName;
    EditText edCount;
    EditText edColors;
    DatabaseReference mDatabase;
    String stateKey = "State";
    public int stateCount = 0;
    ArrayList<String> stateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView1 = (ListView) findViewById(R.id.listView1);
        mDatabase = FirebaseDatabase.getInstance().getReference(stateKey);
        edName = findViewById(R.id.edName);
        edCount = findViewById(R.id.edCount);
        edColors = findViewById(R.id.edColors);
        stateText = new ArrayList<String>(){};

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stateText);
        listView1.setAdapter(arrayAdapter);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    State state = ds.getValue(State.class);
                    String name = (String) ds.child("name").getValue();
                    String count = (String) ds.child("count").getValue();
                    String colors = (String) ds.child("colors").getValue();
                    stateText.add(name + " с численностью населения: " + count + "; и цветами флага: " + colors);
                }
                mDatabase.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }

    public void onClickSave(android.view.View view) {
        String name = edName.getText().toString();
        String count = edCount.getText().toString();
        String colors = edColors.getText().toString();
        Toast.makeText(MainActivity.this, "Данные добавлены!", Toast.LENGTH_LONG).show();
        stateText.add(name + " с численностью населения: " + count + "; и цветами флага: " + colors);
        State state = new State(name, count, colors);
        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(count) && !TextUtils.isEmpty(colors)){
            mDatabase.push().setValue(state);
        }
    }
}