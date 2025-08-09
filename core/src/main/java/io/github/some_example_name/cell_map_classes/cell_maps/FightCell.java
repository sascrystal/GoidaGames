package io.github.some_example_name.cell_map_classes.cell_maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.some_example_name.cell_map_classes.stage.Stage;
import io.github.some_example_name.screens.MapScreen;

public class FightCell extends CellMap {
    private static final Animation<TextureRegion> ANIMATION_MARK;
    private static final float MARK_SCALE = 0.85f;

    static {
        TextureAtlas frames = new TextureAtlas(Gdx.files.internal("cell/mark.atlas"));
        ANIMATION_MARK = new Animation<>(0.1f,
            frames.findRegions("Mark"),
            Animation.PlayMode.LOOP);
    }

    private final Stage stage;

    public FightCell(Stage stage, CellMap cell) {
        this.stage = stage;
        texture = new Texture(Gdx.files.internal("cell/emptyCell.png"));
        this.bounds = cell.bounds;
    }

    @Override
    public void action(MapScreen map) {
        isAvailable = false;
        stage.stageAction(map);
    }

    @Override
    public void draw(SpriteBatch batch, float elapsedTime) {
        int cellWight = 150;
        int cellHeight = 150;
        super.draw(batch, elapsedTime);
        if (isAvailable) {
            TextureRegion currentFrame = ANIMATION_MARK.getKeyFrame(elapsedTime, true);
            batch.draw(currentFrame,
                bounds.getX() + (float) cellWight / 2 - (float) currentFrame.getRegionWidth()*MARK_SCALE /2,
                bounds.getY()+(float) cellHeight / 2 - (float) currentFrame.getRegionHeight()*MARK_SCALE /2,
                currentFrame.getRegionWidth()*MARK_SCALE,
                currentFrame.getRegionHeight()*MARK_SCALE);
        }
    }
}
