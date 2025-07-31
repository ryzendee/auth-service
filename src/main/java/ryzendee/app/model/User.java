package ryzendee.app.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Модель пользователя.
 *
 * @author Dmitry Ryazantsev
 */

@Table(name = "users")
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String passwordHash;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;

    @UpdateTimestamp
    private LocalDateTime modifyDate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private OAuth2UserInfo oAuth2UserInfo;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private List<UserToRole> roles = new ArrayList<>();

    public void addUserToRole(UserToRole userToRole) {
        userToRole.assignUser(this);
        roles.add(userToRole);
    }
}
