package com.RedditClone.RedditClone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.RedditClone.RedditClone.model.SubReddit;

@Repository
public interface SubRedditRepository extends JpaRepository<SubReddit, Long> {

}
