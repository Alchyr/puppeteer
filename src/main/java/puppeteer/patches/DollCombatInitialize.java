package puppeteer.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import puppeteer.fields.DollFields;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "preBattlePrep"
)
public class DollCombatInitialize {
    @SpirePrefixPatch
    public static void initializeDollInfo(AbstractPlayer __instance)
    {
        DollFields.maxDolls.set(__instance, 10);
        //put any doll max modifying effects here
        DollFields.dolls.get(__instance).clear();
        DollFields.targetDoll.set(__instance, null);
        DollFields.dollsThisCombat.set(__instance, 0);
    }
}
