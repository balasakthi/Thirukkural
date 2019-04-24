package com.akh.balasakthi.thirukural.misc;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akh.balasakthi.thirukural.R;
import com.akh.balasakthi.thirukural.adapter.RecyclerKuralAdapter;
import com.akh.balasakthi.thirukural.adapter.RecyclerTouchListener;
import com.akh.balasakthi.thirukural.database.CopyDB;
import com.akh.balasakthi.thirukural.database.DatabaseHelper;
import com.akh.balasakthi.thirukural.model.Thirukural;

import java.io.File;
import java.util.List;
import java.util.Locale;

public class FavouriteKuralActivity extends AppCompatActivity {

    private DatabaseHelper mDBHelper;
    private List<Thirukural> thirukuralList;
    private RecyclerView favKuralView;
    private RecyclerKuralAdapter recyclerKuralAdapter;
    private Paint p = new Paint();
    private TextView emptyView;
    private ImageView emptyImage;

    public static String langToloadKural;
    //For Locale
    SharedPreferences langPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configLocale();

        setContentView(R.layout.activity_favourite_kural);

        // Getting the Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.favourites);
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
        favKuralView = (RecyclerView) findViewById(R.id.FavkuralRecyclerView);

        //Getting the Empty Text and Image
        emptyView = (TextView) findViewById(R.id.emptyTxt);
        emptyImage = (ImageView) findViewById(R.id.emptyImg);


        favKuralView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        favKuralView.setLayoutManager(mLayoutManager);
        favKuralView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        favKuralView.setItemAnimator(new DefaultItemAnimator());


        new KuralTask().execute();


        favKuralView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), favKuralView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Thirukural thirukural = thirukuralList.get(position);
                Intent intent = new Intent(getApplicationContext(), KuralCommentaryActivity.class);
                intent.putExtra("Kural_ID", thirukural.getKural_id());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        initSwipe();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_fav_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        new KuralTask().execute();
        super.onResume();
    }

    // Action for the arrow in tool bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        switch (item.getItemId()) {
            case R.id.removeFav:
                if (!thirukuralList.isEmpty()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.favourite_remove_title);
                    builder.setMessage(R.string.favourite_remove_msg);
                    // Add the buttons
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            recyclerKuralAdapter.removeAllFavItems();
                            dislayEmptyView(getResources().getString(R.string.favourite_empty), R.drawable.ic_love);
                           /* Snackbar snackbar = Snackbar
                                    .make(findViewById(android.R.id.content), getResources().getString(R.string.favourite_removed), Snackbar.LENGTH_LONG);
                            snackbar.show();*/
                        }
                    });
                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog

                        }
                    });
                    // Create the AlertDialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
          /*  case R.id.setAllFav://For testing
                mDBHelper.addAllFavourites();
                Snackbar snackbar1 = Snackbar
                        .make(findViewById(android.R.id.content), "All Setted as Favourties", Snackbar.LENGTH_LONG);
                snackbar1.show();
                recyclerKuralAdapter.notifyDataSetChanged();
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final Thirukural mThirukural = thirukuralList.get(position);
                if (direction == ItemTouchHelper.LEFT) {
                    recyclerKuralAdapter.removeOrUpdateFavItem(position, 0);
                    Snackbar snackbar = Snackbar
                            .make(viewHolder.itemView, getResources().getString(R.string.favourite_removed), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.undo), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d("position", String.valueOf(position));
                                    recyclerKuralAdapter.addFavItems(position, mThirukural);
                                    /*Snackbar snackbar1 = Snackbar.make(view, getResources().getString(R.string.favourite_restored), Snackbar.LENGTH_SHORT);
                                    snackbar1.show();*/
                                    new KuralTask().execute();
                                }
                            });
                    snackbar.show();
                    new KuralTask().execute();
                    favKuralView.getLayoutManager().scrollToPosition(position);
                } else {

                    recyclerKuralAdapter.removeOrUpdateFavItem(position, 0);
                    Snackbar snackbar = Snackbar
                            .make(viewHolder.itemView, getResources().getString(R.string.favourite_removed), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.undo), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d("position", String.valueOf(position));
                                    recyclerKuralAdapter.addFavItems(position, mThirukural);
                                   /* Snackbar snackbar1 = Snackbar.make(view, getResources().getString(R.string.favourite_restored), Snackbar.LENGTH_SHORT);
                                    snackbar1.show();*/
                                    new KuralTask().execute();
                                }
                            });
                    snackbar.show();
                    new KuralTask().execute();
                    favKuralView.getLayoutManager().scrollToPosition(position);
                }
            }


            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        Log.d("dx Value:", dX + "");
                        p.setColor(Color.parseColor("#D32F2F")); // 388E3C
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                    if (dX < 0) {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(favKuralView);
    }

    public void dislayEmptyView(String emptyMessage, int resID) {
        favKuralView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        emptyView.setText(emptyMessage);
        emptyImage.setVisibility(View.VISIBLE);
        emptyImage.setBackgroundResource(resID);//R.drawable.ic_sentiment_dissatisfied);
    }

    public void hideEmptyView() {
        favKuralView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        emptyImage.setVisibility(View.GONE);
    }


    private class KuralTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... passing) {
            try {
                // getting the favourite kural form database
                thirukuralList = mDBHelper.getFavKuralList();
                return true;
            } catch (Exception e) {
                Log.e("tag", "error", e);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            recyclerKuralAdapter = new RecyclerKuralAdapter(getApplicationContext(), thirukuralList);
            favKuralView.setAdapter(recyclerKuralAdapter);
            recyclerKuralAdapter.notifyDataSetChanged();

            if (thirukuralList.isEmpty()) {
                dislayEmptyView(getResources().getString(R.string.favourite_empty), R.drawable.ic_love);
            } else {
                hideEmptyView();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        configLocale();
        setContentView(R.layout.activity_favourite_kural);
    }

    public void configLocale() {
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