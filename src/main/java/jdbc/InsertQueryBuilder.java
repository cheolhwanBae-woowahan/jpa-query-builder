package jdbc;

import java.util.*;
import java.util.stream.Collectors;

public class InsertQueryBuilder {

    private final Map<String, String> insertValues = new LinkedHashMap<>();
    private final List<SettingTarget> settingTargetList = new LinkedList<>();
    private String tableName;

    /**
     * INSERT할 테이블 지정
     */
    public InsertQueryBuilder into(String table) {
        if (table == null) {
            throw new IllegalArgumentException("insert 대상 테이블이 없음");
        }
        this.tableName = table;
        return this;
    }

    /**
     * 컬럼-값 쌍 추가
     * @param column 컬럼명
     * @param value 값 (?, 파라미터 플레이스홀더)
     */
    public InsertQueryBuilder value(String column, String value) {
        if (column == null) {
            throw new IllegalArgumentException("insert 대상 칼럼이 없음");
        }
        if (!value.equals("?")) {
            throw new IllegalArgumentException("placeholder가 올바른 형식이 아님");
        }
        this.insertValues.put(column, value);
        return this;
    }

    /**
     * 여러 컬럼-값 쌍을 Map으로 추가
     */
    public InsertQueryBuilder values(Map<String, String> columnValues) {
        columnValues.forEach(this::value);
        return this;
    }

    /**
     * SQL 생성
     */
    public String build() {
        if (tableName == null) {
            throw new IllegalStateException("insert 대상 테이블이 없음");
        }

        this.insertValues.forEach((key, value) -> this.settingTargetList.add(new SettingTarget(key, value)));
        if (this.settingTargetList.isEmpty()) {
            throw new IllegalStateException("최소 하나 이상의 column을 insert 해야함");
        }
        String columns = this.settingTargetList.stream()
                .map(SettingTarget::getName)
                .collect(Collectors.joining(", "));

        String displayValues = this.settingTargetList.stream()
                .map(SettingTarget::getDisplayValue)
                .collect(Collectors.joining(", "));

        return "INSERT INTO " + this.tableName + " (" + columns + ") VALUES (" + displayValues + ")";
    }
}
