package chnu.edu.inno.mapper;

import chnu.edu.inno.model.Item;
import chnu.edu.inno.model.dto.request.ItemRequest;
import chnu.edu.inno.model.dto.response.ItemResponse;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public ItemResponse toResponse(Item item) {
        return new ItemResponse(item.getId(), item.getName(), item.getCode(), item.getQuantity());
    }

    public Item toEntity(ItemRequest request) {
        return new Item(request.getName().trim(), request.getCode().trim(), request.getQuantity());
    }

    public void updateEntity(Item item, ItemRequest request) {
        item.setName(request.getName().trim());
        item.setCode(request.getCode().trim());
        item.setQuantity(request.getQuantity());
    }
}
