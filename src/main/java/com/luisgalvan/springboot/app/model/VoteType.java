package com.luisgalvan.springboot.app.model;

public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1),
        ;

    VoteType(int direction){

    }
}
