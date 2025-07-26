package ryzendee.app;


import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractTestcontainers {

    private static final String POSTGRES_IMAGE = "postgres:16.9-alpine";

    @ServiceConnection
    protected static PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>(DockerImageName.parse(POSTGRES_IMAGE))
                    .withReuse(false);

}