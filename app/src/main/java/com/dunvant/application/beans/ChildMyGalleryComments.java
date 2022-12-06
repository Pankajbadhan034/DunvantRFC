package com.dunvant.application.beans;

import java.io.Serializable;

/**
 * Created by DEVLABS\pbadhan on 10/1/17.
 */
public class ChildMyGalleryComments implements Serializable {
    String id;
    String entityType;
    String entitiyId;
    String commentedById;
    String comment;
    String createdAt;
    String commentedDate;
    String commentedTime;
    String fullName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntitiyId() {
        return entitiyId;
    }

    public void setEntitiyId(String entitiyId) {
        this.entitiyId = entitiyId;
    }

    public String getCommentedById() {
        return commentedById;
    }

    public void setCommentedById(String commentedById) {
        this.commentedById = commentedById;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCommentedDate() {
        return commentedDate;
    }

    public void setCommentedDate(String commentedDate) {
        this.commentedDate = commentedDate;
    }

    public String getCommentedTime() {
        return commentedTime;
    }

    public void setCommentedTime(String commentedTime) {
        this.commentedTime = commentedTime;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
