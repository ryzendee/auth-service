package ryzendee.app.model;

import jakarta.persistence.*;
import lombok.*;
import ryzendee.app.jwt.UserRole;

/**
 * Модель роли пользователя.
 *
 * @author Dmitry Ryazantsev
 */
@Table(name = "user_role")
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @Enumerated(EnumType.STRING)
    private UserRole id;

    @Column(nullable = false)
    private String name;

}
