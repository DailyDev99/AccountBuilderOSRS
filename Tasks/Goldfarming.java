package Tasks;

import org.lostclient.api.accessors.Skills;
import org.lostclient.api.wrappers.skill.Skill;

public class Goldfarming implements ITask {

    @Override
    public boolean isValid() {
        return Skills.getRealLevel(Skill.WOODCUTTING) < 15;
    }

    @Override
    public Skill getSkill() {
        return Skill.FARMING;
    }

    @Override
    public int onStart() {
        return 1000;
    }

    @Override
    public int onLoop() {
        return 1000;
    }

    @Override
    public void onStop() {
    }

}
