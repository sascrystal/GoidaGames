package io.github.some_example_name.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;


public class ShopSkin {
    private static final float SCALE_FOR_SKIN=0.1f;

    private  transient Texture texture;
    private  int id;
    private int price;
    private boolean isUnlocked;
    private String path;

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
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

    public ShopSkin() {


    }

    public ShopSkin(String path, int index, int price, boolean isUnlocked) {
        this.path = path;
        this.texture = new Texture(Gdx.files.internal(this.path+"/right_sight.png"));
        this.id = index;
        this.price = price;
        this.isUnlocked = isUnlocked;
    }
    private void addTexture(){
        texture = new Texture(Gdx.files.internal(path+"/right_sight.png"));
    }

    public static ShopSkin[] getAllSkins(){
        Json json = new Json();
        ShopSkin[] skinData = json.fromJson(ShopSkin[].class, Gdx.files.local("characters/skins_data.json"));
        for(ShopSkin skin : skinData){
            skin.addTexture();
        }
        return skinData;
    }
    public static ArrayList<ShopSkin> takeOnlyLockedSkins( ShopSkin[] skins){
        ArrayList<ShopSkin> lockedSkins = new ArrayList<>();
        for (ShopSkin skin : skins){
            if(!skin.isUnlocked){
                lockedSkins.add(skin);
            }
        }
        return lockedSkins;
    }
    public void draw(SpriteBatch batch,float x,float y){
        batch.draw(texture,x-texture.getWidth()*SCALE_FOR_SKIN/2,y,texture.getWidth()*SCALE_FOR_SKIN,texture.getHeight()*SCALE_FOR_SKIN);

    }
    public void dispose(){
        if(texture != null){
            texture.dispose();
        }
    }
    public static void saveSkins(ShopSkin[] allSkins){
        Json json = new Json();
        json.setTypeName("skins");
        json.setUsePrototypes(false);

        String jsonData = json.prettyPrint(allSkins);
        FileHandle file = Gdx.files.local("characters/skins_data.json");
        file.writeString(jsonData,false);

    }


}
