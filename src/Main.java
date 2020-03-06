import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solution;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;

import java.util.List;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) {
        Model model = new Model("Jardin problem");

        int nbAllees = 20;

        BoolVar[] vars = model.boolVarArray(2 * nbAllees);

        for(int i = 0; i < nbAllees; i++){
            
        }


        Solver solver = model.getSolver();
        solver.showStatistics();

        List<Solution> allSolutions = solver.findAllSolutions();

        for (Solution s : allSolutions) {
            System.out.println(s);
        }

    }
}
