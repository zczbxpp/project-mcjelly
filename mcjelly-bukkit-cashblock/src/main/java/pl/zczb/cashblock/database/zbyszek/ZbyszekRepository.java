package pl.zczb.cashblock.database.zbyszek;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import pl.zczb.cashblock.database.zbyszek.models.ZbyszekDataModel;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

public class ZbyszekRepository {
    private final MongoCollection<ZbyszekDataModel> collection;

    public ZbyszekRepository(MongoDatabase database) {
        this.collection = database.getCollection("zbyszek", ZbyszekDataModel.class);

    }

    public Iterator<ZbyszekDataModel> fetchUsers() {
        return collection.find()
                .iterator();
    }

    public void update(Collection<ZbyszekDataModel> dataModel) {
        collection.bulkWrite(dataModel.stream().map(
                        d -> new ReplaceOneModel<>(Filters.eq("name", d.getName()), d, new ReplaceOptions().upsert(true)))
                .collect(Collectors.toList()));
    }
}
