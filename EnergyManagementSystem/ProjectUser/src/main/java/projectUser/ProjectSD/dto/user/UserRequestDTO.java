package projectUser.ProjectSD.dto.user;

import projectUser.ProjectSD.dto.validator.annotation.AgeLimit;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a Data Transfer Object (DTO) for a user request.
 * This DTO encapsulates the name, age, email and password of the user.
 */
public class UserRequestDTO implements Serializable {
    @NotNull(message = "The name of an user cannot be missing.")
    private String name;

    @AgeLimit(limit = 18)
    private Integer age;

    @Pattern(regexp = "^[A-Za-z][A-Za-z0-9._-]*@[A-Za-z0-9]+\\.[A-Za-z]+$", message = "Invalid email format.")
    private String email;

    @NotNull(message = "The password of an user cannot be missing.")
    private String password;

    public UserRequestDTO() {
    }

    public UserRequestDTO(String name, Integer age, String email, String password) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRequestDTO userRequestDTO = (UserRequestDTO) o;
        return Objects.equals(age, userRequestDTO.age) &&
                Objects.equals(name, userRequestDTO.name) &&
                Objects.equals(email, userRequestDTO.email) &&
                Objects.equals(password, userRequestDTO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, email, password);
    }
}
