package puppeteer.cards.uncommon.skills;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.BaseCard;
import puppeteer.actions.character.IncreaseAllChargeAction;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class SaturatedSpirit extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "SaturatedSpirit",
            1,
            CardType.SKILL,
            CardTarget.NONE,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private final static int BUFF = 2;
    private final static int UPG_BUFF = 1;

    public SaturatedSpirit()
    {
        super(cardInfo, false);

        setMagic(BUFF, UPG_BUFF);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new IncreaseAllChargeAction(this.magicNumber));
    }
}