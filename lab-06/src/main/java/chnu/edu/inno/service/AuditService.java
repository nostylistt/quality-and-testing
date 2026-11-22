package chnu.edu.inno.service;

import chnu.edu.inno.model.Item;
import lombok.AllArgsConstructor;
import org.javers.core.Changes;
import org.javers.core.Javers;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuditService {

    private final Javers javers;

    public String getItemsChanges() {
        Changes changes = javers.findChanges(QueryBuilder.byClass(Item.class).build());
        return javers.getJsonConverter().toJson(changes);
    }

    public String getItemsShadows() {
        List<Shadow<Item>> shadows = javers.findShadows(QueryBuilder.byClass(Item.class).build());
        return javers.getJsonConverter().toJson(shadows);
    }

    public String getItemShadows(String id) {
        List<Shadow<Item>> shadows = javers.findShadows(
                QueryBuilder.byInstanceId(id, Item.class).withNewObjectChanges().build());
        return javers.getJsonConverter().toJson(shadows);
    }

    public String getItemChanges(String id) {
        return javers.getJsonConverter().toJson(findItemChanges(id));
    }

    public String getItemChangesPretty(String id) {
        return findItemChanges(id).prettyPrint();
    }

    private Changes findItemChanges(String id) {
        return javers.findChanges(QueryBuilder.byInstanceId(id, Item.class).withNewObjectChanges().build());
    }
}
