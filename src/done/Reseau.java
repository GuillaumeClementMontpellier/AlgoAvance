package done;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Reseau {

    private double[][] arc; //arc[i][j] = capacité de l'arc i -> j (0 si pas d'arc)

    private int s;

    private int t;


    //------------------------------------------------------------------
    //------------------CONSTRUCTEURS ----------------------------------
    //------------------------------- ----------------------------------


    public Reseau(int nbNoeud, int s, int t) {
        this.initAllArcToZero(nbNoeud);
        this.s = s;
        this.t = t;
    }

    public Reseau(double[][] arc, int s, int t) {
        this.arc = arc;
        this.s = s;
        this.t = t;
    }

    public Reseau(Reseau courant) {
        int nb = courant.arc[0].length;
        this.arc = new double[nb][nb];
        for (int i = 0; i < nb; i++) {
            System.arraycopy(courant.arc[i], 0, this.arc[i], 0, nb);
        }
        this.s = courant.s;
        this.t = courant.t;
    }


    /**
     * Constructeur en fonction d'un fichier d'initialisation
     *
     * @param fich : l'adresse complète du ficher .txt (ex file = "r1.txt"), au format suivant par exemple
     *             7
     *             0 1:0.5 2:2
     *             2 1:1 3:1 4:1 6:1
     *             4 5:1
     *             6 3:1 5:1
     *             (7 represente le nombre de sommets, 0 à un arc sortant vers 1 avec capacité 0.5, etc
     * @param s,t  vérifient s < n et t < n, avec n en premiere ligne de file
     */
    public Reseau(String fich, int s, int t) throws FileNotFoundException {

        this.s = s;
        this.t = t;
        File file = new File(fich);
        Scanner sc = new Scanner(file);

        int nb = sc.nextInt();
        initAllArcToZero(nb);

        while (sc.hasNext()) {
            int n = sc.nextInt();
            String[] str = sc.nextLine().split(" ");
            for (int i = 1; i < str.length; i++) {
                String[] netcap = str[i].split(":");
                modifierArc(n, new int[]{Integer.parseInt(netcap[0])}, Double.parseDouble(netcap[1]));

            }
        }

    }


    /**
     * Créé un réseau en fonction d'une instance du problème de debruitage inst comme spécifié dans le sujet.
     * En particulier, si le graph de inst à n sommets il faudra créer un réseau avec n+2 sommets, et avec s=n et t=n+1.
     */
    public Reseau(InstanceDebruitage inst) {

        int n = inst.getN() + 2;
        initAllArcToZero(n);
        this.s = n - 2;
        this.t = n - 1;
        double alpha = inst.getAlpha();

        for (int i = 0; i < n - 2; i++) { // Pour chaque sommet sauf s et t
            double b = inst.getB(i) / 255;

            this.set(this.s, i, b);
            this.set(i, this.t, 1 - b);

            for (int j = 0; j < i; j++) {
                if (inst.isVoisin(i, j)) {
                    this.set(i, j, alpha);
                    this.set(j, i, alpha);
                }
            }
        }
    }


    //------------------------------------------------------------------
    //------------------ GETTERS, SETTERS, METHODES UTILES et TOSTRING--
    //------------------------------- ----------------------------------


    public int getN() {
        return arc.length;
    }

    public int getS() {
        return s;
    }

    public int getT() {
        return t;
    }

    public void set(int i, int j, double v) {
        arc[i][j] = v;
    }

    public double get(int i, int j) {
        return arc[i][j];
    }


    private void initAllArcToZero(int nbNoeud) {
        this.initAllArc(nbNoeud, 0);
    }

    private void initAllArc(int nbNoeud, double alpha) {
        arc = new double[nbNoeud][nbNoeud];
        for (int i = 0; i < nbNoeud; i++) {
            Arrays.fill(arc[i], alpha);
        }
    }

    /**
     * modifie la capacité des arcs sortant du noeud n1 vers les noeuds stocké dans n2.
     * ils auront la capacité cap
     */
    private void modifierArc(int n1, int[] n2, double cap) {
        for (int x : n2) {
            arc[n1][x] = cap;
        }
    }

    public String toString() {
        StringBuilder res = new StringBuilder("s : " + s + " t : " + t + "\n");

        for (double[] doubles : arc) {
            for (double aDouble : doubles) {
                res.append(aDouble).append(" ");
            }
            res.append("\n");
        }
        return res.toString();
    }


    //------------------------------------------------------------------
    //------------------ METHODES POUR MAX FLOT / MIN CUT---------------
    //------------------------------- ----------------------------------


    /**
     * Cherche un s-t chemin dans this
     *
     * @return un couple de deux arraylist d'Integer.
     * S'il existe un chemin entre la source et le puit : le couple vaudra {les entiers du chemin, null}
     * S'il n'existe pas un chemin entre la source et le puit : le couple vaudra {null, les entiers atteignables depuis s}
     */
    public Couple<ArrayList<Integer>, ArrayList<Integer>> trouverChemin() {

        Stack<Integer> done = new Stack<>();
        Stack<Integer> parentsDone = new Stack<>();

        Stack<Integer> toDo = new Stack<>();
        Stack<Integer> parentsToDo = new Stack<>();

        toDo.push(this.s);
        parentsToDo.push(this.s);

        int courant = this.s;
        int parentCourant = this.s;

        while (!toDo.isEmpty() && !done.contains(this.t)) {

            for (int prochain = 0; prochain < this.getN(); prochain++) { // Pour chaque sommet

                if (this.get(courant, prochain) > 0) { // s'il y a arc de current a prochain
                    toDo.push(prochain);
                    parentsToDo.push(courant);
                }
            }

            parentsDone.push(parentCourant);
            done.push(courant);

            while (!toDo.isEmpty() && done.contains(courant)) {
                courant = toDo.pop();
                parentCourant = parentsToDo.pop();
            }

        }// Arret : Plus de sommets atteignables OU PAS EXCLUSIF t atteint

        if (!done.contains(this.t)) { // t non atteint
            return new Couple<>(null, new ArrayList<>(done));
        }

        // t atteint : il existe un chemin vers t : voir dans les parents

        int current = this.t;

        ArrayList<Integer> chemin = new ArrayList<>();

        while (current != this.s) {
            chemin.add(current);
            current = parentsDone.get(done.indexOf(current));
        }

        chemin.add(this.s);

        Collections.reverse(chemin);

        return new Couple<>(chemin, null);

    }


    //---------------------------- AUTRES METHODES --------------------------

    /**
     * @param chemin est un s-t chemin (tous les chemin[i][i+1] sont des arcs de capacité non nulle de this)
     * @return la capacité min d'un arc du chemin (min_i chemin[i][i+1])
     */
    public double calculCapMinChemin(ArrayList<Integer> chemin) {

        double epsilon = arc[chemin.get(0)][chemin.get(1)];

        for (int i = 0; i < chemin.size() - 1; i++) {
            double cap = arc[chemin.get(i)][chemin.get(i + 1)];
            if (cap > 0) {
                if (cap < epsilon) {
                    epsilon = cap;
                }
            }
        }
        return epsilon;
    }


    /**
     * On suppose que this est un réseau sans digon
     *
     * @return un flot maximum, et une coupe minimum
     * <p>
     * Applique les étapes de l'algorithme de Ford-Fulkerson vu en cours
     */
    public Couple<Flot, ArrayList<Integer>> flotMax() {
        Flot flot = new Flot(this);

        double valFlot = 0;

        Reseau residuel = flot.créerReseauResiduel();
        Couple<ArrayList<Integer>, ArrayList<Integer>> maybeChemin = residuel.trouverChemin();
        while (maybeChemin.getElement2() == null) {

            ArrayList<Integer> chemin = maybeChemin.getElement1();

            double epsilon = residuel.calculCapMinChemin(chemin);

            flot.modifieSelonChemin(chemin, epsilon);

            valFlot += epsilon;

            residuel = flot.créerReseauResiduel();
            maybeChemin = residuel.trouverChemin();

        }// Arret : plus de chemin dans le reseau residuel

//        System.out.println(valFlot);

        return new Couple<>(flot, maybeChemin.getElement2());

    }


    /**
     * utilisé seulement pour résoudre le problème de débruitage, puisque lorsque l'on créé le réseau en fonction de l'instance de débruitage
     * comme indiqué dans le sujet, le réseau obtenu possède de nombreux digons, qu'il faudra donc supprimer pour pouvoir appliquer
     * FF
     * supprime les digons du reseau : pour chaque digon i->j (de capacité c1) et j->i (de capacité c2) (avec c1 et c2 non nuls) :
     * - on ajoute un nouveau sommet d (pour le premier nouveau sommet d=n, pour le second d=n+1 etc)
     * - i->j reste de capacité c1, j->d et d->i sont de capacité c2
     */

    public void supprimerDigons() {
        int i = 0, j;
        int n = arc[i].length;
        ArrayList<int[]> digons = new ArrayList<>();
        while (i < n) {
            j = 0;
            while (j < n) {
                if (arc[i][j] != 0 && arc[j][i] != 0) {
                    digons.add(new int[]{j, i});
                    arc[j][i] = 0;
                }
                j++;
            }
            i++;
        }
        double[][] newArc = new double[n + digons.size()][n + digons.size()];
        for (i = 0; i < n; i++) {
            newArc[i] = Arrays.copyOf(arc[i], newArc[i].length);
        }
        int d = n;
        for (int[] digon : digons) {
            i = digon[1];
            j = digon[0];
            newArc[j][d] = arc[i][j];
            newArc[d][i] = arc[i][j];
            d++;
        }
        arc = newArc;
    }


}
