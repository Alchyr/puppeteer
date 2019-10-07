package puppeteer.dolls;

import basemod.animations.SpineAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.actions.character.ChangeDollAnimationAction;
import puppeteer.actions.generic.DamageSpecificEnemiesAction;
import puppeteer.magic.HouraiLaser;
import puppeteer.patches.DollAndMagicRenderPatch;

import static puppeteer.PuppeteerMod.*;

public class Hourai extends AbstractDoll {
    private static final String atlasPath = makeDollPath("hourai/hourai.atlas");
    private static final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));

    public static SpineAnimation stand = new SpineAnimation(atlasPath, makeDollPath("hourai/hourai_stand.json"), 2.0f);

    public static SpineAnimation spearTransition = new SpineAnimation(atlasPath, makeDollPath("hourai/hourai_speartransition.json"), 2.0f);
    public static SpineAnimation spearForward = new SpineAnimation(atlasPath, makeDollPath("hourai/hourai_forward.json"), 2.0f);

    public static SpineAnimation magicTransition = new SpineAnimation(atlasPath, makeDollPath("hourai/hourai_magictransition.json"), 2.0f);
    public static SpineAnimation magicHold = new SpineAnimation(atlasPath, makeDollPath("hourai/hourai_magic.json"), 2.0f);

    private static final float width = 120 * Settings.scale;
    private static final float height = 140 * Settings.scale;

    private static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("Hourai"));

    private static String name = uiStrings.TEXT[0];
    private static String[] description = uiStrings.EXTRA_TEXT;

    private static final int baseCharge = 4;

    public HouraiLaser magic;

    public Hourai(float x, float y)
    {
        super(name, description, baseCharge, atlas, x, y, stand, "stand", width, height);

        this.magic = new HouraiLaser();
        this.magic.setOrigin(x, y);

        if (closest != null)
        {
            this.magic.setTarget(closest.hb.cX, closest.hb.cY);
        }
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        this.magic.setOrigin(x, y);
    }

    @Override
    public AbstractMonster getClosestMonster() {
        super.getClosestMonster();

        if (closest != null && this.magic != null)
        {
            this.magic.setTarget(closest.hb.cX, closest.hb.cY);
        }

        logger.info("Closest enemy is null!");

        return closest;
    }

    @Override
    public void render(SpriteBatch sb, boolean showCharge, Color color) {
        super.render(sb, showCharge, color);
        if (this.animation.equals(magicTransition))
        {
            DollAndMagicRenderPatch.renderMagic(this.magic);
        }
    }

    @Override
    public void onEndTurn() {
        getClosestMonster();

        if (closest != null)
        {
            AbstractDungeon.actionManager.addToTop(new ChangeDollAnimationAction(this, stand, "stand"));
            AbstractDungeon.actionManager.addToTop(new DamageSpecificEnemiesAction(AbstractDungeon.player, magic.getTargets(), this.charge, AbstractGameAction.AttackEffect.FIRE, true));
            AbstractDungeon.actionManager.addToTop(new WaitAction(0.1f));
            AbstractGameAction vfx = this.magic.getVfx();
            if (vfx != null)
                AbstractDungeon.actionManager.addToTop(vfx);
            AbstractDungeon.actionManager.addToTop(new ChangeDollAnimationAction(this, magicTransition, "magictransition", magicHold, "magic", false));
        }
    }
}