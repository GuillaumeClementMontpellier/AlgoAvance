package done;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainGraph {

    public static void main(String[] args) throws IllegalArgumentException, IOException {

        int[][] data = new int[10][10];

        Random r = new Random();

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = r.nextInt(255);
            }
        }

//        for (int[] datum : data) {
//            Arrays.fill(datum, 0);
//        }
//        for (int i = 0; i < data.length; i++) {
//            for (int j = i % 2; j < data.length; j += 2) {
//                data[i][j] = 255;
//            }
//        }
//        data[2][0] = 100;

        Img image = new Img(data);

        image.creerImage("Image.png");

//        image.bruiter(3);
//
//        image.creerImage("Image_Bruit.png");

        InstanceDebruitage instDeb = new InstanceDebruitage(image);

        ArrayList<Double> alphas = new ArrayList<>();
        alphas.add(0.1);
        alphas.add(0.2);
        alphas.add(0.3);
        alphas.add(0.4);
        alphas.add(0.5);
        alphas.add(0.6);

        for (Double alpha : alphas) {
            instDeb.setAlpha(alpha);

            ArrayList<Integer> solutionBlancs = instDeb.calculOpt();

//            System.out.println("MainGraph.main");

//            System.out.println(solutionBlancs);

            Img imageRes = image.appliquerFiltre(image.calculFiltre(solutionBlancs));
            imageRes.creerImage("Image_" + alpha * 100 + ".png");
        }


    }

}
