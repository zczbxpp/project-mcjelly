package pl.zczb.cashblock.database.zbyszek;

import com.mongodb.client.MongoDatabase;
import org.bukkit.plugin.Plugin;
import pl.zczb.Cashblock;
import pl.zczb.cashblock.database.zbyszek.models.ZbyszekDataModel;

import java.util.*;

public class ZbyszekHandler {
    private final Map<String, ZbyszekDataModel> dataModelMap = new HashMap<>();

    private static final long SAVE_TIME = 3600L;
    private final Plugin parentPlugin;
    private final ZbyszekRepository repository;
    private boolean initialized;

    public ZbyszekHandler(Cashblock plugin, MongoDatabase database) {
        this.parentPlugin = (Plugin) plugin;
        this.repository = new ZbyszekRepository(database);
    }


    public void initialize() {
        if (this.initialized) {
            throw new IllegalStateException("ZbyszekHandler already initialized!");
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


    public void updateUser(ZbyszekDataModel dataModel) {
        this.repository.update(Collections.singleton(dataModel));
    }

    private void loadData() {
        System.out.println("Rozpoczynam ładowanie danych...");

        Iterator<ZbyszekDataModel> dataModelIterator = this.repository.fetchUsers();
        int count = 0;

        while (dataModelIterator.hasNext()) {
            ZbyszekDataModel dataModel = dataModelIterator.next();
            this.dataModelMap.put(dataModel.getName(), dataModel);
            count++;
        }

        System.out.println("Załadowano " + count + " użytkowników.");
    }

    public ZbyszekDataModel createUser(String name) {
        if (this.dataModelMap.containsKey(name)) {
            return this.dataModelMap.get(name);
        }

        ZbyszekDataModel dataModel = new ZbyszekDataModel(name, 0);
        this.dataModelMap.put(name, dataModel);
        return dataModel;
    }


    public ZbyszekDataModel getZbyszek(String name) {
        ZbyszekDataModel dataModel = this.dataModelMap.get(name);
        if (dataModel != null) {
            return dataModel;
        }
        return null;
    }


    public List<ZbyszekDataModel> getUsers() {
        return new ArrayList<>(this.dataModelMap.values());
    }
}


