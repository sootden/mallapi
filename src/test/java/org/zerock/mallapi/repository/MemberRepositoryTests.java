package org.zerock.mallapi.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.domain.MemberRole;

@SpringBootTest
@Slf4j
public class MemberRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testInsertMemeber(){
        for(int i =0; i < 10; i++){
            Member member = Member.builder()
                    .email("user"+i+"@aaa.com")
                    .pw(passwordEncoder.encode("11111"))
                    .nickname("USER"+i)
                    .build();

            member.addRole(MemberRole.USER);

            if(i >= 5){
                member.addRole(MemberRole.MANAGER);
            }

            if(i >= 8){
                member.addRole(MemberRole.ADMIN);
            }

            memberRepository.save(member);
        }
    }

    @Test
    public void testRead(){
        String email = "user9@aaa.com";
        Member member = memberRepository.getWithRoles(email);
        log.info("-----------------");
        log.info("{}",member);
    }
}
