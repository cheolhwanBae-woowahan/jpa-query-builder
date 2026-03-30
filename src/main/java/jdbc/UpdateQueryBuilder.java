package jdbc;

import java.util.*;
import java.util.stream.Collectors;

public class UpdateQueryBuilder {

    private Map<String, String> updateValues = new LinkedHashMap<>();
    private String tableName;
    private List<SettingTarget> settingTargetList = new LinkedList<>();
    private List<CompareTarget> compareTargetList = new ArrayList<>();


    /**
     * UPDATE할 테이블 지정
     */
    public UpdateQueryBuilder table(String table) {
        if (table == null) {
            throw new IllegalArgumentException("update 대상 테이블이 없음");
        }
        this.tableName = table;
        return this;
    }


    /**
     * SET 절에 컬럼-값 추가
     */
    public UpdateQueryBuilder set(String column, String value) {
        if (column == null) {
            throw new IllegalArgumentException("insert 대상 칼럼이 없음");
        }
        if (!value.equals("?")) {
            throw new IllegalArgumentException("placeholder가 올바른 형식이 아님");
        }
        this.updateValues.put(column, value);
        return this;
    }

    /**
     * WHERE 절 추가
     */
    public UpdateQueryBuilder where(String condition) {
        this.compareTargetList = WhereClauseParser.parse(condition);
        return this;
    }

    /**
     * SQL 생성
     */
    public String build() {
        if (compareTargetList.isEmpty()) {
            throw new IllegalStateException("where절은 필수임");
        }
        if (tableName == null) {
            throw new IllegalStateException("table명은 필수임");
        }
        this.updateValues.forEach((key, value) -> this.settingTargetList.add(new SettingTarget(key, value)));

        String setString = this.settingTargetList.stream()
                .map(SettingTarget::getTargetString)
                .collect(Collectors.joining(", "));

        String whereJoinString = this.compareTargetList.getFirst().getJoinString();

        String whereString = this.compareTargetList.stream()
                .map(CompareTarget::getTargetString)
                .collect(Collectors.joining(whereJoinString));

        return "UPDATE " + this.tableName + " SET " + setString + " WHERE " + whereString;
    }
}
