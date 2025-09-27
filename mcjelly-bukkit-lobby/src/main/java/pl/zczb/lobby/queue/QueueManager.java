package pl.zczb.lobby.queue;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class QueueManager {
    private final Map<String, QueueInstance> queues = new HashMap<>();


    public QueueManager() {
        this.queues.put("cashblock", new QueueInstance("cashblock"));
        this.queues.put("igrzyska", new QueueInstance("igrzyska"));
        this.queues.put("boxpvp", new QueueInstance("boxpvp"));
    }

    public QueueInstance getQueue(String name) {
        return this.queues.get(name.toLowerCase());
    }

    public boolean isInAnyQueue(Player player) {
        return this.queues.values().stream().anyMatch(queue -> queue.isInQueue(player));
    }

    public void removeFromAll(Player player) {
        this.queues.values().forEach(queue -> queue.removePlayer(player));
    }
}


