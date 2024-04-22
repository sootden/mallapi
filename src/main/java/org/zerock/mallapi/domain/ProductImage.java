package org.zerock.mallapi.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

//Embeddable : 값 타입 객체임을 명시
@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {
    private String fileName;
    private int ord;

    public void setOrd(int ord){
        this.ord = ord;
    }
}
