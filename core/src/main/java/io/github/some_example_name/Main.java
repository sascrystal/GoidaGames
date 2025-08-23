package io.github.some_example_name;



import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;

import io.github.some_example_name.screens.FirstScreen;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {
    private static final String PREFS_NAME = "game_pres",FIRST_RUN_KEY = "isFirstRun";
    private static void initDefaultSkins() {
        Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);
        boolean isFirstRun = prefs.getBoolean(FIRST_RUN_KEY, true);


        if (isFirstRun) {
            FileHandle internalFile = Gdx.files.internal("skinss_data.json");

            internalFile.copyTo(Gdx.files.local("skins_data.json"));

            isFirstRun = false;
            prefs.getBoolean(FIRST_RUN_KEY,isFirstRun);
            prefs.flush();
        }
    }

    @Override
    public void create() {
        //setScreen(new FirstScreen());
        initDefaultSkins();
        this.setScreen(new FirstScreen());
    }

}
