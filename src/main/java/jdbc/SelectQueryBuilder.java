package jdbc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SelectQueryBuilder {

    SelectTarget selectTarget = new SelectTarget();
    String tableName = null;
    OrderTarget orderTarget;
    Long limitValue = null;
    List<CompareTarget> compareTargetList = new ArrayList<>();

    /**
     * SELECT 절 지정
     * @param columns 컬럼명 (가변 인자)
     * @return this (메서드 체이닝)
     */
    public SelectQueryBuilder select(String... columns) {
        this.selectTarget.addSelectColumns(columns);
        return this;
    }

    /**
     * FROM 절 지정
     * @param table 테이블명
     * @return this
     */
    public SelectQueryBuilder from(String table) {
        if (table == null) {
            throw new IllegalStateException("from에 테이블은 필수값임");
        }
        this.tableName = table;
        return this;
    }

    /**
     * ORDER BY 절 지정
     * @param column 정렬 컬럼
     * @param direction "ASC" 또는 "DESC"
     * @return this
     */
    public SelectQueryBuilder orderBy(String column, String direction) {
        this.orderTarget = OrderOperand.parse(column, direction);
        return this;
    }

    /**
     * LIMIT 절 지정
     * @param limit 조회 개수
     * @return this
     */
    public SelectQueryBuilder limit(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("limit는 양의 정수여야 함");
        }
        this.limitValue = (long) limit;
        return this;
    }

    /**
     * SQL 문자열 생성
     * @return 생성된 SQL
     */
    public String build() {
        if (tableName == null) {
            throw new IllegalStateException("table명은 필수임");
        }

        String selectString = this.selectTarget.getSelectColumnsString();

        String baseString = "SELECT " + selectString + " FROM " + tableName;

        if (!compareTargetList.isEmpty()) {
            String whereJoinString = this.compareTargetList.getFirst().getJoinString();

            String whereString = this.compareTargetList.stream()
                    .map(CompareTarget::getTargetString)
                    .collect(Collectors.joining(whereJoinString));

            baseString += " WHERE " + whereString;
        }

        if (orderTarget != null) {
            baseString += " " + this.orderTarget.getOrderString();
        }
        if (limitValue != null) {
            baseString += " LIMIT " + this.limitValue + " ";
        }
        return baseString.trim();
    }

    /**
     * WHERE 절 추가
     */
    public SelectQueryBuilder where(String condition) {
        this.compareTargetList = WhereClauseParser.parse(condition);
        return this;
    }
}
