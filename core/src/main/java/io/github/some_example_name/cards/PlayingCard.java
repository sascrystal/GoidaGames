package io.github.some_example_name.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

import io.github.some_example_name.cards.non_target_cards.buff_cards.CookieOfDobor;
import io.github.some_example_name.cards.non_target_cards.buff_cards.CookieOfMana;
import io.github.some_example_name.cards.non_target_cards.buff_cards.CookieOfPower;
import io.github.some_example_name.cards.non_target_cards.buff_cards.CookieOfReinforce;
import io.github.some_example_name.cards.non_target_cards.buff_cards.CookiesOfMadness;
import io.github.some_example_name.cards.non_target_cards.defence_cards.CupDefense;
import io.github.some_example_name.cards.non_target_cards.defence_cards.Evade;
import io.github.some_example_name.cards.non_target_cards.defence_cards.GlassShield;
import io.github.some_example_name.cards.non_target_cards.defence_cards.Inheritance;
import io.github.some_example_name.cards.target_cards.Overload;
import io.github.some_example_name.cards.target_cards.attack_cards.AttackVoid;
import io.github.some_example_name.cards.target_cards.attack_cards.ComboAttack;
import io.github.some_example_name.cards.target_cards.attack_cards.FeintCard;
import io.github.some_example_name.cards.target_cards.attack_cards.GlassSword;
import io.github.some_example_name.cards.target_cards.attack_cards.LetsGoGambling;
import io.github.some_example_name.cards.target_cards.attack_cards.PhantomPain;
import io.github.some_example_name.cards.target_cards.attack_cards.SugarSplash;
import io.github.some_example_name.cards.target_cards.attack_cards.TeapotToss;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;


public abstract class PlayingCard {
    public static final float WIDTH, HEIGHT;
    private static final BitmapFont FONT_MANA;
    private static final BitmapFont FONT_DESCRIPTION;
    private static final BitmapFont FONT_NAME;

    static {
        WIDTH = (new Texture(Gdx.files.internal("cards/noDataCard.png"))).getWidth();
        HEIGHT = (new Texture(Gdx.files.internal("cards/noDataCard.png")).getHeight());

        FONT_NAME = new BitmapFont(Gdx.files.internal("fonts/font.fnt"), Gdx.files.internal("fonts/font.png"), false);
        FONT_DESCRIPTION = new BitmapFont(Gdx.files.internal("fonts/font.fnt"), Gdx.files.internal("fonts/font.png"), false);
        FONT_MANA = new BitmapFont(Gdx.files.internal("fonts/font.fnt"), Gdx.files.internal("fonts/font.png"), false);

        FONT_NAME.getData().setScale(1.1f, 0.3f);
        FONT_DESCRIPTION.getData().setScale(0.89f, 0.4f);
        FONT_MANA.getData().setScale(0.78f);

        FONT_NAME.setColor(0, 0, 0, 0.85f);
        FONT_DESCRIPTION.setColor(0, 0, 0, 0.85f);
        FONT_MANA.setColor(0, 0, 0, 0.85f);
    }

    protected String name, description;
    protected int cost;
    protected boolean burnable = false, ethereal = false;
    protected Texture texture = new Texture(Gdx.files.internal("cards/noDataCard.png"));
    protected Sound soundEffect = Gdx.audio.newSound(Gdx.files.internal("sounds/Zaglushka.wav"));
    protected TextureAtlas frames = new TextureAtlas(Gdx.files.internal("animationCards/gravity.atlas"));
    protected Animation<TextureRegion> effect = new Animation<>(1 / 15F,
        frames.findRegions("Gravity-Sheet"),
        Animation.PlayMode.NORMAL);

