package puppeteer.cards.summon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.AbstractSpecialTargetCard;
import puppeteer.actions.character.SummonDollAction;
import puppeteer.dolls.Shanghai;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class SummonShanghai extends AbstractSpecialTargetCard {
    private final static CardInfo cardInfo = new CardInfo(
            "SummonShanghai",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.COMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static Shanghai drawDoll;
    private static boolean canRenderDoll = true;

    public SummonShanghai()
    {
        super(cardInfo, true);

        if (drawDoll == null)
            drawDoll = new Shanghai(0, 0);

        setExhaust(true, false);
        AlwaysRetainField.alwaysRetain.set(this, true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SummonDollAction(new Shanghai(current_x, this.current_y)));
    }

    @Override
    public void specialRender(SpriteBatch sb) {
        if (canRenderDoll)
        {
            drawDoll.setPosition(this.current_x, this.current_y);
            drawDoll.getClosestMonster();
            drawDoll.render(sb, false, transparent);

            sb.setColor(Color.WHITE);

            if (drawDoll.closest != null)
                drawDoll.closest.renderReticle(sb);

            canRenderDoll = false;
        }
    }

    @Override
    public void update() {
        super.update();
        canRenderDoll = true;
    }
}