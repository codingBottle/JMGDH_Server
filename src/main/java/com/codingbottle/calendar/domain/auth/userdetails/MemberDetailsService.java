//package com.codingbottle.calendar.domain.auth.userdetails;
//
//import com.codingbottle.calendar.domain.member.entity.Member;
//import com.codingbottle.calendar.domain.member.repository.MemberRepository;
//import com.codingbottle.calendar.global.utils.CustomAuthorityUtils;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
//import java.util.Collection;
//import java.util.Optional;
//
//@Component
//public class MemberDetailsService implements UserDetailsService {
//    private final MemberRepository memberRepository;
//    private final CustomAuthorityUtils authorityUtils;
//
//    public MemberDetailsService(MemberRepository memberRepository, CustomAuthorityUtils authorityUtils) {
//        this.memberRepository = memberRepository;
//        this.authorityUtils = authorityUtils;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Optional<Member> optionalMember = memberRepository.findByEmail(username);
//        Member findMember = optionalMember.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. email: " + username)); // 실행되더라도 보안상 이유 BadCredentialsException이 발생한다
//        return new MemberDetails(findMember);
//    }
//
//    private final class MemberDetails extends Member implements UserDetails {
//
//        MemberDetails(Member member) {
//            super(
//                    member.getNickname(),
//                    member.getEmail(),
//                    member.getPassword(),
//                    member.getMemberId(),
//                    member.getRole()
//            );
//        }
//
//        @Override
//        public Collection<? extends GrantedAuthority> getAuthorities() {
//            return authorityUtils.createAuthorities(this.getRole());
//        }
//
//        @Override
//        public String getUsername() {
//            return getEmail();
//        }
//
//        @Override
//        public boolean isAccountNonExpired() {
//            return true;
//        }
//
//        @Override
//        public boolean isAccountNonLocked() {
//            return true;
//        }
//
//        @Override
//        public boolean isCredentialsNonExpired() {
//            return true;
//        }
//
//        @Override
//        public boolean isEnabled() {
//            return true;
//        }
//    }
//}