    public static PlayingCard generateCard() {
        ArrayList<PlayingCard> CARDS = new ArrayList<>();
        CARDS.add(new Overload());
        CARDS.add(new AttackVoid());
        CARDS.add(new ComboAttack());
        CARDS.add(new FeintCard());
        CARDS.add(new LetsGoGambling());
        CARDS.add(new PhantomPain());
        CARDS.add(new SugarSplash());
        CARDS.add(new TeapotToss());
        CARDS.add(new CupDefense());
        CARDS.add(new CupDefense());
        CARDS.add(new Evade());
        CARDS.add(new GlassShield());
        CARDS.add(new Inheritance());
        CARDS.add(new CookieOfDobor());
        CARDS.add(new CookieOfMana());
        CARDS.add(new CookieOfPower());
        CARDS.add(new CookieOfReinforce());
        CARDS.add(new CookiesOfMadness());
        CARDS.add(new GlassSword());

        return CARDS.get(MathUtils.random(CARDS.size() - 1));
    }

    //Пояснение - перегрузка более оптимизирована для большого количества карт.
    public static PlayingCard[] generateCard(int count) {
        ArrayList<PlayingCard> CARDS = new ArrayList<>();
        CARDS.add(new Overload());
        CARDS.add(new AttackVoid());
        CARDS.add(new ComboAttack());
        CARDS.add(new FeintCard());
        CARDS.add(new LetsGoGambling());
        CARDS.add(new PhantomPain());
        CARDS.add(new SugarSplash());
        CARDS.add(new TeapotToss());
        CARDS.add(new CupDefense());
        CARDS.add(new CupDefense());
        CARDS.add(new Evade());
        CARDS.add(new GlassShield());
        CARDS.add(new Inheritance());
        CARDS.add(new CookieOfDobor());
        CARDS.add(new CookieOfMana());
        CARDS.add(new CookieOfPower());
        CARDS.add(new CookieOfReinforce());
        CARDS.add(new CookiesOfMadness());
        PlayingCard[] randomCards = new PlayingCard[count];
        for (int i = 0; i < randomCards.length; i++) {
            int random = MathUtils.random(CARDS.size() - 1);
            randomCards[i] = CARDS.get(random);
            CARDS.remove(random);
        }
        return randomCards;
    }

    public void cardAction(Enemy x, Player y, int index) {
        y.useManaForCard(this);
        soundEffect.play(0.7f);

    }

    public void drawAnimation(float time, Batch batch, Enemy enemy) {
        TextureRegion currentFrame = effect.getKeyFrame(time, false);
        batch.draw(currentFrame, enemy.getBounds().x, enemy.getBounds().y,
            enemy.getBounds().width, enemy.getBounds().height);
    }

    public void draw(SpriteBatch batch, float x, float y, float width, float height) {
        batch.draw(texture, x, y, width, height);

        FONT_MANA.draw(batch, String.valueOf(getCost()), x + width * 0.12f, y + height * 0.95f);
        FONT_NAME.draw(batch, name, x + width * 0.18f, y + height * 0.515f);
        //FONT_DESCRIPTION.draw(batch, description, x+width*0.20f, y+height*0.4f);
        drawWrappedDescription(batch, x, y, width, height);
    }

    public void draw(SpriteBatch batch, float x, float y) {
        batch.draw(texture, x, y);
        FONT_MANA.draw(batch, String.valueOf(getCost()), x + WIDTH * 0.12f, y + HEIGHT * 0.95f);
        FONT_NAME.draw(batch, name, x + WIDTH * 0.18f, y + HEIGHT * 0.53f);

    }

    private void drawWrappedDescription(SpriteBatch batch, float x, float y, float width, float height) {
        GlyphLayout layout = new GlyphLayout();
        float descWidth = width * 0.7f; // Ширина области для описания
        float padding = width * 0.16f; // Отступ слева

        // Устанавливаем текст с переносом
        layout.setText(FONT_DESCRIPTION, description,
            FONT_DESCRIPTION.getColor(),
            descWidth,
            Align.left,
            true);

        // Рисуем текст описания
        FONT_DESCRIPTION.draw(batch, layout,
            x + padding,
            y + height * 0.235f + layout.height);
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public Texture getTexture() {
        return texture;
    }

    public String getDescription() {
        return description;
    }

    public boolean isBurnable() {
        return burnable;
    }

    public boolean isEthereal() {
        return ethereal;
    }

    public Animation<TextureRegion> getEffect() {
        return effect;
    }
}

//CardAttackList





