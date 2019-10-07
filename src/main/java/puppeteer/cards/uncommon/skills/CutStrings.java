package puppeteer.cards.uncommon.skills;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.AbstractDoll;
import puppeteer.abstracts.AbstractDollTargetCard;
import puppeteer.abstracts.BaseCard;
import puppeteer.actions.character.DestroyDollAction;
import puppeteer.actions.character.TriggerDollPassiveAction;
import puppeteer.enums.CardTargetEnums;
import puppeteer.patches.DollEndTurnPatch;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class CutStrings extends AbstractDollTargetCard {
    private final static CardInfo cardInfo = new CardInfo(
            "CutStrings",
            1,
            CardType.SKILL,
            CardTargetEnums.DOLL,
            CardRarity.UNCOMMON);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int MAGIC = 2;
    private static final int UPG_MAGIC = 1;

    public CutStrings() {
        super(cardInfo, false);

        setMagic(MAGIC, UPG_MAGIC);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDoll d = getTargetDoll();

        if (d != null)
        {
            for (int i = 0; i < this.magicNumber; ++i)
            {
                addToBot(new TriggerDollPassiveAction(d));
            }
            addToBot(new DestroyDollAction(d));
        }
    }

    public AbstractCard makeCopy() {
        return new CutStrings();
    }
}