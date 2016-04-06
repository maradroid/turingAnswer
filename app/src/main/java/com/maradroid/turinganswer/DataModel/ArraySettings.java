package com.maradroid.turinganswer.DataModel;

/**
 * Created by mara on 4/6/16.
 */
public class ArraySettings {

    private String ulazniNiz;
    private String prihvacenoStanje;
    private String praznoMjesto;
    private String bezuvjetniPrijelaz;

    public ArraySettings(String ulazniNiz, String bezuvjetniPrijelaz, String praznoMjesto, String prihvacenoStanje) {
        this.ulazniNiz = ulazniNiz;
        this.bezuvjetniPrijelaz = bezuvjetniPrijelaz;
        this.praznoMjesto = praznoMjesto;
        this.prihvacenoStanje = prihvacenoStanje;
    }

    public String getUlazniNiz() {
        return ulazniNiz;
    }

    public void setUlazniNiz(String ulazniNiz) {
        this.ulazniNiz = ulazniNiz;
    }

    public String getPrihvacenoStanje() {
        return prihvacenoStanje;
    }

    public void setPrihvacenoStanje(String prihvacenoStanje) {
        this.prihvacenoStanje = prihvacenoStanje;
    }

    public String getPraznoMjesto() {
        return praznoMjesto;
    }

    public void setPraznoMjesto(String praznoMjesto) {
        this.praznoMjesto = praznoMjesto;
    }

    public String getBezuvjetniPrijelaz() {
        return bezuvjetniPrijelaz;
    }

    public void setBezuvjetniPrijelaz(String bezuvjetniPrijelaz) {
        this.bezuvjetniPrijelaz = bezuvjetniPrijelaz;
    }
}
