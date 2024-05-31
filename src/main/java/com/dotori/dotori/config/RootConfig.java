package com.dotori.dotori.config;

import com.dotori.dotori.dto.PostDTO;
import com.dotori.dotori.entity.Post;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig{
    @Bean
    public ModelMapper getMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.LOOSE);

        // PostDTO의 nickName 필드와 Post 엔티티의 auth.nickName 매핑
        modelMapper.createTypeMap(Post.class, PostDTO.class)
                .addMappings(mapper -> mapper.map(src -> src.getAuth().getNickName(), PostDTO::setNickName));

        return modelMapper;
    }
}
