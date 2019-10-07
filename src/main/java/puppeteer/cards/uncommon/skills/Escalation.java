package puppeteer.cards.uncommon.skills;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import com.megacrit.cardcrawl.powers.EnergizedPower;
import puppeteer.abstracts.BaseCard;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class Escalation extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Escalation",
            0,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int MAGIC = 1;
    private static final int UPG_MAGIC = 1;

    public Escalation() {
        super(cardInfo, true);

        setMagic(MAGIC, UPG_MAGIC);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applySelf(new EnergizedPower(p, 1));
        applySelf(new DrawCardNextTurnPower(p, this.magicNumber));
    }

    public AbstractCard makeCopy() {
        return new Escalation();
    }
}