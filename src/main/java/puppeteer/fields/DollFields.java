package puppeteer.fields;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import puppeteer.abstracts.AbstractDoll;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = SpirePatch.CLASS
)
public class DollFields {
    public static SpireField<ArrayList<AbstractDoll>> dolls = new SpireField<>(ArrayList::new);
    public static SpireField<AbstractDoll> targetDoll = new SpireField<>(()->null);
    public static SpireField<Integer> maxDolls = new SpireField<>(()->10);

    //max dolls should be set to 10 at start of each combat.
    //things (relics) that modify max doll amount should modify at start of combat.
}
