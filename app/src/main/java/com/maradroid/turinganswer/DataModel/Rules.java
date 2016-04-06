package com.maradroid.turinganswer.DataModel;

/**
 * Created by mara on 4/6/16.
 */
public class Rules {

    private String trenutnoStanje;
    private String procitanaVrijednost;
    private String buduceStanje;
    private String vrijednostPisanja;
    private String pomak;

    public Rules(String trenutnoStanje, String procitanaVrijednost, String buduceStanje, String vrijednostPisanja, String pomak) {
        this.trenutnoStanje = trenutnoStanje;
        this.procitanaVrijednost = procitanaVrijednost;
        this.buduceStanje = buduceStanje;
        this.vrijednostPisanja = vrijednostPisanja;
        this.pomak = pomak;
    }

    public String getTrenutnoStanje() {
        return trenutnoStanje;
    }

    public void setTrenutnoStanje(String trenutnoStanje) {
        this.trenutnoStanje = trenutnoStanje;
    }

    public String getProcitanaVrijednost() {
        return procitanaVrijednost;
    }

    public void setProcitanaVrijednost(String procitanaVrijednost) {
        this.procitanaVrijednost = procitanaVrijednost;
    }

    public String getBuduceStanje() {
        return buduceStanje;
    }

    public void setBuduceStanje(String buduceStanje) {
        this.buduceStanje = buduceStanje;
    }

    public String getVrijednostPisanja() {
        return vrijednostPisanja;
    }

    public void setVrijednostPisanja(String vrijednostPisanja) {
        this.vrijednostPisanja = vrijednostPisanja;
    }

    public String getPomak() {
        return pomak;
    }

    public void setPomak(String pomak) {
        this.pomak = pomak;
    }
}
