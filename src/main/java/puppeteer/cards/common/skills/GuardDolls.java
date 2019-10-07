package puppeteer.cards.common.skills;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.abstracts.BaseCard;
import puppeteer.fields.DollFields;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class GuardDolls extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "GuardDolls",
            1,
            CardType.SKILL,
            CardTarget.SELF,
            CardRarity.COMMON);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int BLOCK = 2;
    private static final int UPG_BLOCK = 1;

    public GuardDolls() {
        super(cardInfo, false);

        setBlock(BLOCK, UPG_BLOCK);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!DollFields.dolls.get(p).isEmpty())
        {
            for (AbstractDoll d : DollFields.dolls.get(p))
            {
                block();
            }
        }
    }

    public AbstractCard makeCopy() {
        return new GuardDolls();
    }
}