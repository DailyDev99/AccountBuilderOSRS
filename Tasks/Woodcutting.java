package Tasks;

import org.lostclient.api.accessors.GameObjects;
import org.lostclient.api.accessors.Players;
import org.lostclient.api.containers.Inventory;
import org.lostclient.api.utilities.MethodProvider;
import org.lostclient.api.wrappers.interactives.GameObject;
import org.lostclient.api.wrappers.skill.Skill;
import org.lostclient.api.wrappers.walking.Walking;


public class Woodcutting implements ITask {
    GameObject tree = GameObjects.closest("Tree");

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public Skill getSkill() {
        return Skill.WOODCUTTING;
    }

    @Override
    public int onStart() {
        MethodProvider.log("woodcutting on start");

        return 1000;
    }

    @Override
    public int onLoop() {
        MethodProvider.log("woodcutting on loop");

        if (Inventory.isFull())
            Inventory.dropAll(item -> item != null && item.hasName("Logs"));

        while(Players.localPlayer().isAnimating())
            MethodProvider.sleep(1000);


        if(tree == null) {
            MethodProvider.log("cannot find any applicable tree to murder");
            MethodProvider.sleepUntil(() -> tree != null, 5000);
            return -1;
        }

        if(!tree.canReach()) {
            Walking.walk(tree.getTile());

            while(Players.localPlayer().isAnimating())
                MethodProvider.sleep(1000);
        }

        if(tree.exists() && tree.canReach()){
            if(tree.interact("Chop down")){
                while(Players.localPlayer().isAnimating())
                    MethodProvider.sleep(1000);
                MethodProvider.log("finished murdering tree");
            }
        }

        return 1000;
    }

    @Override
    public void onStop() {
        Inventory.dropAll(item -> item != null && item.hasName("Logs"));
    }
}
