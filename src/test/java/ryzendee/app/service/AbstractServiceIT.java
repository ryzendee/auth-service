package ryzendee.app.service;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ryzendee.app.AbstractTestcontainers;
import ryzendee.app.testutils.DatabaseUtil;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(DatabaseUtil.Config.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
abstract class AbstractServiceIT extends AbstractTestcontainers {

    @Autowired
    protected DatabaseUtil databaseUtil;

    @BeforeAll
    static void startContainer() {
        postgresContainer.start();
    }
}