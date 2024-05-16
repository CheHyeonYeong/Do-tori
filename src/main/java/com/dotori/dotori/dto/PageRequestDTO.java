package com.dotori.dotori.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestDTO {
    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10;

    private String type;    //검색 타입 종류 : t,c,w, tc, tw, twc

    private String keyword;

    public String[] getTypes(){
        if(type ==null || type.equals("")||type.isEmpty()){
            return null;
        }
        return type.split(",");
    }
    public Pageable getPageabble(String...props){
        return PageRequest.of(this.page -1, this.size, Sort.by(props).descending());
    }
    private String link ;
    public String getLink(){
        if(link == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("page=" + this.page + "&size=" + this.size);

            if (type != null && type.length() > 0) {
                builder.append("&type=" + type);
            }
            if (keyword != null && keyword.length() > 0) {
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.getMessage();
                }
            }
            link = builder.toString();
        }

        return link;
    }
}
