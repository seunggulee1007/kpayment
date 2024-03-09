package com.kakaoinsurance.payment.adapter.out.persistence.member;

import com.kakaoinsurance.payment.adapter.out.persistence.UpdatedEntity;
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
@Table(name = "member")
class MemberEntity extends UpdatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private UUID memberId;

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
    @CollectionTable(name = "member_roles", joinColumns = @JoinColumn(name = "id"))
    private Set<MemberRole> roles = Set.of(MemberRole.USER);

    /**
     * 로그인 횟수
     */
    private int loginCount;

    /**
     * 로그인 실패 횟수
     */
    private int loginFailCount;

    public static MemberEntity createNewMember(Member member, PasswordEncoder passwordEncoder) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.memberId = UUID.randomUUID();
        memberEntity.email = member.email();
        memberEntity.nickname = member.nickname();
        memberEntity.password = passwordEncoder.encode(member.password());
        memberEntity.roles = member.roles();
        return memberEntity;
    }

    public Member mapToDomain() {
        return Member.builder()
            .id(this.id)
            .memberId(MemberId.of(this.memberId.toString()))
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
            this.loginFail();
            throw new NotValidMemberException("계정이 존재하지 않거나 비밀번호가 일치하지 않습니다.");
        }
        this.afterLoginSuccess();
    }

    public void loginFail() {
        this.loginFailCount++;
    }

}
