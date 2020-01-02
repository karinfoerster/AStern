package sample.Schatzsuche;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/*Hier werden die Grafiken geladen und gesetzt
* Kann zum Beispiel noch geändert werden, dass man den Startpunkt und das Ziel selber wählt*/
public class Wand extends JComponent{
    public final int WIDTH;
    private final int HEIGHT;

    private BufferedImage insel;
    private BufferedImage piratgewonnen;
    private BufferedImage schiff;
    private BufferedImage wassersuche;
    private BufferedImage wasserweg;

    private BufferedImage stein;
    private BufferedImage meer;
    private BufferedImage pirat;
    private int piratX = 2;
    private int piratY = 2;

    private BufferedImage ziel;
    private int zielX = 25;
    private int zielY = 25;

    public int x;
    public int y;

    private boolean[][] field;
    private final int ZELLEN = 30;
    public final int ratio;

    LinkedList<Wegpunkt> openList;
    LinkedList<Wegpunkt> closedList;
    private Wegpunkt finish;

    public boolean suche = false;


    /**
     * Das Feld wird aufgebaut und Start und Ziel gesetzt
     */
    public Wand(int width, int height) {
        //initialisiere die listen
        openList = new LinkedList<Wegpunkt>();
        openList.add(new Wegpunkt(this.piratX, this.piratY, 1, this.heuristik(this.piratX, this.piratY), null));
        closedList = new LinkedList<Wegpunkt>();

        //rechne aus wie breit eine kachel sein wird
        this.WIDTH = width;
        this.HEIGHT = height;
        this.ratio = this.WIDTH / this.ZELLEN;

        //lade die grafiken
        try {
            this.meer = ImageIO.read(new File("src/sample/Bilder/wasser.png"));
            this.stein = ImageIO.read(new File("src/sample/Bilder/stein.png"));
            this.pirat = ImageIO.read(new File("src/sample/Bilder/pirat.png"));
            this.ziel = ImageIO.read(new File("src/sample/Bilder/schatztruhezu.png"));

        } catch (IOException ex) {
            Logger.getLogger(Wand.class.getName()).log(Level.SEVERE, null, ex);
        }


        //erzeuge ein zufälliges feld
        this.field = new boolean[ZELLEN][ZELLEN];

        for (int i = 0; i < ZELLEN; i++) {
            for (int j = 0; j < ZELLEN; j++) {
                if (Math.random() < 0.7f) {
                    this.field[i][j] = true;
                } else {
                    this.field[i][j] = false;
                }
            }
        }

        //räume start und endposition frei von steinen
        this.field[this.zielX][this.zielY] = true;
        this.field[this.piratX][this.piratY] = true;


        //setzt listener für die eingaben
        this.addMouseListener(new MyMouseListener(this));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyListener(this));
        this.setSize(width, height);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        //pathfinding
        Wegpunkt wegpunkt = null;
        if (this.finish != null) {
            wegpunkt = this.finish;
            this.suche = false;
        } else if (this.openList.isEmpty() == false && this.suche == true){
            wegpunkt = this.aStern();
        }


        //Umgebung wird geladen
        for (int i = 0; i < ZELLEN; i++) {
            for (int j = 0; j < ZELLEN; j++) {
                if (this.field[i][j]) {
                    g.drawImage(this.meer, i * this.ratio, j * this.ratio, this.ratio, this.ratio, null);
                } else {
                    g.drawImage(this.stein, i * this.ratio, j * this.ratio, this.ratio, this.ratio, null);
                }
            }
        }


        //die noch zu untersuchenden Felder werden gezeichnet (openList)
        g.setColor(new Color(28, 255, 181, 125));
        for (int i = 0; i < this.openList.size(); i++) {
            g.fillRect(this.openList.get(i).x * this.ratio, this.openList.get(i).y * this.ratio, this.ratio, this.ratio);
        }

        //die untersuchten Felder werden gezeichnel (closedList)
        g.setColor(new Color(41, 117, 255, 125));
        for (int i = 0; i < this.closedList.size(); i++) {
            g.fillRect(this.closedList.get(i).x * this.ratio, this.closedList.get(i).y * this.ratio, this.ratio, this.ratio);
        }

        //bester Pfad wird gezeichnet
        if (wegpunkt != null) {
            g.setColor(new Color(75, 255, 255, 125));
            wegpunkt.draw(g, ratio);


            g.setColor(Color.yellow);
            g.fillRect(wegpunkt.x * this.ratio, wegpunkt.y * this.ratio, this.ratio, this.ratio);
        }

