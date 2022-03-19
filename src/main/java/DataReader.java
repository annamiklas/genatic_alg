import models.CostObject;
import models.FlowObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.parboiled.common.Tuple2;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataReader {
    private static final String SOURCE_PATH = "src/main/resources/flo_dane_v1.2/";

    private static final String COST_NAME = "_cost.json";
    private static final String FLOW_NAME = "_flow.json";


    private static final String SOURCE_PROP_NAME = "source";
    private static final String DEST_PROP_NAME = "dest";
    private static final String COST_PROP_NAME = "cost";
    private static final String AMOUNT_PROP_NAME = "amount";


    public static Tuple2<ArrayList<CostObject>, ArrayList<FlowObject>> readData(String mode) {
        JSONParser jsonParser = new JSONParser();

        try {
            FileReader costReader = new FileReader(SOURCE_PATH + mode + COST_NAME);
            FileReader flowReader = new FileReader(SOURCE_PATH + mode + FLOW_NAME);

            JSONArray costJsonArray = (JSONArray) jsonParser.parse(costReader);
            JSONArray flowJsonArray = (JSONArray) jsonParser.parse(flowReader);
            List<CostObject> costObjectsList = new ArrayList<>();
            List<FlowObject> flowObjectsList = new ArrayList<>();

            costJsonArray.forEach(costObject -> costObjectsList.add(parseCostObject((JSONObject) costObject)));
            flowJsonArray.forEach(costObject -> flowObjectsList.add(parseFlowObject((JSONObject) costObject)));

            return (Tuple2<ArrayList<CostObject>, ArrayList<FlowObject>>) new Tuple2(costObjectsList, flowObjectsList);

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CostObject parseCostObject(JSONObject costObject) {
        return CostObject.builder()
                .source(Math.toIntExact((Long) costObject.get(SOURCE_PROP_NAME)))
                .dest(Math.toIntExact((Long) costObject.get(DEST_PROP_NAME)))
                .cost(Math.toIntExact((Long) costObject.get(COST_PROP_NAME)))
                .build();
    }

    public static FlowObject parseFlowObject(JSONObject costObject) {
        return FlowObject.builder()
                .source(Math.toIntExact((Long) costObject.get(SOURCE_PROP_NAME)))
                .dest(Math.toIntExact((Long) costObject.get(DEST_PROP_NAME)))
                .amount(Math.toIntExact((Long) costObject.get(AMOUNT_PROP_NAME)))
                .build();
    }
}
