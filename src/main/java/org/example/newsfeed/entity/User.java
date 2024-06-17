package org.example.newsfeed.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.newsfeed.dto.PasswordRequestDTO;
import org.example.newsfeed.dto.UserRequestDTO;
import org.example.newsfeed.exception.InvalidPasswordException;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@ToString
@SuperBuilder
@Table(name = "user")
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 10, max = 20)
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Length(min = 10)
    @Column(nullable = false)
    private String password;

    @Column
    private String name;

    @Email
    @Column(unique = true)
    private String email;

    @Column
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatusEnum status; // UserStatusEnum 타입으로 변경

    @Column
    @Setter
    private String refreshToken;

    @Column
    private String statusChangeTime;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column
    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "user")
    private List<Post> newsfeeds = new ArrayList<>();

    @Builder
    public User(String userId, String password, String name, String email, String comment, String refreshToken, String statusChangeTime, UserStatusEnum status) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.comment = comment;
        this.refreshToken = refreshToken;
        this.statusChangeTime = statusChangeTime;
        this.status = status; // UserStatusEnum 타입으로 설정
    }

    // setStatus 메서드 제거
    /*
    public void setStatus(String status) {
        this.modifyDate = LocalDateTime.now();
    }
    */

    public void updateUser(UserRequestDTO dto) {
        this.name = dto.getName();
        this.comment = dto.getComment();
    }

    public void updatePassword(PasswordRequestDTO dto, BCryptPasswordEncoder passwordEncoder) {
        if (passwordEncoder.matches(dto.getBeforePassword(), this.password)) {
            this.password = passwordEncoder.encode(dto.getUpdatePassword());
        } else {
            throw new InvalidPasswordException("패스워드가 일치하지 않습니다.");
        }
    }

}
