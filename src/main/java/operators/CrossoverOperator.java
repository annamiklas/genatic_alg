package operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import enums.BoardType;

public class CrossoverOperator {

    private final Random rand = new Random();
    private final double crossoverProbability;
    private final BoardType board;

    public CrossoverOperator(double crossoverProbability, BoardType board) {
        this.crossoverProbability = crossoverProbability;
        this.board = board;
    }

    public List<Integer> crossover(List<Integer> parent1, List<Integer> parent2) {
        if(rand.nextDouble() <= crossoverProbability) {
            int randomSplitPoint = rand.nextInt(1, board.getTotalIndividualNumber());
            List<Integer> newIndividual = new ArrayList<>(parent1.subList(0, randomSplitPoint));
            for (var gene : parent2) {
                if (!newIndividual.contains(gene)) {
                    newIndividual.add(gene);
                }
            }
            return newIndividual;
        } else {
            return parent1;
        }
    }
}
