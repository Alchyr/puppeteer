package puppeteer.cards.common.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.BaseCard;
import puppeteer.cards.summon.SummonHourai;
import puppeteer.cards.summon.SummonShanghai;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class DollActivation extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "DollActivation",
            3,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int SUMMON = 3;
    private static final int UPGRADE_COST = 2;

    public DollActivation()
    {
        super(cardInfo, false);

        setCostUpgrade(UPGRADE_COST);
        setMagic(SUMMON);
        setExhaust(true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new SummonShanghai(), this.magicNumber));
    }
}