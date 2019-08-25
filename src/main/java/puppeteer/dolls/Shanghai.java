package puppeteer.dolls;

import basemod.animations.SpineAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.actions.character.ChangeDollAnimationAction;

import static puppeteer.PuppeteerMod.makeDollPath;
import static puppeteer.PuppeteerMod.makeID;

public class Shanghai extends AbstractDoll {
    private static final String atlasPath = makeDollPath("shanghai/shanghai.atlas");
    private static final TextureAtlas atlas = new TextureAtlas(Gdx.files.internal(atlasPath));

    private static SpineAnimation stand = new SpineAnimation(atlasPath, makeDollPath("shanghai/shanghai_stand.json"), 2.0f);

    private static SpineAnimation spearTransition = new SpineAnimation(atlasPath, makeDollPath("shanghai/shanghai_speartransition.json"), 2.0f);
    private static SpineAnimation spearForward = new SpineAnimation(atlasPath, makeDollPath("shanghai/shanghai_forward.json"), 2.0f);

    private static final float width = 120 * Settings.scale;
    private static final float height = 140 * Settings.scale;

    private static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(makeID("Shanghai"));

    private static String name = uiStrings.TEXT[0];
    private static String[] description = uiStrings.EXTRA_TEXT;

    private static final int baseCharge = 4;

    public Shanghai(float x, float y)
    {
        super(name, description, baseCharge, atlas, x, y, stand, "stand", width, height);
    }

    @Override
    public void onEndTurn() {
        AbstractDungeon.actionManager.addToBottom(new ChangeDollAnimationAction(this, spearTransition, "speartransition", spearForward, "forward"));
        AbstractDungeon.actionManager.addToBottom(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(this.charge, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
    }
}
