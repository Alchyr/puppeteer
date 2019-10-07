package puppeteer.cards.uncommon.skills;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.BaseCard;
import puppeteer.cards.summon.SummonHourai;
import puppeteer.cards.summon.SummonShanghai;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class Expand extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Expand",
            1,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = 1;

    public Expand() {
        super(cardInfo, false);

        setMagic(MAGIC, UPG_MAGIC);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new MakeTempCardInHandAction(new SummonShanghai()));
        addToBot(new DrawCardAction(p, this.magicNumber));
    }

    public AbstractCard makeCopy() {
        return new Expand();
    }
}