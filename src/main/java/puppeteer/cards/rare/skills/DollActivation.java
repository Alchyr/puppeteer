package puppeteer.cards.rare.skills;

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
            2,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.RARE
    );

    public final static String ID = makeID(cardInfo.cardName);

    private static final int SUMMON = 3;
    private static final int UPG_SUMMON = 2;

    public DollActivation()
    {
        super(cardInfo, false);



        setMagic(SUMMON, UPG_SUMMON);
        setExhaust(true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new SummonShanghai(), this.magicNumber));
    }
}