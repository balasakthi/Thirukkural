package com.akh.balasakthi.thirukural.model;

/**
 * Created by balasakthi on 15/11/16.
 */

public class Thirukural {

    private int kural_id;
    private int chapter_id;
    private String tamil_kural;
    private String tamil_commentary;
    private String tamil_manakudavar;
    private String tamil_alagar;
    private String english_kural;
    private String english_commentary;
    private int favourite;
    private int part_id;
    private int chapter_id2;
    private String chapter_tamil;
    private String chapter_english;
    private int section_id;
    private int part_id2;
    private String part_tamil;
    private String part_english;
    private int section_id2;
    private String section_tamil;
    private String section_english;


    public Thirukural(int kural_id, int chapter_id, String tamil_kural, String tamil_commentary, String tamil_manakudavar, String tamil_alagar,
                      String english_kural, String english_commentary, int favourite) {
        this.kural_id = kural_id;
        this.chapter_id = chapter_id;
        this.tamil_kural = tamil_kural;
        this.tamil_commentary = tamil_commentary;
        this.tamil_manakudavar = tamil_manakudavar;
        this.tamil_alagar = tamil_alagar;
        this.english_kural = english_kural;
        this.english_commentary = english_commentary;
        this.favourite = favourite;
    }


    public Thirukural(int kural_id, int chapter_id, String tamil_kural, String tamil_commentary, String tamil_manakudavar,
                      String tamil_alagar, String english_kural, String english_commentary, int favourite,
                      int part_id, int chapter_id2, String chapter_tamil, String chapter_english,
                      int section_id, int part_id2, String part_tamil, String part_english,
                      int section_id2, String section_tamil, String section_english) {
        this.kural_id = kural_id;
        this.chapter_id = chapter_id;
        this.tamil_kural = tamil_kural;
        this.tamil_commentary = tamil_commentary;
        this.tamil_manakudavar = tamil_manakudavar;
        this.tamil_alagar = tamil_alagar;
        this.english_kural = english_kural;
        this.english_commentary = english_commentary;
        this.favourite = favourite;
        this.part_id = part_id;
        this.chapter_id2 = chapter_id2;
        this.chapter_tamil = chapter_tamil;
        this.chapter_english = chapter_english;
        this.section_id = section_id;
        this.part_id2 = part_id2;
        this.part_tamil = part_tamil;
        this.part_english = part_english;
        this.section_id2 = section_id2;
        this.section_tamil = section_tamil;
        this.section_english = section_english;
    }

    public int getKural_id() {
        return kural_id;
    }

    public void setKural_id(int kural_id) {
        this.kural_id = kural_id;
    }

    public int getChapter_id() {
        return chapter_id;
    }

    public void setChapter_id(int chapter_id) {
        this.chapter_id = chapter_id;
    }

    public String getTamil_kural() {
        return tamil_kural;
    }

    public void setTamil_kural(String tamil_kural) {
        this.tamil_kural = tamil_kural;
    }

    public String getTamil_commentary() {
        return tamil_commentary;
    }

    public void setTamil_commentary(String tamil_commentary) {
        this.tamil_commentary = tamil_commentary;
    }

    public String getTamil_manakudavar() {
        return tamil_manakudavar;
    }

    public void setTamil_manakudavar(String tamil_manakudavar) {
        this.tamil_manakudavar = tamil_manakudavar;
    }

    public String getTamil_alagar() {
        return tamil_alagar;
    }

    public void setTamil_alagar(String tamil_alagar) {
        this.tamil_alagar = tamil_alagar;
    }

    public String getEnglish_kural() {
        return english_kural;
    }

    public void setEnglish_kural(String english_kural) {
        this.english_kural = english_kural;
    }

    public String getEnglish_commentary() {
        return english_commentary;
    }

    public void setEnglish_commentary(String english_commentary) {
        this.english_commentary = english_commentary;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public int getPart_id() {
        return part_id;
    }

    public void setPart_id(int part_id) {
        this.part_id = part_id;
    }

    public int getChapter_id2() {
        return chapter_id2;
    }

    public void setChapter_id2(int chapter_id2) {
        this.chapter_id2 = chapter_id2;
    }

    public String getChapter_tamil() {
        return chapter_tamil;
    }

    public void setChapter_tamil(String chapter_tamil) {
        this.chapter_tamil = chapter_tamil;
    }

    public String getChapter_english() {
        return chapter_english;
    }

    public void setChapter_english(String chapter_english) {
        this.chapter_english = chapter_english;
    }

    public int getSection_id() {
        return section_id;
    }

    public void setSection_id(int section_id) {
        this.section_id = section_id;
    }

    public int getPart_id2() {
        return part_id2;
    }

    public void setPart_id2(int part_id2) {
        this.part_id2 = part_id2;
    }

    public String getPart_tamil() {
        return part_tamil;
    }

    public void setPart_tamil(String part_tamil) {
        this.part_tamil = part_tamil;
    }

    public String getPart_english() {
        return part_english;
    }

    public void setPart_english(String part_english) {
        this.part_english = part_english;
    }

    public int getSection_id2() {
        return section_id2;
    }

    public void setSection_id2(int section_id2) {
        this.section_id2 = section_id2;
    }

    public String getSection_tamil() {
        return section_tamil;
    }

    public void setSection_tamil(String section_tamil) {
        this.section_tamil = section_tamil;
    }

    public String getSection_english() {
        return section_english;
    }

    public void setSection_english(String section_english) {
        this.section_english = section_english;
    }
}
