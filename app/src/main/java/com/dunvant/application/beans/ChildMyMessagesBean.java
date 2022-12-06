package com.dunvant.application.beans;

import java.io.Serializable;
public class ChildMyMessagesBean implements Serializable {
    String id;
    String parentId;
    String userId;
    String recipientId;
    String subject;
    String message;
    String state;
    String createdAt;
    String siteMediaId;
    String createdAtFormatted;
    String messageFormattedDate;
    String messageTimeFormatted;
    String senderName;
    String senderDpUrl;
    String recieverName;
    String receiverDpUrl;
    String fileUrl;
    String friendId;
    String unreadCount;
    String unreadMessagesIds;

    public String getUnreadMessagesIds() {
        return unreadMessagesIds;
    }

    public void setUnreadMessagesIds(String unreadMessagesIds) {
        this.unreadMessagesIds = unreadMessagesIds;
    }

    public String getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(String unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public String getMessageFormattedDate() {
        return messageFormattedDate;
    }

    public void setMessageFormattedDate(String messageFormattedDate) {
        this.messageFormattedDate = messageFormattedDate;
    }

    public String getMessageTimeFormatted() {
        return messageTimeFormatted;
    }

    public void setMessageTimeFormatted(String messageTimeFormatted) {
        this.messageTimeFormatted = messageTimeFormatted;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderDpUrl() {
        return senderDpUrl;
    }

    public void setSenderDpUrl(String senderDpUrl) {
        this.senderDpUrl = senderDpUrl;
    }

    public String getRecieverName() {
        return recieverName;
    }

    public void setRecieverName(String recieverName) {
        this.recieverName = recieverName;
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
