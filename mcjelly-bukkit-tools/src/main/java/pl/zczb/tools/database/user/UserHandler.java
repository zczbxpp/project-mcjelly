package pl.zczb.tools.database.user;


import com.mongodb.client.MongoDatabase;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import pl.zczb.Tools;
import pl.zczb.tools.database.user.models.*;

import java.util.*;

public class UserHandler {

    private static final long SAVE_TIME = 20L * 60L * 3L;

    private final Map<UUID, UserDataModel> dataModelMap = new HashMap<>();

    private final Plugin parentPlugin;
    private final UserRepository repository;

    private boolean initialized;

    public UserHandler(Tools plugin, MongoDatabase database) {
        this.parentPlugin = plugin;
        this.repository = new UserRepository(
                database
        );
    }

    public void initialize() {
        if (initialized) {
            throw new IllegalStateException("UserHandler already initialized!");
        }

        this.loadData();
        this.parentPlugin.getServer().getScheduler()
                .runTaskTimerAsynchronously(parentPlugin, this::update,
                        SAVE_TIME, SAVE_TIME);

        initialized = true;
    }

    public void update() {
        if (!dataModelMap.isEmpty()) {
            repository.update(dataModelMap.values());
        }

//        for (UserDataModel dataModel : dataModelMap.values()) {
//            dataModel.update();
//        }
    }

    public void updateUser(UserDataModel dataModel) {
        repository.update(Collections.singleton(dataModel));
    }

    private void loadData() {
        System.out.println("Rozpoczynam ładowanie danych...");

        Iterator<UserDataModel> dataModelIterator = repository.fetchUsers();
        int count = 0;

        while (dataModelIterator.hasNext()) {
            UserDataModel dataModel = dataModelIterator.next();
            dataModelMap.put(UUID.fromString(dataModel.getUuid()), dataModel);
            count++;
        }

        System.out.println("Załadowano " + count + " użytkowników.");
    }

    public UserDataModel createUser(Player player) {
        UUID uuid = player.getUniqueId();
        if (dataModelMap.containsKey(uuid)) {
            return dataModelMap.get(uuid);
        }

        UserDataModel dataModel = new UserDataModel(uuid.toString(), player.getName(), UserSynchro.createDefault(), false, 0, 0, false, false, 0, UserKits.createDefault(), UserBans.createDefault(), UserEnderchests.createDefault());
        dataModelMap.put(uuid, dataModel);
        return dataModel;
    }


    public UserDataModel cacheUser(UUID uuid, UserDataModel u) {
        if (uuid == null || u == null || u.getUuid() == null) {
            throw new IllegalArgumentException("Nieprawidłowy użytkownik do cache'owania");
        }

        dataModelMap.put(uuid, u);
        return u;
    }


    public UserDataModel getPlayer(Player player) {
        UserDataModel dataModel = dataModelMap.get(player.getUniqueId());
        if (dataModel != null) {
            return dataModel;
        }
        return null;
    }

    public Collection<UserDataModel> getUsersByIp(String ip) {
        List<UserDataModel> foundUsers = new ArrayList<>();
        for (UserDataModel dataModel : this.dataModelMap.values()) {
            UserBans bans = dataModel.getUserBans();
            if (bans != null && ip.equals(bans.getIpAddress()))
                foundUsers.add(dataModel);
        }
        return foundUsers;
    }
    public UserDataModel getPlayerByName(String name) {
        for (UserDataModel dataModel : this.dataModelMap.values()) {
            if (dataModel.getNick().equalsIgnoreCase(name))
                return dataModel;
        }
        return null;
    }

    public UserDataModel getPlayer(UUID uuid) {
        return dataModelMap.get(uuid);
    }

    public UserDataModel getPlayer(String name) {
        for (UserDataModel dataModel : dataModelMap.values()) {
            if (dataModel.getNick().equalsIgnoreCase(name)) {
                return dataModel;
            }
        }
        return null;
    }

    public List<UserDataModel> getUsers() {
        return new ArrayList<>(dataModelMap.values());
    }

    public long getCountPlayersWarta() {
        return repository.countPlayersWithWarta();
    }
}