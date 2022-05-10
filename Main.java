

import Tasks.*;
import org.lostclient.api.accessors.Skills;
import org.lostclient.api.script.AbstractScript;
import org.lostclient.api.script.ScriptManifest;
import org.lostclient.api.utilities.MethodProvider;

import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.ArrayList;

/*
    This is a script to generate tasks (x minutes and x levels in skill) for each skill in the game.
    I'm not using any kind of weight distribution because this is only a demo on how you can do it without making it too complicated.
    The code is from 2021 and wrote it in about 10 minutes, the project itself is actually over 10k lines,
    sold the whole project for $3k when I needed money.
 */
@ScriptManifest(name = "Goldfarm", author = "Arti", version = 1.0)
public class Main extends AbstractScript {
    public ArrayList<ITask> tasks = new ArrayList<ITask>(Arrays.asList(
        new Woodcutting(),
        new Goldfarming()
    ));

    public ITask task = null;

    public int taskGoal = 0;
    public long taskTime = 0;

    @Override
    public int onLoop() {
        if(task == null) {
            if(tasks.isEmpty()) {
                MethodProvider.log("no task remains that can be started");

                stop();

                return 0;
            }

            MethodProvider.log("current task is empty, starting a new one");

            task = getNewTask();

            int skill = Skills.getRealLevel(task.getSkill());

            if(skill == 99) {
                MethodProvider.log("current task's skill is maximum level, cannot be started'");

                tasks.remove(task);

                task = null;

                return 0;
            }

            if(!task.isValid()) {
                MethodProvider.log("current task isn't valid");

                task = null;

                return 0;
            }

            taskGoal = skill + new Random().nextInt(99 - skill);
            taskTime = new Date().getTime() + ((new Random().nextInt(150) + 30) * 60 * 1000);

            MethodProvider.log(String.format("starting task %s to go from level %d to %d", task.getClass().getName(), skill, taskGoal));

            int result = task.onStart();

            if(result == -1) {
                MethodProvider.log("task cannot be started, removing from list");

                tasks.remove(task);

                task = null;

                return 0;
            }
        }

        if(new Date().getTime() >= taskTime) {
            MethodProvider.log("task has been going on past the time limit, stopping");

            task.onStop();

            task = null;

            return 0;
        }

        int result = task.onLoop();

        if(result == -1) {
            MethodProvider.log("task has been completed");

            task.onStop();

            task = null;

            return 0;
        }

        if(Skills.getRealLevel(task.getSkill()) == taskGoal) {
            MethodProvider.log("task goal has been reached");

            task.onStop();

            task = null;

            return 0;
        }

        return result;
    }

    public ITask getNewTask() {
        int newTaskIndex = new Random().nextInt(tasks.size());

        return tasks.get(newTaskIndex);
    }
}
