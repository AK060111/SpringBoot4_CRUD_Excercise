package vn.edu.ute.admin.dto;
import jakarta.validation.constraints.*;
import vn.edu.ute.admin.entity.Role;
public class UserForm {
    @NotBlank(message="Username bắt buộc") @Size(max=50) private String username;
    @NotBlank(message="Email bắt buộc") @Email(message="Email không hợp lệ") @Size(max=254) private String email;
    @NotBlank(message="Họ tên bắt buộc") @Size(max=100) private String fullname;
    @Size(max=72, message="Mật khẩu tối đa 72 ký tự") private String password="";
    @NotNull(message="Chọn vai trò hợp lệ") private Role role;
    public String getUsername() {
        return username;
    }
    public void setUsername(String v) {
        username=v==null?null:v.strip();
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String v) {
        email=v==null?null:v.strip();
    }
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String v) {
        fullname=v==null?null:v.strip();
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String v) {
        password=v;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role v) {
        role=v;
    }
}
