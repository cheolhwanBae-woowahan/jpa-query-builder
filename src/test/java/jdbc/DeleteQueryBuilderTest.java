package jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteQueryBuilderTest {

    @Test
    @DisplayName("기본 DELETE 쿼리 생성 - WHERE 조건 포함")
    void test01() {
        String sql = new DeleteQueryBuilder()
                .from("users")
                .where("age < ?")
                .build();

        assertEquals("DELETE FROM users WHERE age < ?", sql);
    }

    @Test
    @DisplayName("등호 조건을 사용한 DELETE 쿼리 생성")
    void test02() {
        String sql = new DeleteQueryBuilder()
                .from("users")
                .where("id = ?")
                .build();

        assertEquals("DELETE FROM users WHERE id = ?", sql);
    }

    @Test
    @DisplayName("WHERE 없이 빌드하면 예외 발생")
    void test03() {
        DeleteQueryBuilder builder = new DeleteQueryBuilder()
                .from("users");

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("테이블명 없이 빌드하면 예외 발생")
    void test04() {
        DeleteQueryBuilder builder = new DeleteQueryBuilder()
                .where("id = ?");

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("from()에 null 전달 시 예외 발생")
    void test05() {
        assertThrows(IllegalArgumentException.class, () ->
                new DeleteQueryBuilder().from(null));
    }

    @Test
    @DisplayName("비교 연산자 >=를 사용한 WHERE 조건")
    void test06() {
        String sql = new DeleteQueryBuilder()
                .from("users")
                .where("age >= ?")
                .build();

        assertEquals("DELETE FROM users WHERE age >= ?", sql);
    }

    @Test
    @DisplayName("비교 연산자 <=를 사용한 WHERE 조건")
    void test07() {
        String sql = new DeleteQueryBuilder()
                .from("users")
                .where("age <= ?")
                .build();

        assertEquals("DELETE FROM users WHERE age <= ?", sql);
    }

    @Test
    @DisplayName("비교 연산자 !=를 사용한 WHERE 조건")
    void test08() {
        String sql = new DeleteQueryBuilder()
                .from("users")
                .where("status != ?")
                .build();

        assertEquals("DELETE FROM users WHERE status != ?", sql);
    }
}
