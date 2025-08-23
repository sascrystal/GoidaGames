package io.github.some_example_name.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;


public class Account {
    private static final int BASE_MONEY = 100;
    private static final String PREFS_NAME = "EconomicPref";
    private int indexOfSelectedSkin;
    private int money;

    public Account() {
        Preferences preferences = Gdx.app.getPreferences(PREFS_NAME);
        money = preferences.getInteger("money", BASE_MONEY);
        indexOfSelectedSkin = preferences.getInteger("index_selected_Skin", 0);
    }

    public int getIndexOfSelectedSkin() {
        return indexOfSelectedSkin;
    }

    public void setIndexOfSelectedSkin(int indexOfSelectedSkin) {
        this.indexOfSelectedSkin = indexOfSelectedSkin;
    }

    public void saveMoney() {
        Preferences preferences = Gdx.app.getPreferences(PREFS_NAME);
        preferences.putInteger("money", money);
        preferences.flush();
    }

    public void saveIndexOfSelectedSkin() {
        Preferences preferences = Gdx.app.getPreferences(PREFS_NAME);
        preferences.putInteger("index_selected_Skin", indexOfSelectedSkin);
        preferences.flush();
    }


    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
