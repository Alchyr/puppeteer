package puppeteer.cards.summon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.AbstractSpecialTargetCard;
import puppeteer.actions.character.SummonDollAction;
import puppeteer.dolls.Hourai;
import puppeteer.patches.DollAndMagicRenderPatch;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class SummonHourai extends AbstractSpecialTargetCard {
    private final static CardInfo cardInfo = new CardInfo(
            "SummonHourai",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.COMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static Hourai drawDoll;
    private static boolean canRenderDoll = true;

    public SummonHourai()
    {
        super(cardInfo, true);

        if (drawDoll == null)
            drawDoll = new Hourai(0, 0);

        setExhaust(true, false);
        AlwaysRetainField.alwaysRetain.set(this, true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SummonDollAction(new Hourai(current_x, this.current_y)));
    }

    @Override
    public void specialRender(SpriteBatch sb) {
        if (canRenderDoll)
        {
            drawDoll.setPosition(this.current_x, this.current_y);
            drawDoll.getClosestMonster();
            drawDoll.render(sb, false, transparent);
            DollAndMagicRenderPatch.renderMagic(drawDoll.magic);

            sb.setColor(Color.WHITE);

            if (drawDoll.closest != null)
            {
                for (AbstractMonster m : drawDoll.magic.getTargets())
                {
                    m.renderReticle(sb);
                }
            }

            canRenderDoll = false;
        }
    }

    @Override
    public void update() {
        super.update();
        canRenderDoll = true;
    }
}