package puppeteer.character;

import basemod.abstracts.CustomPlayer;
import basemod.animations.AbstractAnimation;
import basemod.animations.SpineAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.relics.Abacus;
import com.megacrit.cardcrawl.relics.PreservedInsect;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import puppeteer.cards.basic.Defend;
import puppeteer.cards.basic.Reinforcements;
import puppeteer.cards.basic.Strike;
import puppeteer.enums.CharacterEnums;
import puppeteer.patches.EnergyFontGen;
import puppeteer.util.AnimationData;

import java.util.ArrayList;
import java.util.HashMap;

import static puppeteer.PuppeteerMod.*;

public class Puppeteer extends CustomPlayer {

    //stats
    public static final int ENERGY_PER_TURN = 3;
    public static final int STARTING_HP = 65;
    public static final int MAX_HP = 65;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    //strings
    private static final String ID = makeID("Puppeteer");
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;

    private static final String SHOULDER_1 = makeCharacterPath("shoulder.png");
    private static final String SHOULDER_2 = makeCharacterPath("shoulder2.png");
    private static final String CORPSE = makeCharacterPath("corpse.png");

    public static final String[] orbTextures = {
            makeOrbPath("layer1.png"),
            makeOrbPath("layer2.png"),
            makeOrbPath("layer3.png"),
            makeOrbPath("layer4.png"),
            makeOrbPath("layer5.png"),
            makeOrbPath("layer6.png"),
            makeOrbPath("layer1d.png"),
            makeOrbPath("layer2d.png"),
            makeOrbPath("layer3d.png"),
            makeOrbPath("layer4d.png"),
            makeOrbPath("layer5d.png"),};

    private static final String orbVfx = makeOrbPath("vfx.png");

    private HashMap<SpineAnimation, AnimationData> animationMap = new HashMap<>();

    private static SpineAnimation stand = new SpineAnimation(SKELETON_ATLAS, STAND_JSON, 2.0f);
    private static SpineAnimation attack = new SpineAnimation(SKELETON_ATLAS, ATTACK_JSON, 2.0f);

    private static final float SCALE_MOD = 1.5f;

    public Puppeteer()
    {
        super(NAMES[0], CharacterEnums.PUPPETEER, null, null, null, new AbstractAnimation() {
            @Override
            public Type type() {
                return Type.NONE;
            }
        });

        initializeClass(null,
                SHOULDER_1,
                SHOULDER_2,
                CORPSE,
                getLoadout(),
                20.0F, -10.0F, 220.0F, 290.0F,
                new EnergyManager(ENERGY_PER_TURN));

        this.atlas = null;

        changeAnimation(stand, "stand");

        //load attack animation ahead of time
        loadAnimation(attack, "attack"); //if loop is false it be dumb

        dialogX = (drawX + 0.0F * Settings.scale); //location for text bubbles
        dialogY = (drawY + 220.0F * Settings.scale);
    }

    private void loadAnimation(SpineAnimation anim, String animationName)
    {
        SkeletonJson json = new SkeletonJson(this.atlas);

        json.setScale(Settings.scale * SCALE_MOD);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(anim.skeletonUrl));
        Skeleton skeleton = new Skeleton(skeletonData);
        skeleton.setColor(Color.WHITE);
        AnimationStateData stateData = new AnimationStateData(skeletonData);
        AnimationState state = new AnimationState(stateData);

        AnimationState.TrackEntry e = state.setAnimation(0, animationName, true);

        animationMap.put(anim, new AnimationData(skeleton, stateData, state, e));
    }

    private void changeAnimation(SpineAnimation next, String animationName)
    {
        if (this.atlas == null || isDisposed(this.atlas))
        {
            this.atlas = new TextureAtlas(Gdx.files.internal(SKELETON_ATLAS));
        }
        this.animation = next;

        if (!animationMap.containsKey(next))
        {
            SkeletonJson json = new SkeletonJson(this.atlas);

            json.setScale(Settings.scale * SCALE_MOD);
            SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(next.skeletonUrl));
            this.skeleton = new Skeleton(skeletonData);
            this.skeleton.setColor(Color.WHITE);
            this.stateData = new AnimationStateData(skeletonData);
            this.state = new AnimationState(this.stateData);

            AnimationState.TrackEntry e = state.setAnimation(0, animationName, true);

            animationMap.put(next, new AnimationData(skeleton, stateData, state, e));
        }
        else
        {
            this.skeleton = animationMap.get(next).skeleton;
            this.stateData = animationMap.get(next).stateData;
            this.state = animationMap.get(next).state;
        }

        animationMap.get(next).trackEntry.setTime(0);
    }

    @Override
    protected void updateFastAttackAnimation() {
        if (animation.equals(attack))
        {
            if (animationMap.get(attack).trackEntry.isComplete())
            {
                changeAnimation(stand, "stand");
                this.animationTimer = 0.0f;
            }
        }
        else
        {
            changeAnimation(stand, "stand");
            this.animationTimer = 0.0f;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
    }

    @Override
    public void useFastAttackAnimation() {
        super.useFastAttackAnimation();
        changeAnimation(attack, "attack");
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Reinforcements.ID);

        /*
        retVal.add(DefaultCommonAttack.ID);
        retVal.add(DefaultUncommonAttack.ID);
        retVal.add(DefaultRareAttack.ID);

        retVal.add(DefaultCommonSkill.ID);
        retVal.add(DefaultUncommonSkill.ID);
        retVal.add(DefaultRareSkill.ID);

        retVal.add(DefaultCommonPower.ID);
        retVal.add(DefaultUncommonPower.ID);
        retVal.add(DefaultRarePower.ID);*/

        return retVal;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(Abacus.ID);
        UnlockTracker.markRelicAsSeen(Abacus.ID);

        return retVal;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA(getCustomModeCharacterButtonSoundKey(), 1.25f);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT,
                false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_DAGGER_1";
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 4;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return CharacterEnums.PUPPETEER_CARD_COLOR;
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return EnergyFontGen.puppeteerEnergyFont;
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Strike_Red();
    }

    @Override
    public Color getCardTrailColor() {
        return PUPPETEER_COLOR;
    }

    @Override
    public Color getCardRenderColor() {
        return PUPPETEER_COLOR;
    }

    @Override
    public Color getSlashAttackColor() {
        return PUPPETEER_COLOR;
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.SMASH,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        };
    }

    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new Puppeteer();
    }


    private static boolean isDisposed(TextureAtlas atlas)
    {
        for (Texture t : atlas.getTextures())
        {
            if (t.getTextureObjectHandle() == 0)
                return true;
        }
        return false;
    }
}
