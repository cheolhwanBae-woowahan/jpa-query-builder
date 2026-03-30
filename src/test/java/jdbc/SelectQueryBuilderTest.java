package jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SelectQueryBuilderTest {

    @Test
    @DisplayName("기본_SELECT_쿼리_생성")
    void test01() {
        String sql = new SelectQueryBuilder()
                .select("id", "name")
                .from("users")
                .build();

        assertEquals("SELECT id, name FROM users", sql);
    }

    @Test
    @DisplayName("SELECT_없이_호출하면_기본값_사용")
    void test02() {
        String sql = new SelectQueryBuilder()
                .from("users")
                .build();

        assertEquals("SELECT * FROM users", sql);
    }

    @Test
    @DisplayName("FROM_없이_빌드하면_예외_발생")
    void test03() {
        SelectQueryBuilder builder = new SelectQueryBuilder()
                .select("*");

        assertThrows(IllegalStateException.class, () -> builder.build());
    }

    @Test
    @DisplayName("WHERE 조건을 포함한 SELECT 쿼리 생성")
    void test04() {
        String sql = new SelectQueryBuilder()
                .select("*")
                .from("users")
                .where("age >= ?")
                .build();

        assertEquals("SELECT * FROM users WHERE age >= ?", sql);
    }

    @Test
    @DisplayName("WHERE 조건과 ORDER BY를 함께 사용한 SELECT 쿼리 생성")
    void test05() {
        String sql = new SelectQueryBuilder()
                .select("*")
                .from("users")
                .where("age >= ?")
                .orderBy("name", "ASC")
                .build();

        assertEquals("SELECT * FROM users WHERE age >= ? ORDER BY name ASC", sql);
    }

    @Test
    @DisplayName("등호 조건을 사용한 WHERE 절")
    void test06() {
        String sql = new SelectQueryBuilder()
                .select("name", "email")
                .from("users")
                .where("id = ?")
                .build();

        assertEquals("SELECT name, email FROM users WHERE id = ?", sql);
    }

    @Test
    @DisplayName("비교 연산자 <를 사용한 WHERE 절")
    void test07() {
        String sql = new SelectQueryBuilder()
                .select("*")
                .from("users")
                .where("age < ?")
                .build();

        assertEquals("SELECT * FROM users WHERE age < ?", sql);
    }

    @Test
    @DisplayName("WHERE 없이 ORDER BY만 사용한 SELECT 쿼리 생성")
    void test08() {
        String sql = new SelectQueryBuilder()
                .select("*")
                .from("users")
                .orderBy("name", "DESC")
                .build();

        assertEquals("SELECT * FROM users ORDER BY name DESC", sql);
    }

    @Test
    @DisplayName("WHERE, ORDER BY, LIMIT을 모두 사용한 SELECT 쿼리 생성")
    void test09() {
        String sql = new SelectQueryBuilder()
                .select("*")
                .from("users")
                .where("age >= ?")
                .orderBy("name", "ASC")
                .limit(10)
                .build();

        assertEquals("SELECT * FROM users WHERE age >= ? ORDER BY name ASC LIMIT 10", sql);
    }

    @Test
    @DisplayName("from()에 null 전달 시 예외 발생")
    void test10() {
        assertThrows(IllegalStateException.class, () ->
                new SelectQueryBuilder().from(null));
    }

    @Test
    @DisplayName("limit()에 0 이하 값 전달 시 예외 발생")
    void test11() {
        assertThrows(IllegalArgumentException.class, () ->
                new SelectQueryBuilder()
                        .select("*")
                        .from("users")
                        .limit(0));
    }

    @Test
    @DisplayName("비교 연산자 !=를 사용한 WHERE 절")
    void test12() {
        String sql = new SelectQueryBuilder()
                .select("*")
                .from("users")
                .where("status != ?")
                .build();

        assertEquals("SELECT * FROM users WHERE status != ?", sql);
    }
}