package ryzendee.app.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ryzendee.starter.jwt.decoder.AuthRole;

import java.io.Serializable;
import java.util.UUID;


/**
 * Композитный ключ для связи Пользователя и Роли.
 *
 * @author Dmitry Ryazantsev
 */
@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserToRoleId implements Serializable {

    private UUID userId;

    @Enumerated(EnumType.STRING)
    private AuthRole roleId;
}
