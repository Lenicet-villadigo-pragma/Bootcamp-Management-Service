package reactivechallenge.pragma.model.criteria;

import lombok.Getter;

@Getter
public enum SortField {
    NAME("name"),
    TOTAL_SKILLS("totalSkills");

    private final String fieldName;

    SortField(String fieldName) {
        this.fieldName = fieldName;
    }

    public static SortField fromString(String value) {
        if (value != null){
            for (SortField field : SortField.values()) {
                if (field.name().equalsIgnoreCase(value)) {
                    return field;
                }
            }
        }
        return NAME;
    }
}
