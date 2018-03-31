package no.usn.grupp1.arrangementapp;

/**
 * Created by finge on 16.02.2018.
 */
public class Arrangement {


    private String tittel;
    private String description;
    private final int imageResource;

    public Arrangement(String tittel, String description, int imageResource) {
        this.tittel = tittel;
        this.description = description;
        this.imageResource = imageResource;
    }


    public String getTittel() {
        return tittel;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResource() {
        return imageResource;
    }
}
