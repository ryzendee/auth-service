package ryzendee.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ryzendee.app.dto.SignUpRequest;
import ryzendee.app.dto.SignUpResponse;
import ryzendee.starter.jwt.decoder.AuthRole;
import ryzendee.starter.jwt.decoder.JwtPayload;
import ryzendee.app.model.User;
import ryzendee.app.model.UserToRole;
import ryzendee.app.model.UserToRoleId;

import java.util.List;

/**
 * Маппер для преобразования данных пользователя.
 *
 * @author Dmitry Ryazantsev
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserAppMapper {


    /**
     * Преобразует DTO-запрос о регистрации в модель пользователя.
     *
     * @param dto объект запроса на сохранение пользователя
     * @return модель {@link User}
     */
    User toModel(SignUpRequest dto);

    /**
     * Преобразует модель пользователя в DTO-ответ с данными о регистрации.
     *
     * @param model модель {@link User}
     * @return DTO-ответ {@link SignUpResponse}
     */
    SignUpResponse toSignUpResponse(User model);

    /**
     * Преобразует модель пользователя в DTO с данными для JWT.
     *
     * @param model модель {@link User}
     * @return DTO {@link JwtPayload}
     */
    @Mapping(source = "login", target = "subject")
    JwtPayload toJwtCredentials(User model);

    /**
     * Преобразует список моделей связей пользователя {@link UserToRole}
     * в список с enum {@link AuthRole}.
     *
     * @param userToRoleList список связей пользователя и ролей
     * @return список ролей пользователя
     */
    default List<AuthRole> map(List<UserToRole> userToRoleList) {
        return userToRoleList.stream()
                .map(UserToRole::getId)
                .map(UserToRoleId::getRoleId)
                .toList();
    }
}
