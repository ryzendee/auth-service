package ryzendee.app.model;

import jakarta.persistence.*;
import lombok.*;
import ryzendee.app.enums.OAuth2Provider;

import java.util.UUID;

@Table(name = "oauth2_user_info")
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private OAuth2Provider provider;

    @OneToOne
    private User user;
}
