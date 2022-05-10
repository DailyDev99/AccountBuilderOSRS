package Tasks;

import org.lostclient.api.wrappers.skill.Skill;

public interface ITask {
    public Skill getSkill();

    public boolean isValid();

    public int onStart();
    public int onLoop(); // -1 calls onStop
    public void onStop();
}
