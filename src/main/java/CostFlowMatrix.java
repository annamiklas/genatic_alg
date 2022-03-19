import enums.BoardType;
import models.CostObject;
import models.FlowObject;
import org.parboiled.common.Tuple2;

import java.util.ArrayList;
import java.util.List;

public class CostFlowMatrix {
    private List<CostObject> costObjectList  = new ArrayList<>();
    private List<FlowObject> flowObjectList  = new ArrayList<>();
    private final int[][] flowMatrix;
    private final int[][] costMatrix;
    BoardType boardType;

    public CostFlowMatrix(BoardType boardType) {
        this.boardType = boardType;
        flowMatrix = new int[boardType.getTotalIndividualNumber()][boardType.getTotalIndividualNumber()];
        costMatrix = new int[boardType.getTotalIndividualNumber()][boardType.getTotalIndividualNumber()];
        readCostAndFlow();
        createMatrices();
    }

    public void readCostAndFlow() {
        Tuple2<ArrayList<CostObject>, ArrayList<FlowObject>> data = DataReader.readData(boardType.getBoardName());
        costObjectList = data != null ? data.a : null;
        flowObjectList = data != null ? data.b : null;
    }

    public void createMatrices() {
        try {
            flowObjectList.forEach(flowObj -> flowMatrix[flowObj.getSource()][flowObj.getDest()] = flowObj.getAmount());
            flowObjectList.forEach(flowObj -> flowMatrix[flowObj.getDest()][flowObj.getSource()] = flowObj.getAmount());

            costObjectList.forEach(costObj -> costMatrix[costObj.getSource()][costObj.getDest()] = costObj.getCost());
            costObjectList.forEach(costObj -> costMatrix[costObj.getDest()][costObj.getSource()] = costObj.getCost());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public int[][] getFlowMatrix() {
        return flowMatrix;
    }

    public int[][] getCostMatrix() {
        return costMatrix;
    }
}
