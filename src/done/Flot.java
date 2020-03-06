package done;

import java.util.ArrayList;
import java.util.Arrays;

public class Flot {
    private Reseau reseauF;
    private double[][] valeurFlot;
    //pour tout i,j, 0 <= valeurFlot[i][j] <= reseauF.get(i,j) (qui représente la capacité de l'arc i->j)
    //et conservation du flot


    //--------------------------------------------------------
    //---------------------CONTRUCTEUR -----------------------
    //--------------------------------------------------------

    public Flot(Reseau r) {
        reseauF = new Reseau(r);
        int n = reseauF.getN();
        valeurFlot = new double[n][n];

        for (int i = 0; i < n; i++) {
            Arrays.fill(valeurFlot[i], 0);
        }
    }

    //----------------------METHODES------------------------------

    public void modifierFlot(int i, int j, double flot) {
        valeurFlot[i][j] += flot;
    }


    /**
     * On suppose que le reseauF est sans digon.
     *
     * @return le réseau résiduel associé à reseauF et au flot this.
     * Ce réseau résiduel pourra lui avoir des digons.
     */
    public Reseau créerReseauResiduel() {
        int size = reseauF.getN();
        Reseau residuel = new Reseau(size, reseauF.getS(), reseauF.getT());

        for (int i = 0; i < size; i++) {

            for (int j = 0; j < size; j++) {

                double cap = this.reseauF.get(i, j);

                if (cap == 0) {
                    continue;
                }

                residuel.set(i, j, cap - this.valeurFlot[i][j]);
                residuel.set(j, i, this.valeurFlot[i][j]);

            }
        }

        return residuel;
    }

    /**
     * @param chemin  est un s-t chemin dans le réseau résiduel de reseauF et this
     * @param epsilon > 0 est la capacité min de chemin telle définie dans le cours
     *                action : modifie le flot this en ajoutant ou retirant epsilon comme indiqué dans le cours
     */
    public void modifieSelonChemin(ArrayList<Integer> chemin, double epsilon) {

        for (int i = 0; i < chemin.size() - 1; i++) {
            if(reseauF.get(chemin.get(i),chemin.get(i+1)) > 0) { // il y a una arete de i vers j
                this.modifierFlot(chemin.get(i), chemin.get(i + 1), epsilon);
            } else {
                this.modifierFlot(chemin.get(i+1), chemin.get(i), - epsilon);
            }
        }

    }

    @Override
    public String toString() {

        StringBuilder res = new StringBuilder(
                "reseau : \n" + reseauF + "flot : \n");

        for (double[] doubles : valeurFlot) {
            for (double aDouble : doubles) {
                res.append(aDouble).append(" ");
            }
            res.append("\n");
        }

        return res.toString();
    }
}
