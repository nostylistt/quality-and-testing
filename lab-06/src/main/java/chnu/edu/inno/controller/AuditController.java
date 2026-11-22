package chnu.edu.inno.controller;

import chnu.edu.inno.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/audit/items")
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/changes")
    @Operation(summary = "All changes of all items")
    public ResponseEntity<String> getItemsChanges() {
        return json(auditService.getItemsChanges());
    }

    @GetMapping("/shadows")
    @Operation(summary = "All shadows of all items")
    public ResponseEntity<String> getItemsShadows() {
        return json(auditService.getItemsShadows());
    }

    @GetMapping("/{id}/shadows")
    @Operation(summary = "History of one item: initial, update and terminal shadows")
    public ResponseEntity<String> getItemShadows(@PathVariable String id) {
        return json(auditService.getItemShadows(id));
    }

    @GetMapping("/{id}/changes")
    @Operation(summary = "Changes of one item as JSON")
    public ResponseEntity<String> getItemChanges(@PathVariable String id) {
        return json(auditService.getItemChanges(id));
    }

    @GetMapping("/{id}/changes/pretty")
    @Operation(summary = "Changes of one item as readable text")
    public ResponseEntity<String> getItemChangesPretty(@PathVariable String id) {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body("<pre>" + auditService.getItemChangesPretty(id) + "</pre>");
    }

    private ResponseEntity<String> json(String body) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
    }
}
