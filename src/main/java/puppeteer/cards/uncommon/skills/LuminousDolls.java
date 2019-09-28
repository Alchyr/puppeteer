package puppeteer.cards.uncommon.skills;

import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.BaseCard;
import puppeteer.actions.character.IncreaseAllChargeAction;
import puppeteer.actions.character.LuminousDollsAction;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class LuminousDolls extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "SaturatedSpirit",
            1,
            CardType.SKILL,
            CardTarget.ALL_ENEMY,
            CardRarity.UNCOMMON
    );

    public final static String ID = makeID(cardInfo.cardName);

    private final static int DEBUFF = 1;
    private final static int UPG_COST = 0;

    public LuminousDolls()
    {
        super(cardInfo, false);

        setCostUpgrade(UPG_COST);
        setMagic(DEBUFF);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new SFXAction("SINGING_BOWL"));
        AbstractDungeon.actionManager.addToBottom(new LuminousDollsAction(this.magicNumber));
    }
}