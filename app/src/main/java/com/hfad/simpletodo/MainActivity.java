package com.hfad.simpletodo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);

        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        //items.add("Add Your List Of Items");
        setupListViewListener();

    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
                        adb.setTitle("Delete?");
                        adb.setMessage("Are you sure you want to delete Item Number " + pos +"?");
                        final int positionToRemove = pos;
                        Context context = getApplicationContext();
                        adb.setNegativeButton("Cancel", null);
                        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context,"Item Removed!",Toast.LENGTH_LONG).show();
                                items.remove(positionToRemove);
                                itemsAdapter.notifyDataSetChanged();
                            }});
                        adb.show();
                        writeItems();
                        return true;
                    }
                });
    }

    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        Context context = getApplicationContext();
        Toast.makeText(context,"Item Added!",Toast.LENGTH_LONG).show();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

//    public void onDeleteAllItems(View v,int position){
//        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
//        if(items.size()>0) {
//            if (!etNewItem.getText().toString().isEmpty()) {
//                items.clear();
//                itemsAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, items);
//                lvItems.setAdapter(itemsAdapter);
//                Toast.makeText(MainActivity.this, "All Items Have Been Deleted!", Toast.LENGTH_LONG).show();
//                itemsAdapter.notifyDataSetChanged();
//                items.remove(position);
//            }
//        } else {
//            Toast.makeText(MainActivity.this, "There is nothing to delete:(", Toast.LENGTH_LONG).show();
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}