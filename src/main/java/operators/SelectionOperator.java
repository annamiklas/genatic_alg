package operators;

import org.parboiled.common.Tuple2;

import java.util.List;

public interface SelectionOperator {
    Integer select(List<Integer> currentFitnessFactor);
}
