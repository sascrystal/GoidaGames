package io.github.some_example_name.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;


public class ShopSkin {
    private static final float SCALE_FOR_SKIN = 0.1f;

    private transient Texture texture;
    private int id;
    private int price;
    private boolean isUnlocked;
    private String path;


    public ShopSkin() {


    }

    public static ShopSkin[] getAllSkins() {
        Gdx.app.log("GOIDA","GOIDA");
        Json json = new Json();
        ShopSkin[] skinData = json.fromJson(ShopSkin[].class, Gdx.files.local("data/skins_data.json"));
        for (ShopSkin skin : skinData) {
            skin.addTexture();
        }
        return skinData;
    }

    public static ArrayList<ShopSkin> takeOnlyLockedSkins(ShopSkin[] skins) {
        ArrayList<ShopSkin> lockedSkins = new ArrayList<>();
        for (ShopSkin skin : skins) {
            if (!skin.isUnlocked) {
                lockedSkins.add(skin);
            }
        }
        return lockedSkins;
    }

    public static ArrayList<ShopSkin> takeOnlyUnlockedSkins(ShopSkin[] skins) {
        ArrayList<ShopSkin> lockedSkins = new ArrayList<>();
        for (ShopSkin skin : skins) {
            if (skin.isUnlocked) {
                lockedSkins.add(skin);
            }
        }
        return lockedSkins;
    }

    public static void saveSkins(ShopSkin[] allSkins) {
        Json json = new Json();
        //json.setTypeName("skins");
        //json.setUsePrototypes(false);

        String jsonData = json.toJson(allSkins);
        FileHandle file = Gdx.files.local("data/skins_data.json");
        file.writeString(jsonData, false);

    }

    public String getPath() {
        return path;
    }

    public int getPrice() {
        return price;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }

    private void addTexture() {
        texture = new Texture(Gdx.files.internal(path + "/right_sight.png"));
    }

    public void draw(SpriteBatch batch, float x, float y) {
        batch.draw(texture, x - texture.getWidth() * SCALE_FOR_SKIN / 2, y, texture.getWidth() * SCALE_FOR_SKIN, texture.getHeight() * SCALE_FOR_SKIN);

    }

    public void draw(SpriteBatch batch, float x, float y, float scale) {
        batch.draw(texture, x - texture.getWidth() * scale / 2, y, texture.getWidth() * scale, texture.getHeight() * scale);

    }

    public void dispose() {
        if (texture != null) {
            texture.dispose();
        }
    }


}
