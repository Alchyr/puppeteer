package puppeteer.patches.AutoReturnPatches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import puppeteer.effects.FakeExhaustCardEffect;
import puppeteer.fields.AutoReturnField;
import puppeteer.interfaces.OnReturnToHandCard;

public class AutoReturnOnMovePatches {
    @SpirePatch(
            clz = CardGroup.class,
            method = "moveToDeck"
    )
    @SpirePatch(
            clz = CardGroup.class,
            method = "moveToBottomOfDeck"
    )
    @SpirePatch(
            clz = CardGroup.class,
            method = "moveToDiscardPile"
    )
    @SpirePatch(
            clz = CardGroup.class,
            method = "moveToBottomOfDeck"
    )
    @SpirePatch(
            clz = CardGroup.class,
            method = "moveToBottomOfDeck"
    )
    public static class NoMove
    {
        @SpirePrefixPatch
        public static SpireReturn noMove(CardGroup __instance, AbstractCard c)
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
}
