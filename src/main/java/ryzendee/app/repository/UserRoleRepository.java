package ryzendee.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ryzendee.app.jwt.UserRole;
import ryzendee.app.model.Role;
import ryzendee.app.model.User;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Role}.
 *
 * @author Dmitry Ryazantsev
 */
public interface UserRoleRepository extends JpaRepository<Role, UserRole> {

    /**
     * Возвращает список ролей по массиву идентификаторов.
     *
     * @param ids массив ролей {@link UserRole}
     * @return список ролей
     */
    List<Role> findByIdIn(UserRole[] ids);

    /**
     * Получает список ролей, назначенных пользователю по логину.
     *
     * @param login логин пользователя
     * @return список ролей пользователя
     */
    @Query("SELECT r FROM User u JOIN u.roles ur JOIN ur.role r WHERE u.login = :login")
    List<Role> findRolesByUserLogin(@Param("login") String login);

}
