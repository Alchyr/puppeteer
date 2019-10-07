package puppeteer.cards.uncommon.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import puppeteer.abstracts.BaseCard;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class SlightExertion extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "SlightExertion",
            1,
            CardType.POWER,
            CardTarget.SELF,
            CardRarity.UNCOMMON);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int UPG_COST = 0;

    private static final int MAGIC = 3;

    public SlightExertion() {
        super(cardInfo, false);

        setMagic(MAGIC);
        setCostUpgrade(UPG_COST);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applySelf(new StrengthPower(p, this.magicNumber));
    }

    public AbstractCard makeCopy() {
        return new SlightExertion();
    }
}