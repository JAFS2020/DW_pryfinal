package pe.edu.pizzanajani.model;

import java.sql.Timestamp;

public class Inventory {

    private Integer inventoryId;
    private Integer productId;
    private Integer currentStock;
    private Integer minStock;
    private Integer maxStock;
    private Timestamp lastRestockDate;
    private Integer lastRestockQuantity;
    private Timestamp updatedAt;

    private String productName;
    private String productCode;

    public Inventory() {
    }

    public Inventory(Integer inventoryId, Integer productId, Integer currentStock) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.currentStock = currentStock;
    }

    public Inventory(Integer inventoryId, Integer productId, Integer currentStock,
            Integer minStock, Integer maxStock, Timestamp lastRestockDate,
            Integer lastRestockQuantity, Timestamp updatedAt) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.currentStock = currentStock;
        this.minStock = minStock;
        this.maxStock = maxStock;
        this.lastRestockDate = lastRestockDate;
        this.lastRestockQuantity = lastRestockQuantity;
        this.updatedAt = updatedAt;
    }

    public boolean isLowStock() {
        return currentStock != null && minStock != null && currentStock <= minStock;
    }

    public Integer getStockNeeded() {
        if (currentStock != null && minStock != null && currentStock < minStock) {
            return minStock - currentStock;
        }
        return 0;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Integer getMinStock() {
        return minStock;
    }

    public void setMinStock(Integer minStock) {
        this.minStock = minStock;
    }

    public Integer getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(Integer maxStock) {
        this.maxStock = maxStock;
    }

    public Timestamp getLastRestockDate() {
        return lastRestockDate;
    }

    public void setLastRestockDate(Timestamp lastRestockDate) {
        this.lastRestockDate = lastRestockDate;
    }

    public Integer getLastRestockQuantity() {
        return lastRestockQuantity;
    }

    public void setLastRestockQuantity(Integer lastRestockQuantity) {
        this.lastRestockQuantity = lastRestockQuantity;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    @Override
    public String toString() {
        return "Inventory{"
                + "inventoryId=" + inventoryId
                + ", productId=" + productId
                + ", currentStock=" + currentStock
                + ", minStock=" + minStock
                + ", isLowStock=" + isLowStock()
                + '}';
    }
}
