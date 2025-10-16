package pe.edu.pizzanajani.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class User {

    private Integer userId;
    private String username;
    private String email;
    private String passwordHash;
    private String fullName;
    private String phone;
    private Boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp lastLogin;

    private List<Role> roles;

    public User() {
        this.roles = new ArrayList<>();
    }

    public User(Integer userId, String username, String fullName) {
        this();
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
    }

    public User(Integer userId, String username, String email, String passwordHash,
            String fullName, String phone, Boolean isActive,
            Timestamp createdAt, Timestamp updatedAt, Timestamp lastLogin) {
        this();
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.phone = phone;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLogin = lastLogin;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public boolean hasRole(String roleName) {
        return roles.stream()
                .anyMatch(role -> role.getRoleName().equals(roleName));
    }

    @Override
    public String toString() {
        return "User{"
                + "userId=" + userId
                + ", username='" + username + '\''
                + ", email='" + email + '\''
                + ", fullName='" + fullName + '\''
                + ", isActive=" + isActive
                + ", roles=" + roles.size()
                + '}';
    }
}
