package ryzendee.app.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ryzendee.app.dto.RoleDetails;
import ryzendee.app.jwt.JwtPayload;
import ryzendee.app.model.User;

/**
 * Маппер для преобразования данных роли.
 *
 * @author Dmitry Ryazantsev
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserRoleAppMapper {

    /**
     * Преобразует модель роли в DTO.
     *
     * @param model модель {@link ryzendee.app.model.Role}
     * @return DTO {@link RoleDetails}
     */
    RoleDetails toDetails(ryzendee.app.model.Role model);
}
