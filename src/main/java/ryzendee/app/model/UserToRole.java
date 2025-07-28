package ryzendee.app.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Модель для связи пользователя и роли.
 *
 * @author Dmitry Ryazantsev
 */
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserToRole {

    @EmbeddedId
    private UserToRoleId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;

}
