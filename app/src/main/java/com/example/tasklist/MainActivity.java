package com.example.tasklist;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    //variables
    EditText edtTask;
    Button btnAdd;
    ListView listTask;

    //->List
    ArrayList<String> tasks;//ListView
    ArrayAdapter<String> adapter;

    //Shared Preferences
    SharedPreferences sharedPreferences;
    private static final String PREFS_NAME= "TaskPrefs";
    private static final String KEY_TASKS="tasks";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //variables=id
        edtTask=findViewById(R.id.edtTask);
        btnAdd=findViewById(R.id.btnAdd);
        listTask=findViewById(R.id.listTask);
        sharedPreferences=getSharedPreferences(PREFS_NAME,MODE_PRIVATE);

        //load all tasks
        tasks=new ArrayList<>(loadTasks());//ListView with tasks or empty

        adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,tasks);
        //adapter takes all tasks

        listTask.setAdapter(adapter);

        //btn Add: on click

        btnAdd.setOnClickListener(view->{
            String task=edtTask.getText().toString();
            if(!task.isEmpty()){
                tasks.add(task);
                adapter.notifyDataSetChanged();
                saveTasks();
                edtTask.setText("");
            }
            else{
                Toast.makeText(this,"Task empty",Toast.LENGTH_LONG).show();
            }
        });

        //delete element in the ListView

        listTask.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        tasks.remove(i);
                        adapter.notifyDataSetChanged();
                        saveTasks();
                    }
                }
        );


    }
    protected void onPause(){
        super.onPause();
        saveTasks();
    }
    private void saveTasks(){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Set<String> taskSet=new HashSet<>(tasks);
        editor.putStringSet(KEY_TASKS,taskSet);
        editor.apply();
    }
    private Set<String> loadTasks(){
        return sharedPreferences.getStringSet(KEY_TASKS,new HashSet<>());
    }

}