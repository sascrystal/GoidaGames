package io.github.some_example_name.cell_map_classes.cell_maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.some_example_name.cell_map_classes.events.DialogEvent;
import io.github.some_example_name.screens.MapScreen;

public class EventCell extends CellMap {
    private final DialogEvent dialogEvent;
    private static final Animation<TextureRegion> ANIMATION_MARK;

    static {
        TextureAtlas frames = new TextureAtlas(Gdx.files.internal("cell/questionMark.atlas"));
        ANIMATION_MARK = new Animation<>(0.1f,
            frames.findRegions("questionMark"),
            Animation.PlayMode.LOOP);
    }

    public EventCell(DialogEvent dialogEvent, CellMap cell) {
        this.dialogEvent = dialogEvent;
        texture = new Texture(Gdx.files.internal("cell/emptyCell.png"));
        this.bounds = cell.bounds;
    }

    @Override
    public void draw(SpriteBatch batch, float elapsedTime) {
        int cellWight = 150;
        super.draw(batch, elapsedTime);
        if (isAvailable) {
            TextureRegion currentFrame = ANIMATION_MARK.getKeyFrame(elapsedTime, true);
            batch.draw(currentFrame,
                bounds.getX() + (float) cellWight / 2 - 32,
                bounds.getY());
        }
    }

    @Override
    public void action(MapScreen map) {
        isAvailable = false;
        dialogEvent.begin(map);
    }

}
