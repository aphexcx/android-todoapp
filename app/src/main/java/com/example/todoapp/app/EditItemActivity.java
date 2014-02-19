package com.example.todoapp.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditItemActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment(), "EditItemFragment")
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_item, menu);
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

    public void onSaveEdit(View view) {
        PlaceholderFragment frag = (PlaceholderFragment) getSupportFragmentManager().findFragmentByTag("EditItemFragment");
        EditText etEditItem = (EditText) frag.getView().findViewById(R.id.etEditItem);
        int pos = getIntent().getIntExtra("pos", 0);
        Intent data = new Intent();
        data.putExtra("pos", pos);
        data.putExtra("text", etEditItem.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_edit_item, container, false);
            int pos = getIntent().getIntExtra("pos", 0);
            String text = getIntent().getStringExtra("text");
            EditText etEditItem = (EditText) rootView.findViewById(R.id.etEditItem);
            etEditItem.setText(text);
            etEditItem.setSelection(etEditItem.getText().length());
            return rootView;
        }
    }

}
