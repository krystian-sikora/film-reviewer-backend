package pl.ksikora.filmreviewerbackend.token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import pl.ksikora.filmreviewerbackend.user.UserEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TokenEntity {

    @Id @GeneratedValue
    public Integer id;
    @Column(unique = true)
    public String token;
    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
    public UserEntity user;
    public boolean revoked;
    public boolean expired;
}
