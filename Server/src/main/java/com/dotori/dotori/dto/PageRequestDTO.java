package com.dotori.dotori.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

// Lombok 애노테이션을 사용하여 보일러플레이트 코드를 생성합니다.
@Builder
@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {

    // Builder 패턴을 사용할 때의 기본 값 설정

    @Builder.Default
    private int size = 10; // 기본 페이지 크기

    private String type;   // 검색 타입 종류 (예: t, c, w, tc, tw, twc)
    private String keyword; // 검색 키워드

    // type 문자열을 배열로 분리하는 메서드
    public String[] getTypes(){
        if(type == null || type.equals("") || type.isEmpty()){
            return null; // type이 비어있거나 null이면 null 반환
        }
        return type.split(","); // type 문자열을 쉼표로 분리하여 배열로 반환
    }

    // 페이지네이션과 정렬을 위한 Pageable 객체 생성 메서드
    public Pageable getPageable(String...props){
        // 지정된 크기 및 정렬 속성을 사용하여 PageRequest 객체 생성
        return PageRequest.of(0, this.size, Sort.by(props).descending());
    }

    private String link; // 캐시된 링크 문자열

    // 페이지네이션과 검색을 위한 쿼리 문자열 생성 메서드
    public String getLink(){
        if(link == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("&size=").append(this.size); // 페이지와 크기 파라미터 추가

            // type 파라미터가 null이 아니고 비어있지 않으면 추가
            if (type != null && type.length() > 0) {
                builder.append("&type=").append(type);
            }
            // keyword 파라미터가 null이 아니고 비어있지 않으면 추가
            if (keyword != null && keyword.length() > 0) {
                try {
                    // 특수 문자를 처리하기 위해 keyword를 URL 인코딩
                    builder.append("&keyword=").append(URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.getMessage(); // 인코딩 예외 처리
                }
            }
            link = builder.toString(); // 생성된 링크를 캐시에 저장
        }

        return link; // 캐시된 링크 반환
    }
}
