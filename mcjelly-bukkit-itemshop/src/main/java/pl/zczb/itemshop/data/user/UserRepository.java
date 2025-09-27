package pl.zczb.itemshop.data.user;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import pl.zczb.itemshop.data.user.models.UserDataModel;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


public class UserRepository {
    private final MongoCollection<UserDataModel> collection;

    public UserRepository(MongoDatabase database) {
        this.collection = database.getCollection("itemshop_users", UserDataModel.class);
    }


    public Iterator<UserDataModel> fetchUsers() {
        return (Iterator<UserDataModel>) this.collection.find()
                .iterator();
    }

    public void update(Collection<UserDataModel> dataModel) {
        this.collection.bulkWrite((List) dataModel.stream().map(d -> new ReplaceOneModel(Filters.eq("_id", d.getUuid()), d, (new ReplaceOptions()).upsert(true)))

                .collect(Collectors.toList()));
    }
}


