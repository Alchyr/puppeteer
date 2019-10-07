package puppeteer.cards.rare.attacks;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.BaseCard;
import puppeteer.actions.generic.DamageClosestEnemyAction;
import puppeteer.fields.DollFields;
import puppeteer.util.CardInfo;

import static puppeteer.PuppeteerMod.makeID;

public class LemmingsParade extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "LemmingsParade",
            3,
            CardType.ATTACK,
            CardTarget.ALL_ENEMY,
            CardRarity.RARE);

    public final static String ID = makeID(cardInfo.cardName);

    private static final int DAMAGE = 7;
    private static final int UPG_DAMAGE = 3;

    public LemmingsParade() {
        super(cardInfo, false);

        setDamage(DAMAGE, UPG_DAMAGE);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        //TODO: Add a fancy effect where dolls appear, move to closest enemy, and explode.
        for (int i = 0; i < DollFields.dollsThisCombat.get(p); ++i)
        {
            addToBot(new DamageClosestEnemyAction(p, this));
        }
    }

    public AbstractCard makeCopy() {
        return new LemmingsParade();
    }
}