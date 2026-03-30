package jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteQueryBuilder {

    private String tableName;
    private List<CompareTarget> compareTargetList = new ArrayList<>();

    /**
     * DELETE할 테이블 지정
     */
    public DeleteQueryBuilder from(String table) {
        if (table == null) {
            throw new IllegalArgumentException("update 대상 테이블이 없음");
        }
        this.tableName = table;
        return this;
    }

    /**
     * WHERE 절 추가
     */
    public DeleteQueryBuilder where(String condition) {
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

        String whereJoinString = this.compareTargetList.getFirst().getJoinString();

        String whereString = this.compareTargetList.stream()
                .map(CompareTarget::getTargetString)
                .collect(Collectors.joining(whereJoinString));

        return "DELETE FROM " + tableName + " WHERE " + whereString;
    }
}
