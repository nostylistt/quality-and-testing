package chnu.edu.inno.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ApiResponse<M extends BaseMetaData, D> {
    private M meta;
    private List<D> data;

    public ApiResponse(M meta, D data) {
        this.meta = meta;
        this.data = new ArrayList<>();
        this.data.add(data);
    }

    public ApiResponse(M meta) {
        this.meta = meta;
    }

    public static <D> ApiResponse<BaseMetaData, D> ofList(HttpStatus status, List<D> data) {
        ApiResponse<BaseMetaData, D> response = new ApiResponse<>(new BaseMetaData(status.value(), true, null));
        response.setData(data);
        return response;
    }

    public static <D> ApiResponse<BaseMetaData, D> ofItem(HttpStatus status, D item) {
        return ofList(status, List.of(item));
    }

    public static <D> ApiResponse<BaseMetaData, D> error(HttpStatus status, String message) {
        ApiResponse<BaseMetaData, D> response = new ApiResponse<>(new BaseMetaData(status.value(), false, message));
        response.setData(new ArrayList<>());
        return response;
    }
}
