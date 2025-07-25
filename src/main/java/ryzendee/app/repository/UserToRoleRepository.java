package ryzendee.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ryzendee.app.model.UserToRole;
import ryzendee.app.model.UserToRoleId;

/**
 * Репозиторий для работы с сущностью UserToRole.
 *
 * @author Dmitry Ryazantsev
 */
public interface UserToRoleRepository extends JpaRepository<UserToRole, UserToRoleId> {
}
