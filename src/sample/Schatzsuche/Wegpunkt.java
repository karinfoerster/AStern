package sample.Schatzsuche;

import java.awt.Graphics;

/**
 * Wegpunkt - alle wichtigen Informationen für den A*
 */
public class Wegpunkt {
    public int x;
    public int y;

    public int strecke;
    public int heuristik;

    Wegpunkt vorgaenger;

    public Wegpunkt(int x, int y, int strecke, int heuristik, Wegpunkt vor) {
        this.x = x;
        this.y = y;
        this. strecke = strecke;
        this.heuristik = heuristik;
        this.vorgaenger = vor;
    }

    public int getGesamtstrecke() {
        return this.getBisherigeWegstrecke() + this.strecke + this.heuristik;
    }

    public int getBisherigeWegstrecke() {
        if (this.vorgaenger == null) return 0;
        return this.vorgaenger.getBisherigeWegstrecke() + this.strecke;
    }

    public void setVorgaenger(Wegpunkt neuer) {
        this.vorgaenger = neuer;
    }

    public void print() {
        if (this.vorgaenger != null) {
            this.vorgaenger.print();
        }

        System.out.println("x:" + x + " y:" + y);
    }

    public void draw(Graphics g, int ratio) {
        g.fillRect(x * ratio, y * ratio, ratio, ratio);
        if (this.vorgaenger != null) {
            this.vorgaenger.draw(g, ratio);
        }
    }
}
