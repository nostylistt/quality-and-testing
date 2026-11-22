package chnu.edu.inno.service;

import chnu.edu.inno.exception.InvalidRequestException;
import chnu.edu.inno.exception.ItemNotFoundException;
import chnu.edu.inno.mapper.ItemMapper;
import chnu.edu.inno.model.Item;
import chnu.edu.inno.model.dto.request.ItemRequest;
import chnu.edu.inno.model.dto.response.ApiResponse;
import chnu.edu.inno.model.dto.response.ItemResponse;
import chnu.edu.inno.model.dto.response.PaginationMetaData;
import chnu.edu.inno.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemService {

    public static final int MAX_PAGE_SIZE = 100;

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @PostConstruct
    public void init() {
        itemRepository.deleteAll();
        List<Item> items = List.of(
                new Item("Milk", "000001", 1),
                new Item("Bread", "00002", 2),
                new Item("Sugar", "00003", 3),
                new Item("Salt", "00004", 1),
                new Item("Meat", "00005", 1),
                new Item("Chips", "00006", 3),
                new Item("Coca-Cola", "00007", 1),
                new Item("Hot dog", "00008", 1)
        );
        itemRepository.saveAll(items);
    }

    public ItemResponse getItemById(String id) {
        return itemMapper.toResponse(findItem(id));
    }

    public ApiResponse<PaginationMetaData, ItemResponse> getPage(int page, int size) {
        if (page < 0) {
            throw new InvalidRequestException("Page number cannot be negative");
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new InvalidRequestException("Page size must be between 1 and " + MAX_PAGE_SIZE);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<Item> items = itemRepository.findAll(pageable);

        PaginationMetaData metaData = new PaginationMetaData();
        metaData.setCode(HttpStatus.OK.value());
        metaData.setSuccess(true);
        metaData.setNumber(items.getNumber());
        metaData.setSize(items.getSize());
        metaData.setTotalElements(items.getTotalElements());
        metaData.setTotalPages(items.getTotalPages());
        metaData.setFirst(items.isFirst());
        metaData.setLast(items.isLast());

        ApiResponse<PaginationMetaData, ItemResponse> response = new ApiResponse<>(metaData);
        response.setData(items.getContent().stream().map(itemMapper::toResponse).toList());
        return response;
    }

    public ItemResponse createItem(ItemRequest request) {
        validate(request);
        Item saved = itemRepository.save(itemMapper.toEntity(request));
        return itemMapper.toResponse(saved);
    }

    public ItemResponse updateItem(String id, ItemRequest request) {
        validate(request);
        Item item = findItem(id);
        itemMapper.updateEntity(item, request);
        return itemMapper.toResponse(itemRepository.save(item));
    }

    public ItemResponse deleteItemById(String id) {
        Item item = findItem(id);
        itemRepository.delete(item);
        return itemMapper.toResponse(item);
    }

    private Item findItem(String id) {
        return itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
    }

    private void validate(ItemRequest request) {
        if (request == null) {
            throw new InvalidRequestException("Request body is missing");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new InvalidRequestException("Field 'name' is required");
        }
        if (request.getCode() == null || request.getCode().isBlank()) {
            throw new InvalidRequestException("Field 'code' is required");
        }
        if (request.getQuantity() == null) {
            throw new InvalidRequestException("Field 'quantity' is required");
        }
        if (request.getQuantity() < 1) {
            throw new InvalidRequestException("Field 'quantity' must be greater than 0");
        }
    }
}
