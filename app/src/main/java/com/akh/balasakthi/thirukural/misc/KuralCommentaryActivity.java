package com.akh.balasakthi.thirukural.misc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.akh.balasakthi.thirukural.R;
import com.akh.balasakthi.thirukural.adapter.RecyclerCommentaryAdapter;
import com.akh.balasakthi.thirukural.database.CopyDB;
import com.akh.balasakthi.thirukural.database.DatabaseHelper;
import com.akh.balasakthi.thirukural.model.Thirukural;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class KuralCommentaryActivity extends AppCompatActivity {

    private DatabaseHelper mDBHelper;
    private List<Thirukural> thirukuralList;
    private RecyclerView kuralRecyclerView;
    private RecyclerCommentaryAdapter recyclerKuralAdapter;
    private Menu menu;
    private boolean isChecked = false;
    private boolean isOptionChecked=true;
    private int firstVisibleItem;
    private int start=1, end=1330;

    public static String langToloadKural;
    //For Locale
    SharedPreferences langPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configLocale();

        setContentView(R.layout.activity_kural_commentary);

        //Getting value from the intent
        Intent intent = getIntent();
        int kuralID = intent.getIntExtra("Kural_ID", 0);

        // Getting the Toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.couplet);
        setSupportActionBar(toolbar);

        //enables the backbutton
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //For Copying Database
        mDBHelper = new DatabaseHelper(this);
        File databse = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        if (false == databse.exists()) {
            mDBHelper.getReadableDatabase();
            if (CopyDB.copyDataBase(this)) {
                Log.d(this.getLocalClassName(), "DB Copied success");
            } else {
                Log.d(this.getLocalClassName(), "DB not Copied");
            }
        }

        // get the Recycler listview
        kuralRecyclerView = (RecyclerView) findViewById(R.id.kural_recyclerView);

        kuralRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //snapHelper to scroll to a position
        SnapHelper snapHelper = new PagerSnapHelper();
        kuralRecyclerView.setLayoutManager(mLayoutManager);
        snapHelper.attachToRecyclerView(kuralRecyclerView);

        kuralRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));
        kuralRecyclerView.setItemAnimator(new DefaultItemAnimator());


        new KuralTask().execute(start, end);

        // For viewing selected kural commentary
        kuralRecyclerView.getLayoutManager().scrollToPosition(kuralID - 1);


        // set a custom ScrollListner to your RecyclerView
        kuralRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // Get the first visible item
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                int disp = firstVisibleItem + 1;
                toolbar.setTitle(getResources().getString(R.string.couplet)+" "+ disp);
                //Now you can use his index to manipulate your TextView

                int favVal = mDBHelper.getFavouriteVal(disp);
                Log.d("fav value", favVal + "");
                if (favVal == 0) {
                    isChecked = false;
                } else if (favVal == 1) {
                    isChecked = true;
                }
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkable = menu.findItem(R.id.action_favourite);
        checkable.setChecked(isChecked);
        if (isChecked) {
            menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_favorite_checked));
        } else if (!isChecked) {
            menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_favorite_unchecked));
        }
       /* MenuItem checkableAlagar=menu.findItem(R.id.action_alagar);
        checkableAlagar.setChecked(isOptionChecked);*/
        return true;
    }

    // Action for the arrow in tool bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = recyclerKuralAdapter.getKuralView(firstVisibleItem);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;
            case R.id.action_favourite:
                isChecked = !item.isChecked();
                item.setChecked(isChecked);
                if (isChecked) {
                    menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_favorite_checked));
                    Toast.makeText(getApplicationContext(), R.string.favourite_added, Toast.LENGTH_SHORT).show();
                    mDBHelper.updateFavourite(firstVisibleItem + 1, 1);
                    Log.d("Added fav", firstVisibleItem + 1 + "");
                } else if (!isChecked) {
                    menu.getItem(1).setIcon(getResources().getDrawable(R.drawable.ic_favorite_unchecked));
                    Toast.makeText(getApplicationContext(), R.string.favourite_removed_single, Toast.LENGTH_SHORT).show();
                    mDBHelper.updateFavourite(firstVisibleItem + 1, 0);
                    Log.d("Removed fav", firstVisibleItem + 1 + "");
                }
                return true;
          /*  case R.id.action_manakudavar:
                if(item.isChecked()){
                    recyclerKuralAdapter.dummyFunction(8);
                    item.setChecked(false);
                }
                else if(!item.isChecked()){
                    recyclerKuralAdapter.dummyFunction(0);
                    item.setChecked(true);
                }
                return true;
            case R.id.action_alagar:
                item.setChecked(true);
                return true;
            case R.id.action_muva:
               /* if(item.isChecked()){
                   // recyclerKuralAdapter.dummyFunction(8);
                    item.setChecked(false);
                    isOptionChecked=false;
                }
                else if(!item.isChecked()){
                    //recyclerKuralAdapter.dummyFunction(0);
                    item.setChecked(true);
                    isOptionChecked=true;
                }
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kural_detail, menu);
        this.menu = menu;
        return true;
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
                thirukuralList = mDBHelper.getkuralList2(passing[0], passing[1]);

               // chaptersList=mDBHelper.getChapterTitle(passing[0], passing[1],)

                return true;
            } catch (Exception e) {
                Log.e("tag", "error", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            recyclerKuralAdapter = new RecyclerCommentaryAdapter(getApplicationContext(), thirukuralList);
            kuralRecyclerView.setAdapter(recyclerKuralAdapter);
          //  recyclerKuralAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        configLocale();
        setContentView(R.layout.activity_kural_commentary);
    }
    public void configLocale(){
        //for locale
        langPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String languageToLoad = langPrefs.getString("languagePref", Locale.getDefault().getLanguage());
        langToloadKural = languageToLoad;
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

}
