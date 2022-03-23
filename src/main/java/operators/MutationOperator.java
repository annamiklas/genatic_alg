package operators;

import java.util.List;
import java.util.Random;

public class MutationOperator {

    private final Random rand;
    private final double mutationProbability;

    public MutationOperator(double mutationProbability, Random rand) {
        this.mutationProbability = mutationProbability;
        this.rand = rand;
    }

    public void mutation(List<Integer> newIndividual) {
        if(rand.nextDouble() <= mutationProbability ) {
            int randomId = rand.nextInt(newIndividual.size());
            int secondRandomId = randomId;
            while(randomId == secondRandomId) {
                secondRandomId = rand.nextInt(newIndividual.size());
            }

            Integer tempGenValue = newIndividual.get(randomId);
            newIndividual.set(randomId, newIndividual.get(secondRandomId));
            newIndividual.set(secondRandomId, tempGenValue);
        }
    }
}
