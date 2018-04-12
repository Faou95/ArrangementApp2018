package no.usn.grupp1.arrangementapp;

/**
 * Created by rolfi on 07.04.2018.
 */

public class Seat {
    public int seatNr;

    public Seat(){

    }

    public Seat(int seatNr) {
        this.seatNr = seatNr;
    }

    public int getSeatNr() {
        return seatNr;
    }

    public void setSeatNr(int seatNr) {
        this.seatNr = seatNr;
    }
}
