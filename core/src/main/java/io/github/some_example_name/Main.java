package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import io.github.some_example_name.screens.FirstScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {
    private static void initDefaultSkins() {
        FileHandle localFile = Gdx.files.local("skins_data.json");

        if (!localFile.exists()) {
            FileHandle internalFile = Gdx.files.internal("skins_data.json");

            internalFile.copyTo(localFile);

            Gdx.app.log("Files", "Скопировано в: " + localFile.path());
        }
    }

    @Override
    public void create() {
        //setScreen(new FirstScreen());
        initDefaultSkins();
        this.setScreen(new FirstScreen());
    }

}
