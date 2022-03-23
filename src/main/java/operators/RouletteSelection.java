package operators;

import operators.SelectionOperator;

import java.util.*;

public class RouletteSelection implements SelectionOperator {

    private Random rand;
    private int populationSize;

    public RouletteSelection(int populationSize, Random rand) {
        this.populationSize = populationSize;
        this.rand = rand;
    }

    @Override
    public Integer select(List<Integer> currentFitnessFactor) {
        Double totalFitnessSum = 0.0;

        for (var fit : currentFitnessFactor) {
            totalFitnessSum += (1.0 / (double) fit);
        }

        List<Double> individualProbability = new ArrayList<>();
        Double finalTotalFitnessSum = totalFitnessSum;
        currentFitnessFactor.forEach(fit -> individualProbability.add(((1.0 / fit)  / finalTotalFitnessSum)));

        List<Double> probabilityRange = new ArrayList<>();

        double sum = 0;
        for (int i = 0; i < populationSize; i++) {
            sum += individualProbability.get(i);
            probabilityRange.add(sum);
        }

        double randValue = rand.nextDouble();

        for (int i = 0; i < probabilityRange.size(); i++) {
            if (i == 0) {
                if (randValue < probabilityRange.get(i)) {
                    return i;
                }
            }
            else {
                if (randValue >= probabilityRange.get(i - 1) && randValue < probabilityRange.get(i)) {
                    return i;
                }
            }
        }

        return populationSize - 1;
    }
}
