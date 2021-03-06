package com.huineey.blackpigproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
@Data
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min=2, max=30, message = "제목은 2자이상 30자 이하입니다")
    private String title;
    private String content;

    private String rating;

   /* @Formula("(select count(user) from Likes l where l.post_id = id)")
    private int countLike;

    public boolean isLiked(){
        return getCountLike() > 0;
    }*/

    @ManyToOne //게시글 입장에서는 매니 투 원
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id", referencedColumnName = "id")
    @JsonIgnore
    private Store store;

  /*  @Formuala("(select count(1) from comment c where c.board_id = id)")
    private int totalCommentCount;*/


}