        //pirat
        g.drawImage(this.pirat, this.piratX * this.ratio, this.piratY * this.ratio, this.ratio, this.ratio, null);
        //schatztruhe
        g.drawImage(this.ziel, this.zielX * this.ratio, this.zielY * this.ratio, this.ratio, this.ratio, null);

        if (this.suche) {
            this.repaint();
        }

    }


    public void changeFieldAtPos(int x, int y) {
        this.field[x][y] = !this.field[x][y];
        this.repaint();
    }

    /**
     * Der A-Stern Pathfinding Algorythmus
     */
    private Wegpunkt aStern() {

        Wegpunkt bestWegpunkt;

        //Die Nachbarfelder werden untersucht, wenn dort kein Stein liegt und das Feld
        // noch nicht in der ClosedList ist
        bestWegpunkt = openList.get(this.getFirstBestListEntry(openList));
        closedList.add(openList.remove(this.getFirstBestListEntry(openList)));
        System.out.println("untersuche: x:" + bestWegpunkt.x + " y:" + bestWegpunkt.y + " DeltaZiel: " + this.heuristik(bestWegpunkt.x, bestWegpunkt.y));

        //if besterWeg = Ziel = Ziel gefunden
        if (bestWegpunkt.x == this.zielX && bestWegpunkt.y == this.zielY) {
            bestWegpunkt.print();
            this.finish = bestWegpunkt;
            return bestWegpunkt;
        }

        //oberhalb
        this.openListAddHelper(bestWegpunkt.x, bestWegpunkt.y - 1, openList, closedList, bestWegpunkt);

        //rechts
        this.openListAddHelper(bestWegpunkt.x + 1, bestWegpunkt.y, openList, closedList, bestWegpunkt);

        //unterhalb
        this.openListAddHelper(bestWegpunkt.x, bestWegpunkt.y + 1, openList, closedList, bestWegpunkt);

        //links
        this.openListAddHelper(bestWegpunkt.x - 1, bestWegpunkt.y, openList, closedList, bestWegpunkt);


        return bestWegpunkt;
    }


    /**
     * Hier wird überprüft ob ein Feld schon vollständig überprüft wurde
     * kann gegebenfalls geändert werden wenn es zum Beispiel Felder mit
     * besonderen Eigenschaften gibt (Felder bei denen man langsamer wird,........)
     */
    private void openListAddHelper(int x, int y, LinkedList<Wegpunkt> openList, LinkedList<Wegpunkt> closedList, Wegpunkt vorher){
        if (x < 0 || y < 0 || x >= this.ZELLEN || y >= this.ZELLEN) {
            //nichts
        } else {
            if (this.isPointInList(x, y, closedList) == false
                    && this.field[x][y] == true
                    && this.isPointInList(x, y, openList) == false) {
                openList.add(new Wegpunkt(x, y, 1, this.heuristik(x, y), vorher));
            }
        }
    }


    /**
     * hier wird berechnet wie weit es vom Start bis zum Ziel ist
     * wenn es keine Hindernisse gibt
     */
    private int heuristik(int x, int y) {
        int dx = x - this.zielX;
        if (dx < 0) {
            dx = -dx;
        }

        int dy = y - this.zielY;
        if (dy < 0) {
            dy = -dy;
        }

        return dx + dy;
    }

    /**
     * sucht die beste Option - den kürzesten Weg
     */
    private int getFirstBestListEntry(LinkedList<Wegpunkt> list) {
        int best = list.get(0).getGesamtstrecke();

        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).getGesamtstrecke() < best) {
                best = list.get(i).getGesamtstrecke();
            }
        }
        //bester Weg wird geladen
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getGesamtstrecke() == best) {
                return i;
            }
        }
        System.out.println("da ist etwas schiefgelaufen in 'getFirstBestListEntry'");
        return 0;
    }

    private boolean isPointInList(int x, int y, LinkedList<Wegpunkt> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).x == x && list.get(i).y == y) {
                return true;
            }
        }

        return false;
    }

    /**
     * zurücksetzen für eine neue Runde
     */
    public void reset() {
        openList = new LinkedList<Wegpunkt>();
        openList.add(new Wegpunkt(this.piratX, this.piratY, 1, this.heuristik(this.piratX, this.piratY), null));
        closedList = new LinkedList<Wegpunkt>();
        this.finish = null;
    }



}
