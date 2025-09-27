package pl.zczb.tools.spigot.events.antyafk;

public class PlayerAfkState {
    public long lastMouseMoveTime;
    public long lastBlockMoveTime;
    public int warningCountdown = -1;

    public PlayerAfkState() {
        long now = System.currentTimeMillis();
        this.lastMouseMoveTime = now;
        this.lastBlockMoveTime = now;
    }
}


