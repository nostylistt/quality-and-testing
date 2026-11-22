package chnu.edu.inno.model;


/*
  @author   george
  @project   inno
  @class  Item
  @version  1.0.0
  @since 27/09/2026 - 18.59
*/

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Item extends AuditMetadata {
    @Id
    private String id;
    private String name;
    private String code;
    private  int quantity;

    public Item(String name, String code, int quantity) {
        this.name = name;
        this.code = code;
        this.quantity = quantity;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;

        return id != null && id.equals(item.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
