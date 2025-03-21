package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

abstract class CellMap {
    protected Rectangle bounds;
    protected Texture texture;
    protected boolean isPlayerIn;

    abstract public  void  action();
    public void draw(){}
}

