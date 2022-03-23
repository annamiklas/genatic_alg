package operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import enums.BoardType;

public class CrossoverOperator {

    private Random rand;
    private final double crossoverProbability;
    private final BoardType board;

    public CrossoverOperator(double crossoverProbability, BoardType board, Random rand) {
        this.crossoverProbability = crossoverProbability;
        this.board = board;
        this.rand = rand;
    }

    public List<Integer> crossover(List<Integer> parent1, List<Integer> parent2) {
        List<Integer> newIndividual = new ArrayList<>();
        if(rand.nextDouble() <= crossoverProbability && !parent1.equals(parent2)) {
            int randomSplitPoint = rand.nextInt(1, board.getTotalIndividualNumber());

            newIndividual.addAll(parent1.subList(0, randomSplitPoint));

            for (var gene : parent2) {
                if (!newIndividual.contains(gene)) {
                    newIndividual.add(gene);
                }
            }
        } else {
            newIndividual.addAll(parent1);
        }
        return newIndividual;
    }
}
