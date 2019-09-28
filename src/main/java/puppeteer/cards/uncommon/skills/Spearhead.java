package puppeteer.cards.uncommon.skills;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.tools.particleeditor.Chart;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.abstracts.AbstractDollTargetCard;
import puppeteer.actions.character.ChangeDollAnimationAction;
import puppeteer.actions.character.DollChargeEnemyAction;
import puppeteer.dolls.Hourai;
import puppeteer.dolls.Shanghai;
import puppeteer.enums.CardTargetEnums;
import puppeteer.fields.DollFields;
import puppeteer.magic.DollCharge;
import puppeteer.patches.DollAndMagicRenderPatch;
import puppeteer.util.CardInfo;
import puppeteer.util.HitboxHelper;

import static puppeteer.PuppeteerMod.makeID;

public class Spearhead extends AbstractDollTargetCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Spearhead",
            1,
            CardType.SKILL,
            CardTargetEnums.DOLL,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int UPGRADE_COST = 0;

    private DollCharge magic = new DollCharge();

    public Spearhead()
    {
        super(cardInfo, false);

        setCostUpgrade(UPGRADE_COST);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDoll d = getTargetDoll();

        if (d != null)
        {
            AbstractMonster target = HitboxHelper.getClosestMonster(d.hb.cX, d.hb.cY);
            if (target != null)
            {
                d.autoFace = false;
                if (d instanceof Shanghai)
                {
                    AbstractDungeon.actionManager.addToBottom(new ChangeDollAnimationAction(d, Shanghai.spearTransition, "speartransition", Shanghai.spearForward, "forward", false));
                }
                else if (d instanceof Hourai)
                {
                    AbstractDungeon.actionManager.addToBottom(new ChangeDollAnimationAction(d, Hourai.spearTransition, "speartransition", Hourai.spearForward, "forward", false));
                }
                AbstractDungeon.actionManager.addToBottom(new DollChargeEnemyAction(d, target, magic.getCopy(), DollChargeEnemyAction.DollDamageType.MULTIPLIER, 2));
            }
        }
    }


    @Override
    public void update() {
        super.update();
        if (AbstractDungeon.player != null &&
                AbstractDungeon.player.inSingleTargetMode &&
                this.equals(AbstractDungeon.player.hoveredCard) &&
                !DollFields.dolls.get(AbstractDungeon.player).isEmpty())
        {
            AbstractDoll d = getDirectHoverDoll();

            if (d != null)
            {
                AbstractMonster m = HitboxHelper.getClosestMonster(d.hb.cX, d.hb.cY);
                if (m != null)
                {
                    magic.setOrigin(d.hb.cX, d.hb.cY);
                    Chart.Point p = DollChargeEnemyAction.getTargetPoint(d, m);
                    magic.setTarget(p.x, p.y);

                    DollAndMagicRenderPatch.renderMagicWithReticle(magic);
                }
            }
        }
    }
}