package org.zerock.mallapi.repository;

import jakarta.persistence.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Product;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product,Long> {

    @EntityGraph(attributePaths = "imageList") // fetch 조인을 하기 위한 설정 , 연관관계 엔티티 매칭
    @Query("select p from Product p where p.pno = :pno")
    Optional<Product> selectOne(@Param("pno") Long pno);

//    @Override
//    @EntityGraph(attributePaths = "imageList") // fetch 조인을 하기 위한 설정 , 연관관계 엔티티 매칭
//    Optional<Product> findById(Long pno);

    @Transactional
    @Modifying
    @Query("update Product p set p.delFlag = :flag where p.pno = :pno")
    void updateToDelete(@Param("pno") Long pno, @Param("flag") boolean flag);

    @Query("select p,pi from Product p left join p.imageList pi where pi.ord = 0 and p.delFlag = false")
    Page<Object[]> getList(Pageable pageable);
}
