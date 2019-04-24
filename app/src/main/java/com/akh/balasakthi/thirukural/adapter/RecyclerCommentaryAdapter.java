package com.akh.balasakthi.thirukural.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akh.balasakthi.thirukural.R;
import com.akh.balasakthi.thirukural.misc.KuralCommentaryActivity;
import com.akh.balasakthi.thirukural.model.Thirukural;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by balasakthi on 23/3/17.
 */

public class RecyclerCommentaryAdapter extends RecyclerView.Adapter<RecyclerCommentaryAdapter.MyViewHolder> {

    private List<Thirukural> thirukuralList;
    private Context context;
    private String language = KuralCommentaryActivity.langToloadKural;

    public LinearLayout manakudavarLayout, alagarLayout, alagarIntroLayout;
    public TextView chapterIntroTitle;

    public int pos1, pos2;

    private String text, commentary, result;

    public RecyclerCommentaryAdapter(Context context, List<Thirukural> thirukuralList) {
        this.thirukuralList = thirukuralList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView kuralTxtView, commentryTxtView, kuralIdTxtView, kuralSectionTxtView,
                kuralChapterTxtView, kuralPartTxtView, kuralAlagarTxtView, kuralManakudavarTxtView,
                kuralAlagarCommentaryTxtView, chapterIntroTxtView;


        public MyViewHolder(View view) {
            super(view);
            kuralTxtView = (TextView) view.findViewById(R.id.kuralView);
            commentryTxtView = (TextView) view.findViewById(R.id.commentaryView);
            //  kuralIdTxtView = (TextView) view.findViewById(R.id.kuralNoView);
            kuralSectionTxtView = (TextView) view.findViewById(R.id.kuralSectionView);
            kuralChapterTxtView = (TextView) view.findViewById(R.id.kuralChapterView);
            kuralPartTxtView = (TextView) view.findViewById(R.id.kuralPartView);
            kuralAlagarTxtView = (TextView) view.findViewById(R.id.alagarKuralView);
            kuralAlagarCommentaryTxtView = (TextView) view.findViewById(R.id.alagarCommentryView);
            kuralManakudavarTxtView = (TextView) view.findViewById(R.id.manakudavarCommentaryView);
            chapterIntroTxtView = (TextView) view.findViewById(R.id.alagarChapterIntroView);

            manakudavarLayout = (LinearLayout) view.findViewById(R.id.manakudavarLayout);
            alagarLayout = (LinearLayout) view.findViewById(R.id.alagarLayout);
            alagarIntroLayout = (LinearLayout) view.findViewById(R.id.alagarIntroLayout);
            chapterIntroTitle = (TextView) view.findViewById(R.id.alagarIntroTitle);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.kural_commentary_recyclerview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Thirukural thirukural = thirukuralList.get(position);

        try {
            if (language.equalsIgnoreCase("en")) {
                // holder.kuralIdTxtView.setText(String.valueOf(thirukural.getKural_id()));
                manakudavarLayout.setVisibility(View.GONE);
                alagarLayout.setVisibility(View.GONE);
                alagarIntroLayout.setVisibility(View.GONE);
                holder.kuralTxtView.setText(thirukural.getEnglish_kural());
                holder.commentryTxtView.setText(thirukural.getEnglish_commentary());
                holder.kuralChapterTxtView.setText(thirukural.getChapter_english());
                holder.kuralSectionTxtView.setText(thirukural.getSection_english());
                holder.kuralPartTxtView.setText(thirukural.getPart_english());
            } else if (language.equalsIgnoreCase("ta")) {
                // holder.kuralIdTxtView.setText(String.valueOf(thirukural.getKural_id()));
                manakudavarLayout.setVisibility(View.VISIBLE);
                alagarLayout.setVisibility(View.VISIBLE);
                holder.kuralTxtView.setText(thirukural.getTamil_kural());
                holder.commentryTxtView.setText(thirukural.getTamil_commentary());
                holder.kuralChapterTxtView.setText(thirukural.getChapter_tamil());
                holder.kuralSectionTxtView.setText(thirukural.getSection_tamil());
                holder.kuralPartTxtView.setText(thirukural.getPart_tamil());
                holder.kuralManakudavarTxtView.setText(thirukural.getTamil_manakudavar());
                try {
                    // Match lowercase words.
                    Pattern pat = Pattern.compile("[*]");
                    String strs[] = pat.split(thirukural.getTamil_alagar());
                    for (int i = 0; i < strs.length; i++) {
                        text = strs[0];
                        commentary = strs[1];
                    }
                    pos1 = text.indexOf("[");
                    pos2 = text.indexOf("]");
                    if (pos1 != -1 && pos2 != -1) {
                        result = text.substring(pos1 + 1, pos2);
                        text = text.substring(pos2 + 2);
                        alagarIntroLayout.setVisibility(View.VISIBLE);
                        holder.chapterIntroTxtView.setText(result);
                    } else {
                        alagarIntroLayout.setVisibility(View.GONE);
                        pos1 = -1;
                        pos2 = -1;
                    }

                    holder.kuralAlagarTxtView.setText(text);
                    holder.kuralAlagarCommentaryTxtView.setText(commentary);


                } catch (Exception e) {
                    e.printStackTrace();

                }

            } else {
                // holder.kuralIdTxtView.setText(String.valueOf(thirukural.getKural_id()));
                manakudavarLayout.setVisibility(View.GONE);
                alagarLayout.setVisibility(View.GONE);
                alagarIntroLayout.setVisibility(View.GONE);
                holder.kuralTxtView.setText(thirukural.getEnglish_kural());
                holder.commentryTxtView.setText(thirukural.getEnglish_commentary());
                holder.kuralChapterTxtView.setText(thirukural.getChapter_english());

                holder.kuralSectionTxtView.setText(thirukural.getSection_english());
                holder.kuralPartTxtView.setText(thirukural.getPart_english());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return thirukuralList.size();
    }

    public void dummyFunction(int v) {
        manakudavarLayout.setVisibility(v);
    }

    public String getKuralView(int position) {
        String sharedKural = null;
        if (language.equalsIgnoreCase("en")) {
            sharedKural = String.valueOf(context.getString(R.string.Section)+": "+thirukuralList.get(position).getSection_english()+"\n\n"+
                    context.getString(R.string.part)+": "+ thirukuralList.get(position).getPart_english()+"\n\n" +
                    context.getString(R.string.chapter)+": "+thirukuralList.get(position).getChapter_english()+"\n\n"+
                    context.getString(R.string.couplet) +": " + thirukuralList.get(position).getKural_id() + "\n" +
                    thirukuralList.get(position).getEnglish_kural() + "\n\n" +context.getString(R.string.commentary)+":\n" +
                    thirukuralList.get(position).getEnglish_commentary());
        } else if (language.equalsIgnoreCase("ta")) {
            sharedKural = String.valueOf(context.getString(R.string.Section)+": "+thirukuralList.get(position).getSection_tamil() + "\n\n" +
                    context.getString(R.string.part)+": "+thirukuralList.get(position).getPart_tamil() + "\n\n" +
                    context.getString(R.string.chapter)+": "+thirukuralList.get(position).getChapter_tamil()+"\n\n"+
                    context.getString(R.string.couplet) +": "+thirukuralList.get(position).getKural_id() + "\n" +
                    thirukuralList.get(position).getTamil_kural() + "\n\n" +context.getString(R.string.commentary)+":\n" +
                    thirukuralList.get(position).getTamil_commentary() + "\n\n" +context.getString(R.string.manakudavar_text)+":\n" +
                    thirukuralList.get(position).getTamil_manakudavar());
        } else {
            sharedKural = String.valueOf(context.getString(R.string.Section)+": "+thirukuralList.get(position).getSection_english()+"\n\n"+
                    context.getString(R.string.part)+": "+ thirukuralList.get(position).getPart_english()+"\n\n" +
                    context.getString(R.string.chapter)+": "+thirukuralList.get(position).getChapter_english()+"\n\n"+
                    context.getString(R.string.couplet) +": " + thirukuralList.get(position).getKural_id() + "\n" +
                    thirukuralList.get(position).getEnglish_kural() + "\n\n" +context.getString(R.string.commentary)+":\n" +
                    thirukuralList.get(position).getEnglish_commentary());
        }
        return sharedKural;
    }
}
