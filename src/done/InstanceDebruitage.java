package done;

import java.util.ArrayList;
import java.util.Arrays;


public class InstanceDebruitage {
    private boolean[][] g;
    //va contenir la matrice d'ajacence du graphe G sur n sommets modélisant les n pixels de l'image
    //pour nous : chaque pixel correpondra à un sommet, et chaque pixel est adjacent à ses voisins H,B,G,D (sauf au bord bien sûr), le graphe G sera donc une grille
    //Cela dit, on peut tout à fait utiliser cette classe avec un graphe quelconque

    private double[] b; //b est de taille n, 0 <= b[i] <= 255 contient niveau de gris du sommet i (pour nous b[i]=0 (noir) ou 255 (blanc), sauf dans les bonus)
    private double alpha;

    //sortie du problème : sous ensemble S de sommets de g (représentant ensemble de sommets qu'on colorie en blanc, les autres seront en noir)
    //objectif : min f(S) = N(S) + B(V\S) + alpha |{ {i,j} dans E de g tq |{i,j} \cap S|=1 }| 
    // avec N(S) = # de sommets de S qui étaient noirs, B(V\S) = # de sommets de V\S qui étaient blancs
    //

    public InstanceDebruitage(boolean[][] g, double[] bi, double alpha) {
        this.g = g;
        this.b = bi;
        this.alpha = alpha;
    }


    public InstanceDebruitage(Img im) {
        /*
            si im est une image 3x3, va créer un graphe à 9 sommets, avec la numérotation
	        0 1 2
	        3 4 5
	        6 7 8

	        on aura par exemple les aretes {0,3} {3,4} {3,6}
	        b[3] correspondra au niveau de gris du pixel
            g[i][j] = true lorsque i et j sont voisins dans l'image.
         */

        int hauteur = im.nbLignes();
        int largeur = im.nbColonnes();
        int nbTotalNodes = largeur * hauteur;

        g = new boolean[nbTotalNodes][nbTotalNodes];
        b = new double[nbTotalNodes];

        for (boolean[] booleans : this.g) {
            Arrays.fill(booleans, false);
        }

        for (int i = 0; i < largeur; i++) {
            for (int j = 0; j < hauteur; j++) {

                int x = im.calculIndice(i, j);
                b[x] = im.getData()[i][j];
                if (i < largeur - 1) {
                    int y = im.calculIndice(i + 1, j);
                    g[x][y] = true;
                }
                if (i > 0) {
                    int y = im.calculIndice(i - 1, j);
                    g[x][y] = true;
                }
                if (j < hauteur - 1) {
                    int y = im.calculIndice(i, j + 1);
                    g[x][y] = true;
                }
                if (j > 0) {
                    int y = im.calculIndice(i, j - 1);
                    g[x][y] = true;
                }
            }
        }
    }

    public int getN() {
        return g.length;
    }

    public double getB(int i) {
        return b[i];
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public boolean isVoisin(int i, int j) {
        return g[i][j];
    }

    /**
     * calcul un opt en se réduisant à un problème de flot comme indiqué dans le sujet.
     *
     * @return une solution optimale (correspondant donc à un ensemble de pixels à mettre en blanc)
     */
    public ArrayList<Integer> calculOpt() {
        int n = g[0].length;
        int s = n;
        int t = n + 1;

        Reseau r = new Reseau(this);
        r.supprimerDigons();
        Couple<Flot, ArrayList<Integer>> flotMaxMinCut = r.flotMax();
//        System.out.println("InstanceDebruitage.calculOpt");
//        Flot flotMax = couple.getElement1();
//        System.out.println(flotMax);
        ArrayList<Integer> minCut = flotMaxMinCut.getElement2();

        minCut.remove(minCut.indexOf(s));
        //on enleve les points fictif des digons
        ArrayList<Integer> minCutclean = new ArrayList<>();
        for (int x : minCut) {
            if (x < t) {
                minCutclean.add(x);
            }
        }

        return minCutclean;
    }

    public String toString() {
        StringBuilder str = new StringBuilder("Matrice d'adjacence \n  ");

        for (int j = 0; j < this.g[0].length; j++) {
            str.append(j).append(" ");
        }

        str.append("\n");

        for (int i = 0; i < g.length; i++) {
            str.append(i).append(" ");
            for (boolean aBoolean : g[i]) {
                str.append(aBoolean ? "1" : " ");
                str.append(" ");
            }
            str.append("\n");
        }

        str.append("tableau b \n");

        for (double v : this.b) {
            str.append(v);
            str.append(" ");
        }

        return str.toString();
    }


}
