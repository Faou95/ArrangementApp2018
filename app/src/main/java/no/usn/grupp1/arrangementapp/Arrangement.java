package no.usn.grupp1.arrangementapp;

/**
 * Created by finge on 16.02.2018.
 */
public class Arrangement {


    private String tittel;
    private String description;
    private String date;
    private String time;
    private String age;
    private int fee;
    private final int pos;

    public Arrangement(String tittel, String description, String date, String time, String age, int fee , int pos) {
        this.tittel = tittel;
        this.description = description;
        this.date = date;
        this.time = time;
        this.age = age;
        this.fee = fee;
        this.pos = pos;
    }


    public String getTittel() {
        return tittel;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        String temp = "Kl: " + time;
        return temp;
    }

    public String getAge() {
        String temp = "Alder: " + age;
        return temp;
    }

    public String getFee() {
        String temp = "Pris: " + fee + "kr";
        return temp;
    }

    public int getPos() {
        return pos;
    }
}
