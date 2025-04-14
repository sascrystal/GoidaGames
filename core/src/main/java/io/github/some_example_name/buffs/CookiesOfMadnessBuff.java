package io.github.some_example_name.buffs;

import io.github.some_example_name.cards.PlayingCard;
import io.github.some_example_name.cards.non_target_cards.buff_cards.CookieOfDobor;
import io.github.some_example_name.cards.non_target_cards.buff_cards.CookieOfMana;
import io.github.some_example_name.cards.non_target_cards.buff_cards.CookieOfPower;
import io.github.some_example_name.cards.non_target_cards.buff_cards.CookieOfReinforce;
import io.github.some_example_name.player.Player;

public class CookiesOfMadnessBuff extends Buff {
    public CookiesOfMadnessBuff() {
        name = "Печеньковое безумие";
        description = "Дает за каждый стак в начале хода дает дополнительную карту печенек";
        decrease = false;
        inBeginTurn = true;
        stack = 1;
    }

    @Override
    public void buffAction(Player player) {
        int COUNT_OF_TYPE_COOKIES = 4;
        PlayingCard card = null;

        int answer = (int) (Math.random() * COUNT_OF_TYPE_COOKIES);
        switch (answer) {
            case 0:
                card = new CookieOfReinforce();
                break;
            case 1:
                card = new CookieOfMana();
                break;
            case 2:
                card = new CookieOfPower();
                break;
            case 3:
                card = new CookieOfDobor();
                break;
        }
        player.giveTheCard(card);


    }


}
