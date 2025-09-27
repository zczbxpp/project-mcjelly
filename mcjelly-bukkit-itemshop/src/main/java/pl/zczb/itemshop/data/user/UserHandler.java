package pl.zczb.itemshop.data.user;

import com.mongodb.client.MongoDatabase;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import pl.zczb.Itemshop;
import pl.zczb.itemshop.data.user.models.UserDataModel;

import java.util.*;

public class UserHandler {
    private final Map<UUID, UserDataModel> dataModelMap = new HashMap<>();

    private static final long SAVE_TIME = 3600L;
    private final Plugin parentPlugin;
    private final UserRepository repository;
    private boolean initialized;

    public UserHandler(Itemshop plugin, MongoDatabase database) {
        this.parentPlugin = (Plugin) plugin;
        this.repository = new UserRepository(database);
    }


    public void initialize() {
        if (this.initialized) {
            throw new IllegalStateException("UserHandler already initialized!");
        }

        loadData();
        this.parentPlugin.getServer().getScheduler()
                .runTaskTimerAsynchronously(this.parentPlugin, this::update, 3600L, 3600L);


        this.initialized = true;
    }

    public void update() {
        if (!this.dataModelMap.isEmpty()) {
            this.repository.update(this.dataModelMap.values());
        }
    }


    public void updateUser(UserDataModel dataModel) {
        this.repository.update(Collections.singleton(dataModel));
    }

    private void loadData() {
        System.out.println("Rozpoczynam ładowanie danych...");

        Iterator<UserDataModel> dataModelIterator = this.repository.fetchUsers();
        int count = 0;

        while (dataModelIterator.hasNext()) {
            UserDataModel dataModel = dataModelIterator.next();
            this.dataModelMap.put(UUID.fromString(dataModel.getUuid()), dataModel);
            count++;
        }

        System.out.println("Załadowano " + count + " użytkowników.");
    }

    public UserDataModel createUser(Player player) {
        UserDataModel dataModel = new UserDataModel(player.getUniqueId().toString(), player.getName(), 0.0D);
        this.dataModelMap.put(UUID.fromString(dataModel.getUuid()), dataModel);
        return dataModel;
    }

    public UserDataModel cacheUser(UUID uuid, UserDataModel u) {
        this.dataModelMap.put(uuid, u);

        return u;
    }


    public UserDataModel getPlayer(Player player) {
        UserDataModel dataModel = this.dataModelMap.get(player.getUniqueId());
        if (dataModel != null) {
            return dataModel;
        }
        return null;
    }

    public UserDataModel getPlayer(UUID uuid) {
        return this.dataModelMap.get(uuid);
    }

    public UserDataModel getPlayerByName(String name) {
        for (UserDataModel dataModel : this.dataModelMap.values()) {
            if (dataModel.getNick().equalsIgnoreCase(name)) {
                return dataModel;
            }
        }
        return null;
    }

    public UserDataModel getPlayer(String name) {
        for (UserDataModel dataModel : this.dataModelMap.values()) {
            if (dataModel.getNick().equalsIgnoreCase(name)) {
                return dataModel;
            }
        }
        return null;
    }

    public List<UserDataModel> getUsers() {
        return new ArrayList<>(this.dataModelMap.values());
    }
}


