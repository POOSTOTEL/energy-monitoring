package bsuir.backend.generator.storage.enumeration;

public enum DeviceStatus {
    ACTIVE("Активен"),
    PARTIAL("Частично активен"),
    INACTIVE("Неактивен");

    private final String displayName;

    DeviceStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}