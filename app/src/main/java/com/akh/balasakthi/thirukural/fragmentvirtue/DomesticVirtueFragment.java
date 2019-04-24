package com.akh.balasakthi.thirukural.fragmentvirtue;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.akh.balasakthi.thirukural.R;
import com.akh.balasakthi.thirukural.adapter.RecyclerKuralAdapter;
import com.akh.balasakthi.thirukural.adapter.RecyclerTouchListener;
import com.akh.balasakthi.thirukural.database.CopyDB;
import com.akh.balasakthi.thirukural.database.DatabaseHelper;
import com.akh.balasakthi.thirukural.misc.KuralCommentaryActivity;
import com.akh.balasakthi.thirukural.model.Thirukural;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by balasakthi on 3/4/17.
 */

public class DomesticVirtueFragment extends Fragment {

    private DatabaseHelper mDBHelper;
    private List<Thirukural> thirukuralList;
    private RecyclerView kuralRecyclerView;
    private RecyclerKuralAdapter recyclerKuralAdapter;
    private Spinner chapterSpinner;
    private ArrayAdapter<CharSequence> chapterAdapter;
    private int start, end;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View x = inflater.inflate(R.layout.fragment_kuralchapters, null);

        //For Copying Database
        mDBHelper = new DatabaseHelper(getContext());
        File databse = getContext().getDatabasePath(DatabaseHelper.DBNAME);
        if (false == databse.exists()) {
            mDBHelper.getReadableDatabase();
            if (CopyDB.copyDataBase(getContext())) {
                Log.d("DomesticFragment", "DB Copied success");
            } else {
                Log.d("DomesticFragment", "DB not Copied");
            }
        }


        kuralRecyclerView = (RecyclerView) x.findViewById(R.id.kural_recyclerView);
        kuralRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        kuralRecyclerView.setLayoutManager(mLayoutManager);
        kuralRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        kuralRecyclerView.setItemAnimator(new DefaultItemAnimator());


        //For Selecting Chapters in each tabs
        chapterSpinner = (Spinner) x.findViewById(R.id.chapter_spinner);
        chapterAdapter = ArrayAdapter.createFromResource(getContext(), R.array.chapter_domestic_titles, android.R.layout.simple_spinner_item);
        chapterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chapterSpinner.setAdapter(chapterAdapter);
        chapterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    start = 41;
                    end = 50;
                } else if (i == 1) {
                    start = 51;
                    end = 60;
                } else if (i == 2) {
                    start = 61;
                    end = 70;
                } else if (i == 3) {
                    start = 71;
                    end = 80;
                } else if (i == 4) {
                    start = 81;
                    end = 90;
                } else if (i == 5) {
                    start = 91;
                    end = 100;
                } else if (i == 6) {
                    start = 101;
                    end = 110;
                } else if (i == 7) {
                    start = 111;
                    end = 120;
                } else if (i == 8) {
                    start = 121;
                    end = 130;
                } else if (i == 9) {
                    start = 131;
                    end = 140;
                } else if (i == 10) {
                    start = 141;
                    end = 150;
                } else if (i == 11) {
                    start = 151;
                    end = 160;
                } else if (i == 12) {
                    start = 161;
                    end = 170;
                } else if (i == 13) {
                    start = 171;
                    end = 180;
                } else if (i == 14) {
                    start = 181;
                    end = 190;
                } else if (i == 15) {
                    start = 191;
                    end = 200;
                } else if (i == 16) {
                    start = 201;
                    end = 210;
                } else if (i == 17) {
                    start = 211;
                    end = 220;
                } else if (i == 18) {
                    start = 221;
                    end = 230;
                } else if (i == 19) {
                    start = 231;
                    end = 240;
                }
                new KuralTask().execute(start, end);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        kuralRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), kuralRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Thirukural thirukural = thirukuralList.get(position);
                Intent intent = new Intent(getContext(), KuralCommentaryActivity.class);
                intent.putExtra("Kural_ID", thirukural.getKural_id());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return x;
    }


    private class KuralTask extends AsyncTask<Integer, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Integer... passing) {
            try {
                // preparing list data
                thirukuralList = mDBHelper.getkuralList(passing[0], passing[1]);
                return true;
            } catch (Exception e) {
                Log.e("tag", "error", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            recyclerKuralAdapter = new RecyclerKuralAdapter(getContext(), thirukuralList);
            kuralRecyclerView.setAdapter(recyclerKuralAdapter);
            recyclerKuralAdapter.notifyDataSetChanged();
        }
    }
}
