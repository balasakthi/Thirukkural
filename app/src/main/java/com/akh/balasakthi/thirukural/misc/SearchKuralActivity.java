package com.akh.balasakthi.thirukural.misc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akh.balasakthi.thirukural.R;
import com.akh.balasakthi.thirukural.adapter.RecyclerSearchAdapter;
import com.akh.balasakthi.thirukural.adapter.RecyclerTouchListener;
import com.akh.balasakthi.thirukural.database.CopyDB;
import com.akh.balasakthi.thirukural.database.DatabaseHelper;
import com.akh.balasakthi.thirukural.model.Thirukural;

import java.io.File;
import java.util.List;
import java.util.Locale;


public class SearchKuralActivity extends AppCompatActivity {

    private DatabaseHelper mDBHelper;
    private List<Thirukural> thirukuralList;
    private RecyclerView searchKuralRecyclerView;
    private RecyclerSearchAdapter recyclerKuralAdapter;
    private TextView emptySearchView, emptySearchSubView, searchResultView;
    private ImageView emptySearchImage;
    private LinearLayout searchResultsLayout, emptySearchMsgLayout;

    public static String langToloadKural;
    //For Locale
    SharedPreferences langPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configLocale();

        setContentView(R.layout.activity_search_kural);

        // Getting the Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Search Results");
        setSupportActionBar(toolbar);

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
        searchKuralRecyclerView = (RecyclerView) findViewById(R.id.searchRecyclerView);

        //Getting the Empty Text and Image and Search Result disp text
        emptySearchView = (TextView) findViewById(R.id.emptySearchTxt);
        emptySearchImage = (ImageView) findViewById(R.id.emptySearchImg);
        emptySearchSubView = (TextView) findViewById(R.id.empySearchSubTxt);
        searchResultView = (TextView) findViewById(R.id.searchResultTxt);
        searchResultsLayout=(LinearLayout) findViewById(R.id.searchResultLayout);
        emptySearchMsgLayout=(LinearLayout) findViewById(R.id.emptySearchLayout);
        searchResultsLayout.setVisibility(View.GONE);

        // getting the kural form database
        thirukuralList = mDBHelper.getkuralList2(1, 1330);
        recyclerKuralAdapter = new RecyclerSearchAdapter(this, thirukuralList);
        searchKuralRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        searchKuralRecyclerView.setLayoutManager(mLayoutManager);
        searchKuralRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        searchKuralRecyclerView.setItemAnimator(new DefaultItemAnimator());

        searchKuralRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), searchKuralRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Thirukural thirukural = recyclerKuralAdapter.getItem(position);
                //Toast.makeText(getApplicationContext(), thirukural.getKural_id() + "", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), KuralCommentaryActivity.class);
                intent.putExtra("Kural_ID", thirukural.getKural_id());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      /*  getMenuInflater().inflate(R.menu.main, menu);
        return true;*/
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_kural_search, menu);
        MenuItem item = menu.findItem(R.id.kuralSearch);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search));
        item.expandActionView();

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    searchResultView.setText(null);
                    searchKuralRecyclerView.setVisibility(View.GONE);
                    searchResultsLayout.setVisibility(View.GONE);
                    emptySearchMsgLayout.setVisibility(View.GONE);
                } else {
                    recyclerKuralAdapter.filter(newText);
                    searchKuralRecyclerView.setAdapter(recyclerKuralAdapter);
                    if (!thirukuralList.isEmpty()) {
                        if (recyclerKuralAdapter.getItemCount() == 0) {
                            searchResultView.setText(null);
                            dislayEmptyView(getResources().getString(R.string.no_search_result),
                                    getResources().getString(R.string.no_search_subresult),R.drawable.ic_sentiment_dissatisfied);
                        } else {
                            hideEmptyView();
                            searchResultView.setText(String.valueOf(recyclerKuralAdapter.getItemCount()));
                            }
                    }
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    public void dislayEmptyView(String emptyMessage, String emptySubMessage, int resID) {
        searchKuralRecyclerView.setVisibility(View.GONE);
        emptySearchView.setText(emptyMessage);
        emptySearchSubView.setText(emptySubMessage);
        emptySearchImage.setBackgroundResource(resID);
        searchResultsLayout.setVisibility(View.GONE);
        emptySearchMsgLayout.setVisibility(View.VISIBLE);
    }

    public void hideEmptyView() {
        searchKuralRecyclerView.setVisibility(View.VISIBLE);
        searchResultsLayout.setVisibility(View.VISIBLE);
        emptySearchMsgLayout.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        configLocale();
        setContentView(R.layout.activity_search_kural);
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