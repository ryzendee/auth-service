package ryzendee.app.testutils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

public class DatabaseUtil {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public <E> E find(Object id, Class<E> entityClass) {
        return transactionTemplate.execute(status -> entityManager.find(entityClass, id));
    }

    public <T> List<T> findAll(Class<T> entityClass) {
        String jpql = "SELECT e FROM " + entityClass.getName() + " e";
        return transactionTemplate.execute(status -> {
            TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
            return query.getResultList();
        });
    }

    public <E> void saveAll(E... entities) {
        transactionTemplate.execute(status -> {
            for (E entity : entities) {
                save(entity);
            }
            return null;
        });
    }

    public <E> E save(E entity) {
        return transactionTemplate.execute(status -> {
            entityManager.persist(entity);
            entityManager.flush();
            return entity;
        });
    }

    public <E> E merge(E entity) {
        return transactionTemplate.execute(status -> {
            entityManager.merge(entity);
            entityManager.flush();
            return entity;
        });
    }
    public void cleanDatabase() {
        transactionTemplate.execute(status -> {
            JdbcTestUtils.deleteFromTables(jdbcTemplate, getTablesToClean());
            return null;
        });
    }

    private String[] getTablesToClean() {
        return new String[]{
                "user_to_role",
                "user_role",
                "users"
        };
    }

    @TestConfiguration
    public static class Config {

        @Bean
        public DatabaseUtil databaseUtil() {
            return new DatabaseUtil();
        }
    }
}