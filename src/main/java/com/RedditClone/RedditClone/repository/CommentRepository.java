package com.RedditClone.RedditClone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RedditClone.RedditClone.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
