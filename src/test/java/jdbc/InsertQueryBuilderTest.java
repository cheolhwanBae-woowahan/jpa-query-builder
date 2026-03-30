package jdbc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InsertQueryBuilderTest {

    @Test
    @DisplayName("기본 INSERT 쿼리 생성 - value()로 컬럼-값 하나씩 추가")
    void test01() {
        String sql = new InsertQueryBuilder()
                .into("users")
                .value("name", "?")
                .value("age", "?")
                .value("email", "?")
                .build();

        assertEquals("INSERT INTO users (name, age, email) VALUES (?, ?, ?)", sql);
    }

    @Test
    @DisplayName("Map을 사용한 INSERT 쿼리 생성 - values()로 한 번에 추가")
    void test02() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("name", "?");
        values.put("age", "?");

        String sql = new InsertQueryBuilder()
                .into("users")
                .values(values)
                .build();

        assertEquals("INSERT INTO users (name, age) VALUES (?, ?)", sql);
    }

    @Test
    @DisplayName("컬럼-값 쌍 없이 빌드하면 예외 발생")
    void test03() {
        InsertQueryBuilder builder = new InsertQueryBuilder()
                .into("users");

        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("테이블명 없이 빌드하면 예외 발생")
    void test04() {
        InsertQueryBuilder builder = new InsertQueryBuilder()
                .value("name", "?");

        assertThrows(Exception.class, builder::build);
    }

    @Test
    @DisplayName("into()에 null 전달 시 예외 발생")
    void test05() {
        assertThrows(IllegalArgumentException.class, () ->
                new InsertQueryBuilder().into(null));
    }

    @Test
    @DisplayName("value()에 null 컬럼명 전달 시 예외 발생")
    void test06() {
        assertThrows(IllegalArgumentException.class, () ->
                new InsertQueryBuilder()
                        .into("users")
                        .value(null, "?"));
    }

    @Test
    @DisplayName("플레이스홀더가 아닌 직접 값 전달 시 예외 발생")
    void test07() {
        assertThrows(IllegalArgumentException.class, () ->
                new InsertQueryBuilder()
                        .into("users")
                        .value("name", "'John'"));
    }

    @Test
    @DisplayName("컬럼 하나만 있는 INSERT 쿼리 생성")
    void test08() {
        String sql = new InsertQueryBuilder()
                .into("users")
                .value("name", "?")
                .build();

        assertEquals("INSERT INTO users (name) VALUES (?)", sql);
    }

    @Test
    @DisplayName("LinkedHashMap 사용 시 컬럼 순서가 추가한 순서대로 유지된다")
    void test09() {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("email", "?");
        values.put("name", "?");
        values.put("age", "?");

        String sql = new InsertQueryBuilder()
                .into("users")
                .values(values)
                .build();

        assertEquals("INSERT INTO users (email, name, age) VALUES (?, ?, ?)", sql);
    }

    @Test
    @DisplayName("value()와 values()를 혼합하여 사용할 수 있다")
    void test10() {
        Map<String, String> mapValues = new LinkedHashMap<>();
        mapValues.put("age", "?");
        mapValues.put("email", "?");

        String sql = new InsertQueryBuilder()
                .into("users")
                .value("name", "?")
                .values(mapValues)
                .build();

        assertEquals("INSERT INTO users (name, age, email) VALUES (?, ?, ?)", sql);
    }
}
