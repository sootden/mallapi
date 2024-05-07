package org.zerock.mallapi.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.mallapi.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

    @EntityGraph(attributePaths = {"memberRoleList"}) // fetch 조인을 하기 위한 설정 , 연관관계 엔티티 매칭
    @Query("select m from Member m where m.email = :email")
    Member getWithRoles(@Param("email") String email);
}
