package puppeteer.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;
import puppeteer.actions.character.EndTurnDollsAction;

@SpirePatch(
        clz = GameActionManager.class,
        method = "callEndOfTurnActions"
)
public class DollEndTurnPatch {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void dollEndTurn(GameActionManager __instance)
    {
        AbstractDungeon.actionManager.addToBottom(new EndTurnDollsAction());
    }



    private static class Locator extends SpireInsertLocator {
        private Locator() {
        }

        public int[] Locate(CtBehavior ctBehavior) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "hand");
            return LineFinder.findInOrder(ctBehavior, finalMatcher);
        }
    }
}
