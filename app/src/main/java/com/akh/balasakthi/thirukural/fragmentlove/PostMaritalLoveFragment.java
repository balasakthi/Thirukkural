package com.akh.balasakthi.thirukural.fragmentlove;

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

public class PostMaritalLoveFragment extends Fragment {

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
                Log.d("PreMaritalFragment", "DB Copied success");
            } else {
                Log.d("PreMaritalFragment ", "DB not Copied");
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
        chapterAdapter = ArrayAdapter.createFromResource(getContext(), R.array.chapter_thepostmaritallove_titles, android.R.layout.simple_spinner_item);
        chapterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chapterSpinner.setAdapter(chapterAdapter);
        chapterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    start = 1151;
                    end = 1160;
                }
                if (i == 1) {
                    start = 1161;
                    end = 1170;
                }
                if (i == 2) {
                    start = 1171;
                    end = 1180;
                }
                if (i == 3) {
                    start = 1181;
                    end = 1190;
                }
                if (i == 4) {
                    start = 1191;
                    end = 1200;
                }
                if (i == 5) {
                    start = 1201;
                    end = 1210;
                }
                if (i == 6) {
                    start = 1211;
                    end = 1220;
                }
                if (i == 7) {
                    start = 1221;
                    end = 1230;
                }
                if (i == 8) {
                    start = 1231;
                    end = 1240;
                }
                if (i == 9) {
                    start = 1241;
                    end = 1250;
                }
                if (i == 10) {
                    start = 1251;
                    end = 1260;
                }
                if (i == 11) {
                    start = 1261;
                    end = 1270;
                }
                if (i == 12) {
                    start = 1271;
                    end = 1280;
                }
                if (i == 13) {
                    start = 1281;
                    end = 1290;
                }
                if (i == 14) {
                    start = 1291;
                    end = 1300;
                }
                if (i == 15) {
                    start = 1301;
                    end = 1310;
                }
                if (i == 16) {
                    start = 1311;
                    end = 1320;
                }
                if (i == 17) {
                    start = 1321;
                    end = 1330;
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
