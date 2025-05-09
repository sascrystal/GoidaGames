package io.github.some_example_name.cell_map_classes.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import io.github.some_example_name.cards.PlayingCard;
import io.github.some_example_name.cards.target_cards.Overload;
import io.github.some_example_name.cell_map_classes.cell_maps.CellMap;
import io.github.some_example_name.player.Player;

public class GolikovEvent extends DialogEvent {
    public GolikovEvent() {
        background = new Texture(Gdx.files.internal("backgrounds/golikov_event_background.jpg"));
        eventDescription = "Вы встречаете высокого человека с очками для зрения, у него в руке две карты: одна пишет bash скрипт на питоне, другая делает тебя амбассадором vibe-coding. Что ты сделаешь?";
        dialogOptions = new String[3];
        dialogOptions[0] = "Стать программистом на питоне";
        dialogOptions[1] = "Стать амбассадором vibe-coding";
        dialogOptions[2] = "свалить отсюда подальше";
    }

    @Override
    public void actionFirst(Player player, CellMap[][] map) {
        PlayingCard givingCard = new Overload();
        player.addCardInDeck(givingCard);
        super.actionFirst(player, map);
    }

    @Override
    public void actionSecond(Player player, CellMap[][] map) {
        PlayingCard givingCard = PlayingCard.generateCard();
        player.addCardInDeck(givingCard);
        super.actionSecond(player, map);
    }
}

