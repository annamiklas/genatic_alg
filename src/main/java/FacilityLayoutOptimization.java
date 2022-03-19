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
    private Integer minFitnessFunction = Integer.MAX_VALUE;
    private final Random rand;


    public FacilityLayoutOptimization(BoardType board, Integer populationSize,
                                      SelectionOperator selectionOperator,
                                      CrossoverOperator crossoverOperator,
                                      MutationOperator mutationOperator) {
        this.board = board;
        this.populationSize = populationSize;
        this.selectionOperator = selectionOperator;
        this.crossoverOperator = crossoverOperator;
        this.mutationOperator = mutationOperator;
        this.matrices = new CostFlowMatrix(board);
        this.currentFitnessFactor = new ArrayList<>();
        this.rand = new Random();
        this.population = randomPopulationMethod(populationSize);
        calculatePopulationFitnessFunction(population);
    }

    public List<List<Integer>> randomPopulationMethod(int n) {
        List<List<Integer>> population = new ArrayList<>();
        List<Integer> tempList = new ArrayList<>();
        int randomNumber;
        population.add(tempList);

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

    //TODO
    public int calculateIndividualFitnessFunction(List<Integer> individual) {
        int individualSum = 0;

        int j = 0;
        for (int i = 0; i < individual.size(); i++) {
            for (int k = j; k < individual.size(); k++) {
                int xi = individual.get(i) % board.getDimension().width;
                int yi = individual.get(i) / board.getDimension().height;

                int xj = individual.get(k) % board.getDimension().width;
                int yj = individual.get(k) / board.getDimension().height;

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
            if (individualSum < minFitnessFunction) {
                minFitnessFunction = individualSum;
            }
            currentFitnessFactor.add(individualSum);
        }
    }

    public int calculateManhattanDistance(int xi, int yi, int xj, int yj) {
        return Math.abs(xi - xj) + Math.abs(yi - yj);
    }

    public void evolutionToStopCondition() {
        int i = 0;
        while (minFitnessFunction > board.getOptimum()) {
            generateNewPopulation();
            calculatePopulationFitnessFunction(population);
            System.out.println(minFitnessFunction);
            i++;
        }
        System.out.println(i);
    }

    public List<Statistic> evolutionNTimeWithStatistic(int n) {
        List<Statistic> statistics = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            generateNewPopulation();
            calculatePopulationFitnessFunction(population);
            statistics.add(createStatistics());
            System.out.println(minFitnessFunction);
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
            while (parent1Id == parent2Id) parent2Id = selectionOperator.select(currentFitnessFactor);
            List<Integer> newIndividual = crossoverOperator.crossover(population.get(parent1Id),
                    population.get(parent2Id));
            mutationOperator.mutation(newIndividual);
            tempPopulation.add(newIndividual);
        }
        population = tempPopulation;
    }

    public static void main(String [] args) {
        // optimum HARD - 9212
        // optimum FLAT - 18640

        int populationSize = 5000;
        int generationNumber = 100;
        int tournamentFactor = 20;
        double crossoverProbability = 0.75;
        double mutationProbability = 0.3;
        BoardType board = BoardType.HARD;

        SelectionOperator tournament = new TournamentSelection(tournamentFactor, populationSize);
        SelectionOperator roulette = new RouletteSelection(populationSize);
        CrossoverOperator crossover = new CrossoverOperator(crossoverProbability, board);
        MutationOperator mutation = new MutationOperator(mutationProbability);

        FacilityLayoutOptimization flc = new FacilityLayoutOptimization(board, populationSize, tournament,
                                                                        crossover, mutation);
//        flc.evolutionToStopCondition();
        List<Statistic> statistics = flc.evolutionNTimeWithStatistic(generationNumber);
        ChartDrawer.createGraph(statistics);

//        List<Integer> list = Arrays.asList(2, 7, 3, 6, 0, 4, 1, 8, 5, 11, 9, 10);
//        System.out.println(flc.calculateIndividualFitnessFunction(list));

    }
}
