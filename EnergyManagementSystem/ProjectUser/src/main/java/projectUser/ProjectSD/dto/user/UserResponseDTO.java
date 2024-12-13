package projectUser.ProjectSD.dto.user;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a Data Transfer Object (DTO) for a user response.
 * This DTO encapsulates the id, name, age, email and role of the user.
 */
public class UserResponseDTO implements Serializable {
    private UUID id;
    private String name;
    private Integer age;
    private String email;
    private RoleDTO role;

    public UserResponseDTO() {
    }

    public UserResponseDTO(UUID id, String name, Integer age, String email, RoleDTO role) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponseDTO userResponseDTO = (UserResponseDTO) o;
        return Objects.equals(age, userResponseDTO.age) &&
                Objects.equals(name, userResponseDTO.name) &&
                Objects.equals(email, userResponseDTO.email) &&
                Objects.equals(role, userResponseDTO.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, email, role);
    }
}
