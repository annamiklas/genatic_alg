import enums.BoardType;
import models.Statistic;
import operators.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class FacilityLayoutOptimization {

    private final Integer populationSize;
    private final SelectionOperator selectionOperator;
    private final CrossoverOperator crossoverOperator;
    private final MutationOperator mutationOperator;
    private final BoardType board;
    private final CostFlowMatrix matrices;
    private final List<Integer> currentFitnessFactor;
    private List<List<Integer>> population;
    private final Random rand;


    public FacilityLayoutOptimization(BoardType board, Integer populationSize,
                                      SelectionOperator selectionOperator,
                                      CrossoverOperator crossoverOperator,
                                      MutationOperator mutationOperator, Random rand) {
        this.board = board;
        this.populationSize = populationSize;
        this.selectionOperator = selectionOperator;
        this.crossoverOperator = crossoverOperator;
        this.mutationOperator = mutationOperator;
        this.matrices = new CostFlowMatrix(board);
        this.currentFitnessFactor = new ArrayList<>();
        this.rand = rand;
    }

    public List<List<Integer>> randomPopulationMethod(int n) {
        List<List<Integer>> population = new ArrayList<>();
        List<Integer> tempList = new ArrayList<>();
        int randomNumber;

        while (!(population.size() >= n)) {
            while (tempList.size() < board.getTotalIndividualNumber()){
                randomNumber = rand.nextInt(board.getTotalIndividualNumber());
                if(!tempList.contains(randomNumber)){
                    tempList.add(randomNumber);
                }
            }
            population.add(tempList);
            tempList = new ArrayList<>();
        }
        return population;
    }

    public int calculateIndividualFitnessFunction(List<Integer> individual) {
        int individualSum = 0;

        int j = 0;
        for (int i = 0; i < individual.size(); i++) {
            for (int k = j; k < individual.size(); k++) {
                int xi = individual.get(i) % board.getDimension().width;
                int yi =  board.getDimension().height == 1 ?  0 : individual.get(i) / board.getDimension().height;

                int xj = individual.get(k) % board.getDimension().width;
                int yj = board.getDimension().height == 1 ?  0 : individual.get(k) / board.getDimension().height;

                int distance = calculateManhattanDistance(xi, yi, xj, yj);
                int pairSum = distance * matrices.getFlowMatrix()[i][k] * matrices.getCostMatrix()[i][k];

                individualSum += pairSum;
            }
            j++;
        }
        return individualSum;
    }

    public void calculatePopulationFitnessFunction(List<List<Integer>> population) {
        currentFitnessFactor.clear();

        for (List<Integer> integers : population) {
            int individualSum = calculateIndividualFitnessFunction(integers);
            currentFitnessFactor.add(individualSum);
        }
    }

    public int calculateManhattanDistance(int xi, int yi, int xj, int yj) {
        return Math.abs(xi - xj) + Math.abs(yi - yj);
    }

    public void evolutionToStopCondition() {
        initPopulation();
        int i = 0;
        while (currentFitnessFactor.stream().min(Comparator.reverseOrder()).get() > board.getOptimum()) {
            generateNewPopulation();
            calculatePopulationFitnessFunction(population);
            i++;
        }
    }

    public List<Statistic> evolutionNTimeWithStatistic(int n) {
        initPopulation();
        List<Statistic> statistics = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            generateNewPopulation();
            calculatePopulationFitnessFunction(population);
            Statistic s = createStatistics();
            statistics.add(s);
        }
        return statistics;
    }

    public void initPopulation() {
        this.population = randomPopulationMethod(populationSize);
        calculatePopulationFitnessFunction(population);
    }

    public List<Statistic> randomEvolutionNTimeWithStatistic(int n) {
        initPopulation();
        List<Statistic> statistics = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            initPopulation();
            statistics.add(createStatistics());
        }
        return statistics;
    }

    public Statistic createStatistics() {
        int best = currentFitnessFactor.stream().min(Comparator.naturalOrder()).orElse(-1);
        int worst = currentFitnessFactor.stream().min(Comparator.reverseOrder()).orElse(-1);
        final double avg = (double) currentFitnessFactor.stream().reduce(0, Integer::sum)
                / (double) currentFitnessFactor.size();

        AtomicReference<Double> denominator = new AtomicReference<>((double) 0);
        currentFitnessFactor.forEach(fit -> {
            denominator.updateAndGet(v -> (double) (v + Math.pow(fit - avg, 2)));
        });
        double std = Math.sqrt(denominator.get() / 2.0);
        return new Statistic(best, worst, avg, std);
    }

    public void generateNewPopulation(){
        List<List<Integer>> tempPopulation = new ArrayList<>();
        for (int j = 0; j < populationSize; j++) {
            int parent1Id = selectionOperator.select(currentFitnessFactor);
            int parent2Id = selectionOperator.select(currentFitnessFactor);
            List<Integer> newIndividual = crossoverOperator.crossover(population.get(parent1Id),
                    population.get(parent2Id));
            mutationOperator.mutation(newIndividual);
            tempPopulation.add(newIndividual);
        }
        population = tempPopulation;
    }
}
