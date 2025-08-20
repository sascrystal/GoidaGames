package io.github.some_example_name.screens;

import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public interface DrawableScreen {
    public void draw(float delta);
    public void show();
    public void resize(int width, int height);
    public Viewport getViewport();
}
