package pl.zczb.packets.subs;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import pl.zczb.Cashblock;
import pl.zczb.Controller;
import pl.zczb.cashblock.database.zbyszek.models.ZbyszekDataModel;
import pl.zczb.cashblock.helpers.DataUtil;
import pl.zczb.helpers.GlobalHelper;
import pl.zczb.packets.TurboKasaPacket;
import pl.zczb.packets.ZbyszekPacket;
import pl.zczb.redis.subscriber.RedisSubscriber;


public final class ZbyszekSubscriber extends RedisSubscriber<ZbyszekPacket> {
    public ZbyszekSubscriber() {
        super(ZbyszekPacket.class, "CH|cashblock_tryb");
    }
    @Override
    public void onPacketReceived(ZbyszekPacket packet) {

        switch (packet.getType()) {
            case "add":
                ZbyszekDataModel zbyszek = Cashblock.getInstance().getZbyszekHandler().getZbyszek(packet.getName());
                zbyszek.addBalance(packet.getAmount());

                if (zbyszek.getName().equals("deszczkluczy")) {
                    if (zbyszek.getBalance() >= 500) {
                        zbyszek.removeBalance(500);
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendTitle(GlobalHelper.fixColor("&#45DAEF&lZ&#3ADACE&lB&#2EDAAD&lY&#23DB8C&lS&#17DB6B&lZ&#0CDB4A&lE&#00DB29&lK"), GlobalHelper.fixColor("&#45DAEFZrób &fmiejsce &#45DAEFw &#40DAE0e&#3DDAD8k&#3ADAD1w&#38DAC9i&#35DAC1p&#32DABAu&#30DAB2n&#2DDAAAk&#2ADAA3u &#25DA94r&#23DB8Co&#20DB84z&#1DDB7Dd&#1BDB75a&#18DB6Ej&#15DB66e &#10DB57k&#0DDB4Fl&#0BDB47u&#08DB40c&#05DB38z&#03DB31e&#00DB29!"), 10, 60, 10);
                        }
                        Bukkit.getScheduler().runTaskLater(Cashblock.getInstance(), () -> {
                            new BukkitRunnable() {
                                int countdown = 5;

                                @Override
                                public void run() {
                                    if (countdown == -1) {

                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "getcase giveall epicka 2");
                                        cancel();
                                        return;
                                    }
                                    for (Player p : Bukkit.getOnlinePlayers()) {
                                        p.sendTitle(GlobalHelper.fixColor("&#45DAEF&lZ&#3ADACE&lB&#2EDAAD&lY&#23DB8C&lS&#17DB6B&lZ&#0CDB4A&lE&#00DB29&lK"), GlobalHelper.fixColor("&#00DB29" + countdown), 10, 60, 10);
                                    }
                                    countdown--;
                                }
                            }.runTaskTimer(Cashblock.getInstance(), 0L, 20L);
                        }, 80L);


                    }

                } else {
                    if (zbyszek.getBalance() >= 250) {
                        zbyszek.removeBalance(250);
                        if (zbyszek.getName().equals("turbokasa")) {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.sendTitle(GlobalHelper.fixColor("&#45DAEF&lZ&#3ADACE&lB&#2EDAAD&lY&#23DB8C&lS&#17DB6B&lZ&#0CDB4A&lE&#00DB29&lK"), GlobalHelper.fixColor("&#45DAEFDaje wam &fTurboKase&b &#45DAEFk&#3FDADFo&#3ADACEp&#34DABEc&#2EDAADi&#28DA9De &#1DDB7Cb&#17DB6Bl&#11DB5Bo&#0CDB4Ak&#06DB3Ai&#00DB29!"), 10, 60, 10);
                            }
                            final long eventtime = DataUtil.parseDateDiff("30min", true);
                            Controller.getInstance().getRedis().publish("CH|cashblock_tryb", new TurboKasaPacket(eventtime, "Zbyszek hehe ;)"));
                        } else if (zbyszek.getName().equals("piniata")) {
                            if(!Cashblock.getCashblockConfig().getSector_name().equalsIgnoreCase("cashblock_event"))
                                return;
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                p.sendTitle(GlobalHelper.fixColor("&#45DAEF&lZ&#3ADACE&lB&#2EDAAD&lY&#23DB8C&lS&#17DB6B&lZ&#0CDB4A&lE&#00DB29&lK"), GlobalHelper.fixColor("&#45DAEFR&#43DAEAe&#41DAE4s&#3FDADFp&#3DDAD9i&#3BDAD4e &#38DAC9w&#36DAC3a&#34DABEm &#30DAB3z&#2EDAADa&#2CDAA8r&#2ADAA2a&#28DA9Dz &#24DA92p&#23DB8Ci&#21DB87n&#1FDB81i&#1DDB7Ca&#1BDB76t&#19DB71e &#15DB66n&#13DB60a &#0FDB55w&#0DDB50a&#0CDB4Ar&#0ADB45p &#06DB3Ap&#04DB34v&#02DB2Fp&#00DB29!"), 10, 60, 10);
                            }
                            Bukkit.getScheduler().runTaskLater(Cashblock.getInstance(), () -> {
                                new BukkitRunnable() {
                                    int countdown = 5;

                                    @Override
                                    public void run() {
                                        if (countdown == -1) {
                                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "boss PINIATA 500");
                                            cancel();
                                            return;
                                        }
                                        for (Player p : Bukkit.getOnlinePlayers()) {
                                            p.sendTitle(GlobalHelper.fixColor("&#45DAEF&lZ&#3ADACE&lB&#2EDAAD&lY&#23DB8C&lS&#17DB6B&lZ&#0CDB4A&lE&#00DB29&lK"), GlobalHelper.fixColor("&#00DB29" + countdown), 10, 60, 10);
                                        }
                                        countdown--;
                                    }
                                }.runTaskTimer(Cashblock.getInstance(), 0L, 20L);
                            }, 80L);


                        }

                    }
                }

                break;
        }
    }
}