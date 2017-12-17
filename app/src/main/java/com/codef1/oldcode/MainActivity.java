package com.codef1.oldcode;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PWAdapter mAdapter;
    private PWViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ViewModel
        mViewModel = ViewModelProviders.of(this).get(PWViewModel.class);
        List<Password> arrayList = mViewModel.getData();

        //View
        ListView listView = findViewById(R.id.lvPw);
        mAdapter = new PWAdapter(this, arrayList);
        listView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_pw:
                final LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                final EditText titleEditText = new EditText(this);
                final EditText valueEditText = new EditText(this);
                titleEditText.setHint("密码名");
                valueEditText.setHint("密码值");
                linearLayout.addView(titleEditText);
                linearLayout.addView(valueEditText);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("添加新的密码")
                        .setView(linearLayout)
                        .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = String.valueOf(titleEditText.getText());
                                String value = valueEditText.getText().toString();
                                //ViewModel
                                Password password = mViewModel.addPassword(title, value);
                                // VIEW
                                mAdapter.add(password);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deletePw(View view) {
        View parent = (View) view.getParent();
        int position = (int) parent.getTag(R.id.pos);
        Password password = mAdapter.getItem(position);

        //ViewModel
        mViewModel.removePassword(password.getID());
        //View
        mAdapter.remove(password);
    }

    private class ViewHolder {
        TextView tvTitle;
        TextView tvValue;
    }

    private class PWAdapter extends ArrayAdapter<Password> {

        public PWAdapter(@NonNull Context context, List<Password> data) {
            super(context, R.layout.item, data);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Password password = getItem(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
                viewHolder.tvTitle = convertView.findViewById(R.id.tvTitle);
                viewHolder.tvValue = convertView.findViewById(R.id.tvValue);
                convertView.setTag(R.id.tag, viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag(R.id.tag);
            }
            viewHolder.tvTitle.setText(password.getTitle());
            viewHolder.tvValue.setText(password.getValue());
            convertView.setTag(R.id.pos, position);
            return convertView;
        }
    }


}
