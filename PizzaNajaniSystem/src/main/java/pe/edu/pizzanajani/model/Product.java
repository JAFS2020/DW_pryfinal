package pe.edu.pizzanajani.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Product {

    private Integer productId;
    private Integer categoryId;
    private String productCode;
    private String productName;
    private String description;
    private BigDecimal price;
    private BigDecimal cost;
    private String imageUrl;
    private Boolean isAvailable;
    private Integer preparationTime;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private String categoryName;

    public Product() {
    }

    public Product(Integer productId, String productName, BigDecimal price) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
    }

    public Product(Integer productId, Integer categoryId, String productCode,
            String productName, String description, BigDecimal price,
            BigDecimal cost, String imageUrl, Boolean isAvailable,
            Integer preparationTime, Timestamp createdAt, Timestamp updatedAt) {
        this.productId = productId;
        this.categoryId = categoryId;
        this.productCode = productCode;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.cost = cost;
        this.imageUrl = imageUrl;
        this.isAvailable = isAvailable;
        this.preparationTime = preparationTime;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Integer getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(Integer preparationTime) {
        this.preparationTime = preparationTime;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "Product{"
                + "productId=" + productId
                + ", productCode='" + productCode + '\''
                + ", productName='" + productName + '\''
                + ", price=" + price
                + ", isAvailable=" + isAvailable
                + '}';
    }
}
