package io.github.some_example_name.cell_map_classes.cell_maps;

import io.github.some_example_name.cell_map_classes.stage.Stage;
import io.github.some_example_name.screens.MapScreen;

public class FightCellTutorial extends FightCell{
    private String path;
    public FightCellTutorial(Stage stage, CellMap cell,String path) {
        super(stage, cell);
        this.path = path;
    }

    @Override
    public void action(MapScreen map) {
        isAvailable = false;
        stage.stageActionTutorial(map,path);
    }
}
