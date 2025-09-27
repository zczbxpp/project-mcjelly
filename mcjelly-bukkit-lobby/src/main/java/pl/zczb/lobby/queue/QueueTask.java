package pl.zczb.lobby.queue;

public class QueueTask implements Runnable {
    private final QueueManager manager;

    public QueueTask(QueueManager manager) {
        this.manager = manager;
    }


    public void run() {
        this.manager.getQueue("cashblock").tryJoinServer();
        this.manager.getQueue("cashblock").updateBossBars();

        this.manager.getQueue("igrzyska").tryJoinServer();
        this.manager.getQueue("igrzyska").updateBossBars();

        this.manager.getQueue("boxpvp").tryJoinServer();
        this.manager.getQueue("boxpvp").updateBossBars();
    }
}


