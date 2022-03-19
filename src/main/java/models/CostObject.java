package models;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CostObject {
    private int source;
    private int dest;
    private int cost;
}
