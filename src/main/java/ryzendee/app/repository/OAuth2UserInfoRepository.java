package ryzendee.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ryzendee.app.model.OAuth2UserInfo;
import ryzendee.app.model.User;

import java.util.UUID;

public interface OAuth2UserInfoRepository extends JpaRepository<OAuth2UserInfo, UUID> {

}
