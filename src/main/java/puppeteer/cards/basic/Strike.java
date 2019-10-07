package puppeteer.cards.basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import puppeteer.abstracts.BaseCard;
import puppeteer.util.CardInfo;

import static basemod.helpers.BaseModCardTags.BASIC_STRIKE;
import static puppeteer.PuppeteerMod.makeID;

public class Strike extends BaseCard {
    private final static CardInfo cardInfo = new CardInfo(
            "Strike",
            1,
            CardType.ATTACK,
            CardTarget.ENEMY,
            CardRarity.BASIC
    );

    public final static String ID = makeID(cardInfo.cardName);

    private final static int DAMAGE = 6;
    private final static int UPG_DAMAGE = 3;

    public Strike()
    {
        super(cardInfo, false);

        setDamage(DAMAGE, UPG_DAMAGE);

        tags.add(CardTags.STRIKE);
        tags.add(BASIC_STRIKE);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        damageSingle(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
    }
}