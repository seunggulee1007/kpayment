package com.kakaoinsurance.payment.adapter.out.persistence;

import com.kakaoinsurance.payment.common.advice.exceptions.NotValidMemberException;
import com.kakaoinsurance.payment.domain.Member;
import com.kakaoinsurance.payment.domain.MemberId;
import com.kakaoinsurance.payment.domain.MemberRole;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(name = "account")
class MemeberEntity extends UpdatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID accountId;

    /**
     * 닉네임
     */
    private String nickname;

    /**
     * 이메일
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * 비밀번호
     */
    private String password;

    /* 권한 */
    @ElementCollection(fetch = LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "account_roles", joinColumns = @JoinColumn(name = "id"))
    private Set<MemberRole> roles = Set.of(MemberRole.USER);

    /**
     * 로그인 횟수
     */
    private int loginCount;

    /**
     * 로그인 실패 횟수
     */
    private int loginFailCount;

    public static MemeberEntity createNewMember(Member member, PasswordEncoder passwordEncoder) {
        MemeberEntity memeberEntity = new MemeberEntity();
        memeberEntity.accountId = UUID.randomUUID();
        memeberEntity.email = member.email();
        memeberEntity.nickname = member.nickname();
        memeberEntity.password = passwordEncoder.encode(member.password());
        memeberEntity.roles = member.roles();
        return memeberEntity;
    }

    public Member mapToDomain() {
        return Member.builder()
            .id(this.id)
            .memberId(MemberId.of(this.accountId.toString()))
            .email(this.email)
            .nickname(this.nickname)
            .loginCount(this.loginCount)
            .loginFailCount(this.loginFailCount)
            .roles(this.roles)
            .build();
    }

    /**
     * 로그인 후 세팅
     */
    public void afterLoginSuccess() {
        this.loginFailCount = 0;
        this.loginCount++;
    }

    public void login(PasswordEncoder passwordEncoder, String credential) {
        if (!passwordEncoder.matches(credential, this.password)) {
            this.loginFailCount++;
            throw new NotValidMemberException("계정이 존재하지 않거나 비밀번호가 일치하지 않습니다.");
        }
        this.afterLoginSuccess();
    }

    public void loginFail() {
        this.loginFailCount++;
    }

}
