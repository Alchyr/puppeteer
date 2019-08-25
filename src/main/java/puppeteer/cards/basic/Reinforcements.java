package puppeteer.cards.basic;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.BaseCard;
import puppeteer.cards.summon.SummonShanghai;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class Reinforcements extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Reinforcements",
            2,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.BASIC
    );

    public final static String ID = makeID(cardInfo.cardName);

    private final static int BLOCK = 8;

    private final static int SUMMON = 1;
    private final static int UPG_SUMMON = 1;

    public Reinforcements()
    {
        super(cardInfo, false);

        setBlock(BLOCK);
        setMagic(SUMMON, UPG_SUMMON);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new SummonShanghai(), this.magicNumber));
    }
}