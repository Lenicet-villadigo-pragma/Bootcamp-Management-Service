package reactivechallenge.pragma.model.criteria;

import lombok.Getter;

@Getter
public enum SortOrder {
    DESC("desc"),
    ASC("asc");

    private final String name;

    SortOrder(String name) {
        this.name = name;
    }

    public static SortOrder fromString(String value) {
        if (value != null){
            for (SortOrder order : SortOrder.values()) {
                if (order.name().equalsIgnoreCase(value)) {
                    return order;
                }
            }
        }
        return ASC;
    }
}
