package operators;

import operators.SelectionOperator;

import java.util.*;

public class RouletteSelection implements SelectionOperator {

    private Random rand = new Random();
    private int populationSize;

    public RouletteSelection(int populationSize) {
        this.populationSize = populationSize;
    }

    @Override
    public Integer select(List<Integer> currentFitnessFactor) {
        Integer totalFitnessSum = currentFitnessFactor.stream().reduce(0, Integer::sum);
        List<Double> individualProbability = new ArrayList<>();
        currentFitnessFactor.forEach(fit -> individualProbability.add((double)fit / (double)totalFitnessSum));

        List<Double> probabilityRange = new ArrayList<>();

        double sum = 0;
        for (int i = 0; i < populationSize; i++) {
            probabilityRange.add(sum);
            sum += individualProbability.get(i);
        }

        double randValue = rand.nextDouble();

        for (int i = 0; i < probabilityRange.size(); i++) {
            if (i + 1 >= probabilityRange.size()) break;
            if (randValue >= probabilityRange.get(i) && randValue < probabilityRange.get(i + 1)) {
                return i;
            }
        }
        return populationSize - 1;
    }
}
