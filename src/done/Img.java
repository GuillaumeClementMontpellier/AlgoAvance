package done;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

class Img {

    private int[][] data;  //data[i][j] représente pixel(i,j) avec i numero colonne et j num ligne avec ligne 0 en haut, et colonne 0 à gauche
    private int hauteur;
    private int largeur;


    /**
     * @param nomFichier : adresse d'un fichier pgm, ex "fichier.pgm"
     */
    public Img(String nomFichier) {
        try {
            Pixmap pm = new Pixmap(nomFichier);
            data = new int[pm.getW()][pm.getH()];
            for (int i = 0; i < pm.getW(); i++) {
                for (int j = 0; j < pm.getH(); j++) {

                    data[i][j] = pm.get(i, j);
                }
            }
            setHL();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Img(int[][] tab) {
        data = new int[tab.length][tab[0].length];
        for (int i = 0; i < tab.length; i++) {
            System.arraycopy(tab[i], 0, data[i], 0, tab[0].length);
        }
        setHL();
    }

    private void setHL() {
        hauteur = data[0].length;
        largeur = data.length;
    }

    public int[][] getData() {
        return data;
    }

    public int get(int i, int j) {
        return data[i][j];
    }

    //utilisé dans InstanceDebruitage où le pixel (i,j) d'une Img correspondant au sommet numéro "calculIndice(i,j)" du graphe de l'instanceDebruitage associée à l'Img
    public int calculIndice(int i, int j) {
        return largeur * j + i;
    }

    public Couple<Integer, Integer> calculCoord(int x) {
        int j = x / largeur;
        int i = x % largeur;
        return new Couple<>(i, j);
    }

    public int getPixel(int i, int j) {
        return data[i][j];
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int j = 0; j < nbLignes(); j++) {
            for (int i = 0; i < nbColonnes(); i++) {
                str.append("\t").append(data[i][j]);
            }
            str.append("\n");
        }
        return str.toString();
    }

    public int nbColonnes() {
        return largeur;
    }

    public int nbLignes() {
        return hauteur;
    }

    public void bruiter(int n) {

        for (int x = 0; x < n; x++) {

            Random r = new Random();
            int i = r.nextInt(data.length);
            int j = r.nextInt(data[0].length);
            data[i][j] = 255 - data[i][j];

        }

    }


    /**
     * @param solutionBlancs : liste des indices des pixels (le pixel (i,j) à l'indice "calculIndice(i,j)") à mettre en blanc
     * @return les coordonnées de ces pixels
     */
    public ArrayList<Couple<Integer, Integer>> calculFiltre(ArrayList<Integer> solutionBlancs) {
        ArrayList<Couple<Integer, Integer>> res = new ArrayList<>();
        for (Integer x : solutionBlancs) {
            res.add(calculCoord(x));
        }
        return res;
    }


    /**
     * param : liste des coordonnées de pixels à mettre en blanc
     */
    public Img appliquerFiltre(ArrayList<Couple<Integer, Integer>> solutionBlancs) {

        Img nouvelleImage = new Img(data);

        for (int i = 0; i < this.nbColonnes(); i++) {
            for (int j = 0; j < this.nbLignes(); j++) {
                Couple<Integer, Integer> indice = new Couple<>(i, j);
                if (solutionBlancs.contains(indice)) {
                    // Si i,j est cense etre blanc
                    if (nouvelleImage.data[i][j] < 255 / 2) {
                        // et Si i,j est noir
                        nouvelleImage.data[i][j] = 255 - this.data[i][j];
                    }
                } else {
                    // Si i,j est censé etre noir
                    if (nouvelleImage.data[i][j] > 255 / 2) {
                        // et Si i,j est blanc
                        nouvelleImage.data[i][j] = 255 - this.data[i][j];
                    }
                }
            }
        }
        return nouvelleImage;
    }

    /**
     * Convertie l'image en un nouveau ficher pgm
     *
     * @param nomFile : nom du nouveau ficher
     */
    public void creerImage(String nomFile) {
        Pixmap pm = new Pixmap(this.nbColonnes(), this.nbLignes());
        pm.setData();
        for (int i = 0; i < nbColonnes(); i++) {
            for (int j = 0; j < nbLignes(); j++) {
                pm.set(i, j, data[i][j]);
            }
        }
        pm.write(nomFile);
    }
}
