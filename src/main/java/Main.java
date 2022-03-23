import enums.BoardType;
import models.Statistic;
import operators.*;

import java.util.*;

public class Main {
    public static void main(String [] args) {
        run();
//        test();
    }

    public static void run() {
        int populationSize = 10;
        int generationNumber = 10;
        int repeatNumber = 1;
        int tournamentFactor = 5;
        double crossoverProbability = 0.75;
        double mutationProbability = 0.2;
        BoardType board = BoardType.EASY;
        Random random = new Random();

        SelectionOperator tournament = new TournamentSelection(tournamentFactor, populationSize, random);
        SelectionOperator roulette = new RouletteSelection(populationSize, random);
        CrossoverOperator crossover = new CrossoverOperator(crossoverProbability, board, random);
        MutationOperator mutation = new MutationOperator(mutationProbability, random);

        FacilityLayoutOptimization flc = new FacilityLayoutOptimization(board, populationSize, tournament,
                crossover, mutation, random);

        List<Statistic> statisticList = new ArrayList<>();
//        10-time-genetic-algorithm
        for (int i = 0; i < repeatNumber;i++) {
            List<Statistic> statistics = flc.evolutionNTimeWithStatistic(generationNumber);
            ChartDrawer.createGraph(statistics, board.getBoardName() + "_g" + generationNumber + "_p" +
                    populationSize + "_c" + crossoverProbability + "_m" + mutationProbability + "_t" +
                    tournamentFactor + "_i"  + i);
            statisticList.addAll(statistics);
        }

//        //10-time-random-function
//        for (int i = 0; i < repeatNumber;i++) {
//            List<Statistic> statistics = flc.randomEvolutionNTimeWithStatistic(generationNumber);
//            ChartDrawer.createGraph(statistics, "random_"+ board.getBoardName() + "_g" + generationNumber + "_p" +
//                    populationSize + "_c" + crossoverProbability + "_m" + mutationProbability + "_t" +
//                    tournamentFactor + "_i"  + i);
//            statisticList.addAll(statistics);
//        }

        List<Integer> bests = new ArrayList<>();
        List<Integer> worsts = new ArrayList<>();
        List<Double> avgs = new ArrayList<>();
        List<Double> stds = new ArrayList<>();
        statisticList.forEach(x -> {
            bests.add(x.getBest());
            worsts.add(x.getWorst());
            avgs.add(x.getAvg());
            stds.add(x.getStd());
        });
        int best = bests.stream().min(Comparator.naturalOrder()).get();
        int worst = worsts.stream().max(Comparator.naturalOrder()).get();
        double avg =  (double) avgs.stream().reduce(0.0, Double::sum)
                / (double) avgs.size();
        double std =  (double) stds.stream().reduce(0.0, Double::sum)
                / (double) stds.size();

        System.out.println(best + "    " + worst + "    " + avg + "    " + std);
    }
}
