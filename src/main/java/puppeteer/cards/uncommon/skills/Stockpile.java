package puppeteer.cards.uncommon.skills;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.BaseCard;
import puppeteer.actions.generic.DrawAndSaveCardsAction;
import puppeteer.actions.generic.IncreaseCostAction;
import puppeteer.actions.generic.IncreaseDrawnCostAction;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class Stockpile extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Stockpile",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int MAGIC = 3;
    private static final int UPG_MAGIC = 1;

    public Stockpile() {
        super(cardInfo, false);

        setMagic(MAGIC, UPG_MAGIC);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        DrawAndSaveCardsAction draw = new DrawAndSaveCardsAction(p, this.magicNumber);

        addToBot(draw);
        addToBot(new IncreaseDrawnCostAction(draw, 1));
    }

    public AbstractCard makeCopy() {
        return new Stockpile();
    }
}