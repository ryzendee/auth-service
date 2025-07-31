package ryzendee.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ryzendee.app.model.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сущностью {@link User}.
 *
 * @author Dmitry Ryazantsev
 */
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Поиск пользователя по логину.
     *
     * @param login логин пользователя
     * @return {@link Optional} с пользователем, если найден
     */
    Optional<User> findByLogin(String login);

    /**
     * Проверяет, существует ли пользователь с указанной электронной почтой.
     *
     * @param email электронная почта пользователя
     * @return true, если пользователь существует
     */
    boolean existsByEmail(String email);

    /**
     * Проверяет, существует ли пользователь с указанным логином.
     *
     * @param login логин для проверки
     * @return true, если пользователь существует
     */
    boolean existsByLogin(String login);

    @Query("SELECT u FROM User u JOIN u.oAuth2UserInfo WHERE u.email = :email")
    Optional<User> findByEmailWithOAuth2Info(@Param("email") String email);
}
