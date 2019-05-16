package cz.muni.fi.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by jsmadis
 * @author jsmadis
 */

public class UserUpdatePwdDTO {
    private Long id;
    @NotNull
    @Size(min = 5, max = 150)
    private String currentPassword;
    @NotNull
    @Size(min = 5, max = 150)
    private String newPassword;

    public UserUpdatePwdDTO(Long id, String currentPassword, String newPassword) {
        this.id = id;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
