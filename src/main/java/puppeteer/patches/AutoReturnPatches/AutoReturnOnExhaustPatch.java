package puppeteer.patches.AutoReturnPatches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import javassist.CtBehavior;
import puppeteer.effects.FakeExhaustCardEffect;
import puppeteer.fields.AutoReturnField;
import puppeteer.interfaces.OnReturnToHandCard;

@SpirePatch(
        clz = CardGroup.class,
        method = "moveToExhaustPile"
)
public class AutoReturnOnExhaustPatch {
    @SpirePrefixPatch
    public static SpireReturn youNoExhaust(CardGroup __instance, AbstractCard c)
    {
        if (__instance.equals(AbstractDungeon.player.hand) && AutoReturnField.autoReturn.get(c))
        {
            if (!__instance.contains(c))
            {
                __instance.addToTop(c);
            }
            c.flash();

            if (c instanceof OnReturnToHandCard)
            {
                ((OnReturnToHandCard) c).onReturnToHand();
            }

            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}
