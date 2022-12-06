package com.dunvant.application.beans;

import java.io.Serializable;

/**
 * Created by DEVLABS\pbadhan on 17/1/17.
 */
public class ChildMyMessageDetailBean implements Serializable {
    String id;
    String parentId;
    String usersId;
    String recipientId;
    String subject;
    String message;
    String state;
    String createdAtDate;
    String siteMediaId;
    String createdAtFormatted;
    String messageDateFormatted;
    String messageTimeFormatted;
    String senderDpUrl;
    String receiverDpUrl;
    String fileUrl;
    String mediaType;

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUsersId() {
        return usersId;
    }

    public void setUsersId(String usersId) {
        this.usersId = usersId;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreatedAtDate() {
        return createdAtDate;
    }

    public void setCreatedAtDate(String createdAtDate) {
        this.createdAtDate = createdAtDate;
    }

    public String getSiteMediaId() {
        return siteMediaId;
    }

    public void setSiteMediaId(String siteMediaId) {
        this.siteMediaId = siteMediaId;
    }

    public String getCreatedAtFormatted() {
        return createdAtFormatted;
    }

    public void setCreatedAtFormatted(String createdAtFormatted) {
        this.createdAtFormatted = createdAtFormatted;
    }

    public String getMessageDateFormatted() {
        return messageDateFormatted;
    }

    public void setMessageDateFormatted(String messageDateFormatted) {
        this.messageDateFormatted = messageDateFormatted;
    }

    public String getMessageTimeFormatted() {
        return messageTimeFormatted;
    }

    public void setMessageTimeFormatted(String messageTimeFormatted) {
        this.messageTimeFormatted = messageTimeFormatted;
    }

    public String getSenderDpUrl() {
        return senderDpUrl;
    }

    public void setSenderDpUrl(String senderDpUrl) {
        this.senderDpUrl = senderDpUrl;
    }

    public String getReceiverDpUrl() {
        return receiverDpUrl;
    }

    public void setReceiverDpUrl(String receiverDpUrl) {
        this.receiverDpUrl = receiverDpUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
