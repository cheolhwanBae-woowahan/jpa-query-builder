package jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpdateQueryBuilderTest {

    @Test
    @DisplayName("기본 UPDATE 쿼리 생성 - SET과 WHERE 포함")
    void test01() {
        String sql = new UpdateQueryBuilder()
                .table("users")
                .set("name", "?")
                .set("age", "?")
                .where("id = ?")
                .build();

        assertEquals("UPDATE users SET name = ?, age = ? WHERE id = ?", sql);
    }

    @Test
    @DisplayName("SET 절에 컬럼 하나만 지정한 UPDATE 쿼리 생성")
    void test02() {
        String sql = new UpdateQueryBuilder()
                .table("users")
                .set("name", "?")
                .where("id = ?")
                .build();

        assertEquals("UPDATE users SET name = ? WHERE id = ?", sql);
    }

    @Test
    @DisplayName("WHERE 없이 빌드하면 예외 발생")
    void test03() {
        UpdateQueryBuilder builder = new UpdateQueryBuilder()
                .table("users")
                .set("name", "?");

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("테이블명 없이 빌드하면 예외 발생")
    void test04() {
        UpdateQueryBuilder builder = new UpdateQueryBuilder()
                .set("name", "?")
                .where("id = ?");

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("table()에 null 전달 시 예외 발생")
    void test05() {
        assertThrows(IllegalArgumentException.class, () ->
                new UpdateQueryBuilder().table(null));
    }

    @Test
    @DisplayName("set()에 null 컬럼명 전달 시 예외 발생")
    void test06() {
        assertThrows(IllegalArgumentException.class, () ->
                new UpdateQueryBuilder()
                        .table("users")
                        .set(null, "?"));
    }

    @Test
    @DisplayName("set()에 플레이스홀더가 아닌 직접 값 전달 시 예외 발생")
    void test07() {
        assertThrows(IllegalArgumentException.class, () ->
                new UpdateQueryBuilder()
                        .table("users")
                        .set("name", "'John'"));
    }

    @Test
    @DisplayName("비교 연산자 <를 사용한 WHERE 조건")
    void test08() {
        String sql = new UpdateQueryBuilder()
                .table("users")
                .set("status", "?")
                .where("age < ?")
                .build();

        assertEquals("UPDATE users SET status = ? WHERE age < ?", sql);
    }

    @Test
    @DisplayName("비교 연산자 >=를 사용한 WHERE 조건")
    void test09() {
        String sql = new UpdateQueryBuilder()
                .table("users")
                .set("status", "?")
                .where("age >= ?")
                .build();

        assertEquals("UPDATE users SET status = ? WHERE age >= ?", sql);
    }

    @Test
    @DisplayName("SET 절의 컬럼 순서가 추가한 순서대로 유지된다")
    void test10() {
        String sql = new UpdateQueryBuilder()
                .table("users")
                .set("email", "?")
                .set("name", "?")
                .set("age", "?")
                .where("id = ?")
                .build();

        assertEquals("UPDATE users SET email = ?, name = ?, age = ? WHERE id = ?", sql);
    }
}
