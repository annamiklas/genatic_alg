package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

@Getter
@AllArgsConstructor
public enum BoardType {
    EASY("easy", new Dimension(3,3), 9, 4818),
    FLAT("flat", new Dimension(12,1), 12, -1),
    HARD("hard", new Dimension(5,6), 24, -1);

    private String boardName;
    private Dimension dimension;
    private int totalIndividualNumber;
    private int optimum;
}
