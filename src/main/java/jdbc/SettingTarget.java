package jdbc;

public class SettingTarget {

    private final String name;
    private final String displayValue;

    public SettingTarget(String name, String displayValue) {
        this.name = name;
        this.displayValue = displayValue;
    }

    public String getTargetString() {
        return name + " = " + displayValue;
    }

    public String getName() {
        return name;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
