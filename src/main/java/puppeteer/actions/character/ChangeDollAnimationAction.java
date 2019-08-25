package puppeteer.actions.character;

import basemod.animations.SpineAnimation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import puppeteer.abstracts.AbstractDoll;

public class ChangeDollAnimationAction extends AbstractGameAction {
    private AbstractDoll toChange;
    private SpineAnimation changeTo;
    private String name;

    private SpineAnimation next = null;
    private String nextName = "";

    private boolean autoReset = true; //Should doll automatically go back to idle animation when done playing

    public ChangeDollAnimationAction(AbstractDoll toChange, SpineAnimation changeTo, String name)
    {
        this.toChange = toChange;
        this.changeTo = changeTo;
        this.name = name;
    }

    public ChangeDollAnimationAction(AbstractDoll toChange, SpineAnimation changeTo, String name, SpineAnimation nextAnimation, String nextName)
    {
        this.toChange = toChange;
        this.changeTo = changeTo;
        this.name = name;

        this.next = nextAnimation;
        this.nextName = nextName;
    }

    public ChangeDollAnimationAction(AbstractDoll toChange, SpineAnimation changeTo, String name, SpineAnimation nextAnimation, String nextName, boolean autoReset)
    {
        this(toChange, changeTo, name, nextAnimation, nextName);

        this.autoReset = autoReset;
    }

    @Override
    public void update() {
        toChange.changeAnimation(changeTo, name);

        if (this.next != null)
            toChange.setNextAnimation(next, nextName);

        toChange.autoReset = this.autoReset;

        this.isDone = true;
    }
}
