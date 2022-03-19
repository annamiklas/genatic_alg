package models;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FlowObject {
    private int source;
    private int dest;
    private int amount;
}