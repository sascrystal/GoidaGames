package io.github.some_example_name;



import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import io.github.some_example_name.screens.FirstScreen;
import io.github.some_example_name.utils.ShopSkin;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends Game {
    private static final String PREFS_NAME = "game_pres",FIRST_RUN_KEY = "isFirstRun";
    private static void initDefaultSkins() {
        FileHandle localFile = Gdx.files.local("data/skins_data.json");
        if (!localFile.exists()) {
            Json json = new Json();
            FileHandle internalFile = Gdx.files.internal("skinss_data.json");
            ShopSkin[] skins = json.fromJson(ShopSkin[].class, internalFile);
            String jsonString = json.toJson(skins);
            localFile.writeString(jsonString,false);



        }
    }

    @Override
    public void create() {
        //setScreen(new FirstScreen());
        initDefaultSkins();
        this.setScreen(new FirstScreen());
    }

}
