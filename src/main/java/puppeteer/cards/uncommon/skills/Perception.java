package puppeteer.cards.uncommon.skills;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.AbstractSpecialTargetCard;
import puppeteer.util.CardInfo;
import puppeteer.util.HitboxHelper;

import static puppeteer.PuppeteerMod.makeID;

public class Perception extends AbstractSpecialTargetCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Perception",
            2,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private final static int BLOCK = 18;
    private final static int UPG_BLOCK = 5;

    public Perception()
    {
        super(cardInfo, false);

        setBlock(BLOCK, UPG_BLOCK);
    }

    @Override
    public void renderHoverShadow(SpriteBatch sb) {
        super.doRenderHoverShadow(sb);
    }

    @Override
    public void specialRender(SpriteBatch sb) {
        renderBase(sb);

        AbstractMonster m = HitboxHelper.getClosestMonster(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY);

        if (m != null)
            m.renderReticle(sb);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        m = HitboxHelper.getClosestMonster(p.hb.cX, p.hb.cY);
        if (m != null &&
                (m.intent == AbstractMonster.Intent.ATTACK ||
                        m.intent == AbstractMonster.Intent.ATTACK_BUFF ||
                        m.intent == AbstractMonster.Intent.ATTACK_DEBUFF ||
                        m.intent == AbstractMonster.Intent.ATTACK_DEFEND)) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
        }
    }
}