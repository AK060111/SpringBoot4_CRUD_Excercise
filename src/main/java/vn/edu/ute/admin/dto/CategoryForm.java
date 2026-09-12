package vn.edu.ute.admin.dto;
import jakarta.validation.constraints.*;
public class CategoryForm {
    @NotBlank(message="Tên danh mục không được trống") @Size(max=100, message="Tên tối đa 100 ký tự")  private String name;
    public String getName() {
        return name;
    }
    public void setName(String value) {
        name=value==null?null:value.strip();
    }
}
