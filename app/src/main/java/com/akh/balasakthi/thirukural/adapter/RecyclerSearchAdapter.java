package com.akh.balasakthi.thirukural.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akh.balasakthi.thirukural.R;
import com.akh.balasakthi.thirukural.database.DatabaseHelper;
import com.akh.balasakthi.thirukural.misc.SearchKuralActivity;
import com.akh.balasakthi.thirukural.model.Thirukural;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by balasakthi on 23/3/17.
 */

public class RecyclerSearchAdapter extends RecyclerView.Adapter<RecyclerSearchAdapter.MyViewHolder> {

    private List<Thirukural> thirukuralList;
    private Context context;
    private List<Thirukural> thirukuralCpyList;
    private String mSearchText = "";
    private String lang = SearchKuralActivity.langToloadKural;

    public RecyclerSearchAdapter(Context context, List<Thirukural> thirukuralList) {
        this.thirukuralList = thirukuralList;
        this.context = context;
        this.thirukuralCpyList = thirukuralList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView kuralNoTxtView, KuralTxtView, chapterTxtView, chapterNoView;

        public MyViewHolder(View view) {
            super(view);
            kuralNoTxtView = (TextView) view.findViewById(R.id.kuralNoTxtView);
            KuralTxtView = (TextView) view.findViewById(R.id.kuralTxtView);
            chapterTxtView = (TextView) view.findViewById(R.id.chapterTxtView);
            chapterNoView=(TextView) view.findViewById(R.id.chapterNoView);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kural_search_recyclerview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Thirukural thirukural = thirukuralList.get(position);

        if (lang.equalsIgnoreCase("en")) {
            holder.kuralNoTxtView.setText(context.getResources().getString(R.string.couplet)+": "+Integer.toString(thirukural.getKural_id()));
            holder.chapterNoView.setText(String.valueOf(thirukural.getChapter_id()));
            holder.KuralTxtView.setText(Integer.toString(thirukural.getKural_id())+thirukural.getEnglish_kural());
            holder.chapterTxtView.setText(thirukural.getChapter_english());
            String fullText = thirukuralList.get(position).getEnglish_kural();
            spannableText(holder, fullText, thirukural.getEnglish_kural());
        } else if (lang.equalsIgnoreCase("ta")) {
            holder.kuralNoTxtView.setText(context.getResources().getString(R.string.couplet)+": "+Integer.toString(thirukural.getKural_id()));
            holder.chapterNoView.setText(String.valueOf(thirukural.getChapter_id()));
            holder.KuralTxtView.setText(Integer.toString(thirukural.getKural_id())+thirukural.getTamil_kural());
            holder.chapterTxtView.setText(thirukural.getChapter_tamil());
            String fullText = thirukuralList.get(position).getTamil_kural();
            spannableText(holder, fullText, thirukural.getTamil_kural());
        } else {
            holder.kuralNoTxtView.setText(context.getResources().getString(R.string.couplet)+": "+Integer.toString(thirukural.getKural_id()));
            holder.chapterNoView.setText(String.valueOf(thirukural.getChapter_id()));
            holder.KuralTxtView.setText(Integer.toString(thirukural.getKural_id())+thirukural.getEnglish_kural());
            holder.chapterTxtView.setText(thirukural.getChapter_english());
            String fullText = thirukuralList.get(position).getEnglish_kural();
            spannableText(holder, fullText, thirukural.getEnglish_kural());
        }
    }

    public void spannableText(MyViewHolder holder, String fullText, String kural) {
        if (mSearchText != null && !mSearchText.isEmpty()) {
            int startPos = fullText.toLowerCase(Locale.US).indexOf(mSearchText.toLowerCase(Locale.US));
            int endPos = startPos + mSearchText.length();
            if (startPos != -1) {
                Spannable spannable = new SpannableString(kural);
                // ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.BLUE});
                BackgroundColorSpan backgroundSpan = new BackgroundColorSpan(Color.YELLOW);
                //   ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.RED);
                // TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                //   spannable.setSpan(foregroundSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(backgroundSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.KuralTxtView.setText(spannable);
            } else {
                holder.KuralTxtView.setText(fullText);
            }
        } else {
            holder.KuralTxtView.setText(fullText);
        }
    }

    @Override
    public int getItemCount() {
        return thirukuralList.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        this.mSearchText = charText;
        thirukuralList = new ArrayList<>();
        if (charText.length() == 0) {
            thirukuralList.addAll(thirukuralCpyList);
        } else {
            for (Thirukural client : thirukuralCpyList) {

                final String textTamilKural = client.getTamil_kural().toLowerCase();
                final String textEnglishKural = client.getEnglish_kural().toLowerCase();
                final String textKuralID = String.valueOf(client.getKural_id());
                final String chapterTamilTitle = client.getChapter_tamil().toLowerCase();
                final String chapterEnglishTitle = client.getChapter_english().toLowerCase();

                if (lang.equalsIgnoreCase("ta")) {
                    if (textTamilKural.contains(charText) || textKuralID.contains(charText) || chapterTamilTitle.contains(charText)) {
                        thirukuralList.add(client);
                    }
                } else if (lang.equalsIgnoreCase("en")) {
                    if (textEnglishKural.contains(charText) || textKuralID.contains(charText) || chapterEnglishTitle.contains(charText)) {
                        thirukuralList.add(client);
                    }
                } else {
                    if (textEnglishKural.contains(charText) || textKuralID.contains(charText) || chapterTamilTitle.contains(charText)) {
                        thirukuralList.add(client);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }


    public Thirukural getItem(int position) {
        return thirukuralList.get(position);
    }

    //Updates the single kural Favourite value either 0 or 1
    public void removeOrUpdateFavItem(int position, int favourite) {
        DatabaseHelper mDBHelper = new DatabaseHelper(context);
        if (favourite == 0) {
            mDBHelper.updateFavourite(thirukuralList.get(position).getKural_id(), favourite);
            thirukuralList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, thirukuralList.size());
        }
    }

    //Adding the Favourite kural while UNDO
    public void addFavItems(int postition, Thirukural thirukural) {
        DatabaseHelper mDBHelper = new DatabaseHelper(context);
        mDBHelper.updateFavourite(thirukural.getKural_id(), 1);
        thirukuralList.add(postition, thirukural);
        notifyItemInserted(postition);
    }

    //Update the all kural Favourite value to 0
    public void removeAllFavItems() {
        DatabaseHelper mDBHelper = new DatabaseHelper(context);
        int size = this.thirukuralList.size();
        this.thirukuralList.clear();
        notifyItemRangeRemoved(0, size);
        mDBHelper.removeFavourite();
    }
}
