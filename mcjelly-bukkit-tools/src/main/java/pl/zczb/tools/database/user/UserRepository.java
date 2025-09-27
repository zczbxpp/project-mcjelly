package pl.zczb.tools.database.user;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import pl.zczb.Tools;
import pl.zczb.tools.database.user.models.UserDataModel;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;


public class UserRepository {
    private final MongoCollection<UserDataModel> collection;

    public UserRepository(MongoDatabase database) {
        this.collection = database.getCollection(Tools.getSectorConfig().getCurrentSector().getSectorType().toString().toLowerCase() + "_platform", UserDataModel.class);
        this.collection.createIndex(Indexes.ascending("warta"));
    }

    public Iterator<UserDataModel> fetchUsers() {
        return collection.find()
                .iterator();
    }

    public void update(Collection<UserDataModel> dataModel) {
        collection.bulkWrite(dataModel.stream().map(
                        d -> new ReplaceOneModel<>(Filters.eq("_id", d.getUuid()), d, new ReplaceOptions().upsert(true)))
                .collect(Collectors.toList()));
    }

    public long countPlayersWithWarta() {
        return collection.countDocuments(Filters.eq("warta", true));
    }

}