package models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Statistic {
    private int best;
    private int worst;
    private double avg;
    private double std;
}
