package projectUser.ProjectSD.dto.auth;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a Data Transfer Object (DTO) for a login request.
 * This DTO encapsulates the user's email and password for authentication.
 */
public class LoginRequestDTO implements Serializable {
    private String email;
    private String password;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
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
        LoginRequestDTO loginRequestDTO = (LoginRequestDTO) o;
        return Objects.equals(email, loginRequestDTO.email) &&
                Objects.equals(password, loginRequestDTO.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }
}
