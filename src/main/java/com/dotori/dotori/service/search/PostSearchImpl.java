package com.dotori.dotori.service.search;


import com.dotori.dotori.dto.PostListCommentCountDTO;
import com.dotori.dotori.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class PostSearchImpl extends QuerydslRepositorySupport implements PostSearch{

    public PostSearchImpl() {
        super(Post.class);
    }

    @Override
    public Page<Post> searchOne(Pageable pageable) {

        QPost post = QPost.post;

        JPQLQuery<Post> query = from(post);   // select .. from post

        query.where(post.content.contains("1")); // where content like ...

        this.getQuerydsl().applyPagination(pageable,query);

        List<Post> content = query.fetch();      // jpql Query에 대한 실행,
        long count = query.fetchCount();        // fetchCount를 이용해 쿼리 실행

        return null;
    }

    @Override
    public Page<Post> searchAll(String[] types, String keyword, Pageable pageable) {

        // 1 Qdomain 객체 생성
        QPost post = QPost.post;

        // 2 sql 작성
        JPQLQuery<Post> query = from(post); //select ~ from board
        if ((types != null && types.length >0) && keyword!= null) {
            //검색 조건과 키워드가 있는 경우
            BooleanBuilder booleanBuilder = new BooleanBuilder(); // (
            for (String type: types) {
                switch (type) {
                    case "c" :
                        booleanBuilder.or(post.content.contains(keyword)); // content like concat('%', keyword, '%')
                        break;
                    case "n" :
                        booleanBuilder.or(post.nickName.contains(keyword)); // nickName like concat('%', keyword, '%')
                        break;
                }
            } // for end
            query.where(booleanBuilder);
        } // if end
        // pid > 0
        query.where(post.pid.gt(0));
        // paging
        this.getQuerydsl().applyPagination(pageable,query);
        List<Post> list = query.fetch();
        long count = query.fetchCount();

        //PageImpl을 통해서 반환 : (알아온 데이터, pageable, (총개수)total)
        return new PageImpl<>(list,pageable,count);
    }

    @Override
    public Page<PostListCommentCountDTO> searchWithCommentCount(String[] types, String keyword, Pageable pageable) {
        QPost post = QPost.post;
        QComment comment = QComment.comment;
        QPostThumbnail postThumbnail = QPostThumbnail.postThumbnail;

        QAuth auth = QAuth.auth;

        JPQLQuery<PostListCommentCountDTO> query = from(post)
                .leftJoin(comment).on(comment.post.eq(post))
                .leftJoin(postThumbnail).on(postThumbnail.post.eq(post))
                .leftJoin(post.auth, auth)
                .groupBy(post.pid)
                .select(Projections.constructor(PostListCommentCountDTO.class,
                        post.pid,
                        post.auth.aid,
                        post.content,
                        (!auth.nickName.equals(post.nickName)) ? auth.nickName : post.nickName,
                        auth.profileImage,
                        post.regDate,
                        post.modDate,
                        JPAExpressions.select(postThumbnail.thumbnail.max()).from(postThumbnail).where(postThumbnail.post.eq(post)),
                        comment.count()));

        if ((types != null && types.length > 0) && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "c":
                        booleanBuilder.or(post.content.contains(keyword));
                        break;
                    case "n":
                        booleanBuilder.or(post.nickName.contains(keyword));
                        break;
                }
            }
            query.where(booleanBuilder);
        }

        query.where(post.pid.gt(0));

        this.getQuerydsl().applyPagination(pageable, query);

        List<PostListCommentCountDTO> content = query.fetch();
        long count = query.fetchCount();

        return new PageImpl<>(content, pageable, count);
    }
}
