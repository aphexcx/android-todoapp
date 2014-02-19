package com.example.todoapp.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

public class TodoActivity extends ActionBarActivity {
    private static final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(), "MainFragment")
                    .commit();
        }
//        Fragment frag = getSupportFragmentManager().findFragmentByTag("MainFragment");
//        View fragView = getSupportFragmentManager().findFragmentByTag("MainFragment").getView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onAddedItem(View view) {
        PlaceholderFragment frag = (PlaceholderFragment) getSupportFragmentManager().findFragmentByTag("MainFragment");
        View fragView = frag.getView();
        EditText etNewItem = (EditText) fragView.findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        frag.addItem(itemText);
        frag.writeItems();
        etNewItem.setText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            PlaceholderFragment frag = (PlaceholderFragment) getSupportFragmentManager().findFragmentByTag("MainFragment");
            int pos = data.getExtras().getInt("pos");
            String text = data.getExtras().getString("text");
            frag.setItem(pos, text);

        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment {
        private ArrayList<String> todoItems;
        private ArrayAdapter<String> aTodoItems;
        private ListView lvItems;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            lvItems = (ListView) rootView.findViewById(R.id.lvItems);
            readItems();
            aTodoItems = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, todoItems);
            lvItems.setAdapter(aTodoItems);
            setupListViewListener();
            return rootView;
        }

        private void setupListViewListener() {
            lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    todoItems.remove(i);
                    aTodoItems.notifyDataSetChanged();
                    writeItems();
                    return true;
                }
            });

            lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(TodoActivity.this, EditItemActivity.class);
                    intent.putExtra("pos", i);
                    intent.putExtra("text", todoItems.get(i));
                    getActivity().startActivityForResult(intent, REQUEST_CODE);

                    aTodoItems.notifyDataSetChanged();
                    writeItems();
                }
            });
        }

        private void readItems() {
            File filesDir = getFilesDir();
            File todoFile = new File(filesDir, "todo.txt");
            try {
                todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
            } catch (IOException e) {
                todoItems = new ArrayList<String>();
            }

        }

        private void writeItems() {
            File filesDir = getFilesDir();
            File todoFile = new File(filesDir, "todo.txt");
            try {
                FileUtils.writeLines(todoFile, todoItems);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void addItem(String itemText) {
            aTodoItems.add(itemText);
        }

        public void setItem(int pos, String itemText) {
            todoItems.set(pos, itemText);
            aTodoItems.notifyDataSetChanged();
            writeItems();
        }

    }
}
