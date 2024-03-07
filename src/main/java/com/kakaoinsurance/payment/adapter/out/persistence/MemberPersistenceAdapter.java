package com.kakaoinsurance.payment.adapter.out.persistence;

import com.kakaoinsurance.payment.application.port.out.GetMemberPort;
import com.kakaoinsurance.payment.application.port.out.RegisterMemberPort;
import com.kakaoinsurance.payment.common.PersistenceAdapter;
import com.kakaoinsurance.payment.common.advice.exceptions.AlreadyPresentAccountException;
import com.kakaoinsurance.payment.common.advice.exceptions.NotValidMemberException;
import com.kakaoinsurance.payment.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

/**
 * 계정 JPA 용 어댑터
 * JPA 관련 로직들을 해당 클래스에 작성한다.
 *
 * @see MemeberEntity
 */
@PersistenceAdapter
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements RegisterMemberPort, GetMemberPort {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 등록
     * 데이터 베이스에 사용자를 등록시킨 이후 도메인으로 변환해서 반환한다.
     *
     * @param member 사용자 도메인
     * @return 등록된 사용자 도메인
     */
    @Override
    public Member registerMember(Member member) {
        memberRepository.findByEmail(member.email()).ifPresent(c -> {
            throw new AlreadyPresentAccountException("이미 존재하는 계정입니다.");
        });
        MemeberEntity memeberEntity = MemeberEntity.createNewMember(member, passwordEncoder);
        memberRepository.save(memeberEntity);
        return memeberEntity.mapToDomain();
    }

    /**
     * 이메일과 비밀번호가 일치하는 사용자가 있는지 확인
     *
     * @param email    사용자 이메일
     * @param password 비밀번호
     * @return Account 계정 정보
     */
    @Override
    @Transactional
    public Member getMemberByEmailAndPassword(String email, String password) {
        MemeberEntity memeberEntity = memberRepository.findByEmail(email).orElseThrow(NotValidMemberException::new);
        memeberEntity.login(passwordEncoder, password);
        return memeberEntity.mapToDomain();
    }

    @Override
    public Member getMemberByIdAndPassword(Long memberId, String password) {
        return memberRepository.findById(memberId).orElseThrow(NotValidMemberException::new).mapToDomain();
    }

}
