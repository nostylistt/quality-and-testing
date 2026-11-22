package chnu.edu.inno.controller;

import chnu.edu.inno.model.dto.request.ItemRequest;
import chnu.edu.inno.model.dto.response.ApiResponse;
import chnu.edu.inno.model.dto.response.BaseMetaData;
import chnu.edu.inno.model.dto.response.ItemResponse;
import chnu.edu.inno.model.dto.response.PaginationMetaData;
import chnu.edu.inno.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{id}")
    @Operation(summary = "Get item by id", description = "200 if found, 404 if item does not exist")
    public ResponseEntity<ApiResponse<BaseMetaData, ItemResponse>> getItemById(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ofItem(HttpStatus.OK, itemService.getItemById(id)));
    }

    @GetMapping
    @Operation(summary = "Get page of items, sorted by id ASC, 5 items per page by default",
            description = "200 with page of items, 400 if page or size is invalid")
    public ResponseEntity<ApiResponse<PaginationMetaData, ItemResponse>> getItemsPage(
            @Parameter(description = "Page number, starts from 0")
            @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "Page size, from 1 to " + ItemService.MAX_PAGE_SIZE)
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(itemService.getPage(page, size));
    }

    @PostMapping
    @Operation(summary = "Create item", description = "201 if created, 400 if request is invalid")
    public ResponseEntity<ApiResponse<BaseMetaData, ItemResponse>> createItem(@RequestBody ItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ofItem(HttpStatus.CREATED, itemService.createItem(request)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update item", description = "200 if updated, 400 if request is invalid, 404 if item does not exist")
    public ResponseEntity<ApiResponse<BaseMetaData, ItemResponse>> updateItem(
            @PathVariable String id, @RequestBody ItemRequest request) {
        return ResponseEntity.ok(ApiResponse.ofItem(HttpStatus.OK, itemService.updateItem(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete item", description = "200 with deleted item, 404 if item does not exist")
    public ResponseEntity<ApiResponse<BaseMetaData, ItemResponse>> deleteItem(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ofItem(HttpStatus.OK, itemService.deleteItemById(id)));
    }
}
