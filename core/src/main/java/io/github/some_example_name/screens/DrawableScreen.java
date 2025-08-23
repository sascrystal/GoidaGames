package io.github.some_example_name.screens;

import com.badlogic.gdx.utils.viewport.Viewport;

public interface DrawableScreen {
    void draw(float delta);

    void show();

    void resize(int width, int height);

    Viewport getViewport();
}
