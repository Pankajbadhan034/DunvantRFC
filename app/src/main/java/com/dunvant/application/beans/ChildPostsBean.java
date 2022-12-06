package com.dunvant.application.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by DEVLABS\pbadhan on 19/1/17.
 */
public class ChildPostsBean implements Serializable {

    String localType;
    String id;
    String userId;
    String activity;
    String challengeEntityId;
    String challengeAction;
    String challengeActionPerformedId;
    String challengeActionAgainstId;
    String challengePostDateTime;
    String challengePostParentId;
    String challengeIsShared;
    String challengeChallengeStatus;
    String challengePostOwnerId;
    String postEntitiyId;
    String postEntityImageId;
    String postEntityVideoId;
    String postEntityOwnerId;
    String postEntityCategoryId;
    String postEntityTitle;
    String postEntityDescription;
    String postEntityTargetScore;
    String postEntityTargetTime;
    String postEntityTargetTimeType;
    String postEntityExpiration;
    String postEntityApprovalRequired;
    String postEntityState;
    String postEntityIsGlobal;
    String postEntityCreatedAt;
    String postEntityExpirationFormatted;
    String postEntityExpirationDate;
    String postEntityExpirationDateFormatted;
    String postEntityExpirationTime;
    String postEntityExpirationTimeFormatted;
    String postEntityExpirationLocationIds;
    String postEntityExpirationLocationName;
    String postEntityExpirationSessionIds;
    String postEntityGroupName;
    String postEntityDays;
    String postEntityChallengeImageUrl;
    String postEntityChallengeVideoUrl;
    String postEntityCategoryName;
    String postEntityAcceptedDateFormatted;
    String postEntityChallengeResult;
    String postEntityAchievedScore;
    String postEntityAchievedTime;
    String postEntityAchievedDate;
    String postEntityAchievedDateFormatted;
    String isLikeByYou;
    String entityLikes;
    String entityComments;
    String actionPerformedProfilePicUrl;
    String actionPerformFullName;
    String state;
    String entityId;
    String createdAtDate;
    String postedDateFormatted;
    String postedTimeFormatted;
    String fileName;
    String fileType;
    String isPublic;
    String fileUrl;
    String achievedBy;
    String noCtaAction;
    String approvalStatus;
    String mediaDescription;
    String siteMediaId;
    String requestState;
    String requestStateLabel;
    String sentFrom;
    String postType;
    String suggestions;
    String approvalRequiredByParent;
    String postEntityTargetTimeTypeFormatted;
    String approvedRequiredBy;
    String videoThumbnail;
    String postAddDataType;
    String sessionDetailTitle;
    String sessionDetailLocation;
    String sessionDetailCoachingProg;
    String sessionDetailTermName;
    String sessionDetailDay;
    String sessionDetailFromDateFormatted;
    String sessionDetailToDateFormatted;
    String sessionDetailStartDateFormatted;
    String sessionDetailStartTimeFormatted;
    String sessionDetailEndTimeFormatted;
    String sessionDetailEndDateFormatted;
    String sessionDetailNumberOfHours;
    String sessionDetailGroupName;
    String sessionDetailStartDate;
    String SessionDetailEndDate;
    ArrayList<ChildPostAdditionalTimelineDataBean> childPostAdditionalTimelineDataBeans = new ArrayList<>();
    String commentDetailId;
    String commentDetailEntityType;
    String commentDetailEntityId;
    String commmentDetailCommentedById;
    String commentDetailComment;
    String commentDetailCreatedAt;
    String dateFormatted;
    String startTime;
    String endTime;
    String postTimelineActivityType;
    String challengeResultId;
    String isSynced;
    String youtubeUrl;
    String youTubeEmbedId;

    public String getChallengeResultId() {
        return challengeResultId;
    }

    public void setChallengeResultId(String challengeResultId) {
        this.challengeResultId = challengeResultId;
    }

    public String getPostTimelineActivityType() {
        return postTimelineActivityType;
    }

    public void setPostTimelineActivityType(String postTimelineActivityType) {
        this.postTimelineActivityType = postTimelineActivityType;
    }

    public String getDateFormatted() {
        return dateFormatted;
    }

    public void setDateFormatted(String dateFormatted) {
        this.dateFormatted = dateFormatted;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCommentDetailId() {
        return commentDetailId;
    }

    public void setCommentDetailId(String commentDetailId) {
        this.commentDetailId = commentDetailId;
    }

    public String getCommentDetailEntityType() {
        return commentDetailEntityType;
    }

    public void setCommentDetailEntityType(String commentDetailEntityType) {
        this.commentDetailEntityType = commentDetailEntityType;
    }

    public String getCommentDetailEntityId() {
        return commentDetailEntityId;
    }

    public void setCommentDetailEntityId(String commentDetailEntityId) {
        this.commentDetailEntityId = commentDetailEntityId;
    }

    public String getCommmentDetailCommentedById() {
        return commmentDetailCommentedById;
    }

    public void setCommmentDetailCommentedById(String commmentDetailCommentedById) {
        this.commmentDetailCommentedById = commmentDetailCommentedById;
    }

    public String getCommentDetailComment() {
        return commentDetailComment;
    }

    public void setCommentDetailComment(String commentDetailComment) {
        this.commentDetailComment = commentDetailComment;
    }

    public String getCommentDetailCreatedAt() {
        return commentDetailCreatedAt;
    }

    public void setCommentDetailCreatedAt(String commentDetailCreatedAt) {
        this.commentDetailCreatedAt = commentDetailCreatedAt;
    }

    public String getSessionDetailStartTimeFormatted() {
        return sessionDetailStartTimeFormatted;
    }

    public void setSessionDetailStartTimeFormatted(String sessionDetailStartTimeFormatted) {
        this.sessionDetailStartTimeFormatted = sessionDetailStartTimeFormatted;
    }

    public String getSessionDetailEndTimeFormatted() {
        return sessionDetailEndTimeFormatted;
    }

    public void setSessionDetailEndTimeFormatted(String sessionDetailEndTimeFormatted) {
        this.sessionDetailEndTimeFormatted = sessionDetailEndTimeFormatted;
    }

    public String getSessionDetailStartDate() {
        return sessionDetailStartDate;
    }

    public void setSessionDetailStartDate(String sessionDetailStartDate) {
        this.sessionDetailStartDate = sessionDetailStartDate;
    }

    public String getSessionDetailEndDate() {
        return SessionDetailEndDate;
    }

    public void setSessionDetailEndDate(String sessionDetailEndDate) {
        SessionDetailEndDate = sessionDetailEndDate;
    }

    public ArrayList<ChildPostAdditionalTimelineDataBean> getChildPostAdditionalTimelineDataBeans() {
        return childPostAdditionalTimelineDataBeans;
    }

    public void setChildPostAdditionalTimelineDataBeans(ArrayList<ChildPostAdditionalTimelineDataBean> childPostAdditionalTimelineDataBeans) {
        this.childPostAdditionalTimelineDataBeans = childPostAdditionalTimelineDataBeans;
    }

    public String getPostAddDataType() {
        return postAddDataType;
    }

    public void setPostAddDataType(String postAddDataType) {
        this.postAddDataType = postAddDataType;
    }

    public String getSessionDetailTitle() {
        return sessionDetailTitle;
    }

    public void setSessionDetailTitle(String sessionDetailTitle) {
        this.sessionDetailTitle = sessionDetailTitle;
    }

    public String getSessionDetailLocation() {
        return sessionDetailLocation;
    }

    public void setSessionDetailLocation(String sessionDetailLocation) {
        this.sessionDetailLocation = sessionDetailLocation;
    }

    public String getSessionDetailCoachingProg() {
        return sessionDetailCoachingProg;
    }

    public void setSessionDetailCoachingProg(String sessionDetailCoachingProg) {
        this.sessionDetailCoachingProg = sessionDetailCoachingProg;
    }

    public String getSessionDetailTermName() {
        return sessionDetailTermName;
    }

    public void setSessionDetailTermName(String sessionDetailTermName) {
        this.sessionDetailTermName = sessionDetailTermName;
    }

    public String getSessionDetailDay() {
        return sessionDetailDay;
    }

    public void setSessionDetailDay(String sessionDetailDay) {
        this.sessionDetailDay = sessionDetailDay;
    }

    public String getSessionDetailFromDateFormatted() {
        return sessionDetailFromDateFormatted;
    }

    public void setSessionDetailFromDateFormatted(String sessionDetailFromDateFormatted) {
        this.sessionDetailFromDateFormatted = sessionDetailFromDateFormatted;
    }

    public String getSessionDetailToDateFormatted() {
        return sessionDetailToDateFormatted;
    }

    public void setSessionDetailToDateFormatted(String sessionDetailToDateFormatted) {
        this.sessionDetailToDateFormatted = sessionDetailToDateFormatted;
    }

    public String getSessionDetailStartDateFormatted() {
        return sessionDetailStartDateFormatted;
    }

    public void setSessionDetailStartDateFormatted(String sessionDetailStartDateFormatted) {
        this.sessionDetailStartDateFormatted = sessionDetailStartDateFormatted;
    }

    public String getSessionDetailEndDateFormatted() {
        return sessionDetailEndDateFormatted;
    }

    public void setSessionDetailEndDateFormatted(String sessionDetailEndDateFormatted) {
        this.sessionDetailEndDateFormatted = sessionDetailEndDateFormatted;
    }

    public String getSessionDetailNumberOfHours() {
        return sessionDetailNumberOfHours;
    }

    public void setSessionDetailNumberOfHours(String sessionDetailNumberOfHours) {
        this.sessionDetailNumberOfHours = sessionDetailNumberOfHours;
    }

    public String getSessionDetailGroupName() {
        return sessionDetailGroupName;
    }

    public void setSessionDetailGroupName(String sessionDetailGroupName) {
        this.sessionDetailGroupName = sessionDetailGroupName;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public String getApprovedRequiredBy() {
        return approvedRequiredBy;
    }

    public void setApprovedRequiredBy(String approvedRequiredBy) {
        this.approvedRequiredBy = approvedRequiredBy;
    }

    public String getPostEntityTargetTimeTypeFormatted() {
        return postEntityTargetTimeTypeFormatted;
    }

    public void setPostEntityTargetTimeTypeFormatted(String postEntityTargetTimeTypeFormatted) {
        this.postEntityTargetTimeTypeFormatted = postEntityTargetTimeTypeFormatted;
    }

    public String getApprovalRequiredByParent() {
        return approvalRequiredByParent;
    }

    public void setApprovalRequiredByParent(String approvalRequiredByParent) {
        this.approvalRequiredByParent = approvalRequiredByParent;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getRequestState() {
        return requestState;
    }

    public void setRequestState(String requestState) {
        this.requestState = requestState;
    }

    public String getRequestStateLabel() {
        return requestStateLabel;
    }

    public void setRequestStateLabel(String requestStateLabel) {
        this.requestStateLabel = requestStateLabel;
    }

    public String getSentFrom() {
        return sentFrom;
    }

    public void setSentFrom(String sentFrom) {
        this.sentFrom = sentFrom;
    }

    public String getMediaDescription() {
        return mediaDescription;
    }

    public void setMediaDescription(String mediaDescription) {
        this.mediaDescription = mediaDescription;
    }

    public String getSiteMediaId() {
        return siteMediaId;
    }

    public void setSiteMediaId(String siteMediaId) {
        this.siteMediaId = siteMediaId;
    }

    public String getNoCtaAction() {
        return noCtaAction;
    }

    public void setNoCtaAction(String noCtaAction) {
        this.noCtaAction = noCtaAction;
    }

    public String getLocalType() {
        return localType;
    }

    public void setLocalType(String localType) {
        this.localType = localType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getChallengeEntityId() {
        return challengeEntityId;
    }

    public void setChallengeEntityId(String challengeEntityId) {
        this.challengeEntityId = challengeEntityId;
    }

    public String getChallengeAction() {
        return challengeAction;
    }

    public void setChallengeAction(String challengeAction) {
        this.challengeAction = challengeAction;
    }

    public String getChallengeActionPerformedId() {
        return challengeActionPerformedId;
    }

    public void setChallengeActionPerformedId(String challengeActionPerformedId) {
        this.challengeActionPerformedId = challengeActionPerformedId;
    }

    public String getChallengeActionAgainstId() {
        return challengeActionAgainstId;
    }

    public void setChallengeActionAgainstId(String challengeActionAgainstId) {
        this.challengeActionAgainstId = challengeActionAgainstId;
    }

    public String getChallengePostDateTime() {
        return challengePostDateTime;
    }

    public void setChallengePostDateTime(String challengePostDateTime) {
        this.challengePostDateTime = challengePostDateTime;
    }

    public String getChallengePostParentId() {
        return challengePostParentId;
    }

    public void setChallengePostParentId(String challengePostParentId) {
        this.challengePostParentId = challengePostParentId;
    }

    public String getChallengeIsShared() {
        return challengeIsShared;
    }

    public void setChallengeIsShared(String challengeIsShared) {
        this.challengeIsShared = challengeIsShared;
    }

    public String getChallengeChallengeStatus() {
        return challengeChallengeStatus;
    }

    public void setChallengeChallengeStatus(String challengeChallengeStatus) {
        this.challengeChallengeStatus = challengeChallengeStatus;
    }

    public String getChallengePostOwnerId() {
        return challengePostOwnerId;
    }

    public void setChallengePostOwnerId(String challengePostOwnerId) {
        this.challengePostOwnerId = challengePostOwnerId;
    }

    public String getPostEntitiyId() {
        return postEntitiyId;
    }

    public void setPostEntitiyId(String postEntitiyId) {
        this.postEntitiyId = postEntitiyId;
    }

    public String getPostEntityImageId() {
        return postEntityImageId;
    }

    public void setPostEntityImageId(String postEntityImageId) {
        this.postEntityImageId = postEntityImageId;
    }

    public String getPostEntityVideoId() {
        return postEntityVideoId;
    }

    public void setPostEntityVideoId(String postEntityVideoId) {
        this.postEntityVideoId = postEntityVideoId;
    }

    public String getPostEntityOwnerId() {
        return postEntityOwnerId;
    }

    public void setPostEntityOwnerId(String postEntityOwnerId) {
        this.postEntityOwnerId = postEntityOwnerId;
    }

    public String getPostEntityCategoryId() {
        return postEntityCategoryId;
    }

    public void setPostEntityCategoryId(String postEntityCategoryId) {
        this.postEntityCategoryId = postEntityCategoryId;
    }

    public String getPostEntityTitle() {
        return postEntityTitle;
    }

    public void setPostEntityTitle(String postEntityTitle) {
        this.postEntityTitle = postEntityTitle;
    }

    public String getPostEntityDescription() {
        return postEntityDescription;
    }

    public void setPostEntityDescription(String postEntityDescription) {
        this.postEntityDescription = postEntityDescription;
    }

    public String getPostEntityTargetScore() {
        return postEntityTargetScore;
    }

    public void setPostEntityTargetScore(String postEntityTargetScore) {
        this.postEntityTargetScore = postEntityTargetScore;
    }

    public String getPostEntityTargetTime() {
        return postEntityTargetTime;
    }

    public void setPostEntityTargetTime(String postEntityTargetTime) {
        this.postEntityTargetTime = postEntityTargetTime;
    }

    public String getPostEntityTargetTimeType() {
        return postEntityTargetTimeType;
    }

    public void setPostEntityTargetTimeType(String postEntityTargetTimeType) {
        this.postEntityTargetTimeType = postEntityTargetTimeType;
    }

    public String getPostEntityExpiration() {
        return postEntityExpiration;
    }

    public void setPostEntityExpiration(String postEntityExpiration) {
        this.postEntityExpiration = postEntityExpiration;
    }

    public String getPostEntityApprovalRequired() {
        return postEntityApprovalRequired;
    }

    public void setPostEntityApprovalRequired(String postEntityApprovalRequired) {
        this.postEntityApprovalRequired = postEntityApprovalRequired;
    }

    public String getPostEntityState() {
        return postEntityState;
    }

    public void setPostEntityState(String postEntityState) {
        this.postEntityState = postEntityState;
    }

    public String getPostEntityIsGlobal() {
        return postEntityIsGlobal;
    }

    public void setPostEntityIsGlobal(String postEntityIsGlobal) {
        this.postEntityIsGlobal = postEntityIsGlobal;
    }

    public String getPostEntityCreatedAt() {
        return postEntityCreatedAt;
    }

    public void setPostEntityCreatedAt(String postEntityCreatedAt) {
        this.postEntityCreatedAt = postEntityCreatedAt;
    }

    public String getPostEntityExpirationFormatted() {
        return postEntityExpirationFormatted;
    }

    public void setPostEntityExpirationFormatted(String postEntityExpirationFormatted) {
        this.postEntityExpirationFormatted = postEntityExpirationFormatted;
    }

    public String getPostEntityExpirationDate() {
        return postEntityExpirationDate;
    }

    public void setPostEntityExpirationDate(String postEntityExpirationDate) {
        this.postEntityExpirationDate = postEntityExpirationDate;
    }

    public String getPostEntityExpirationDateFormatted() {
        return postEntityExpirationDateFormatted;
    }

    public void setPostEntityExpirationDateFormatted(String postEntityExpirationDateFormatted) {
        this.postEntityExpirationDateFormatted = postEntityExpirationDateFormatted;
    }

    public String getPostEntityExpirationTime() {
        return postEntityExpirationTime;
    }

    public void setPostEntityExpirationTime(String postEntityExpirationTime) {
        this.postEntityExpirationTime = postEntityExpirationTime;
    }

    public String getPostEntityExpirationTimeFormatted() {
        return postEntityExpirationTimeFormatted;
    }

    public void setPostEntityExpirationTimeFormatted(String postEntityExpirationTimeFormatted) {
        this.postEntityExpirationTimeFormatted = postEntityExpirationTimeFormatted;
    }

    public String getPostEntityExpirationLocationIds() {
        return postEntityExpirationLocationIds;
    }

    public void setPostEntityExpirationLocationIds(String postEntityExpirationLocationIds) {
        this.postEntityExpirationLocationIds = postEntityExpirationLocationIds;
    }

    public String getPostEntityExpirationLocationName() {
        return postEntityExpirationLocationName;
    }

    public void setPostEntityExpirationLocationName(String postEntityExpirationLocationName) {
        this.postEntityExpirationLocationName = postEntityExpirationLocationName;
    }

    public String getPostEntityExpirationSessionIds() {
        return postEntityExpirationSessionIds;
    }

    public void setPostEntityExpirationSessionIds(String postEntityExpirationSessionIds) {
        this.postEntityExpirationSessionIds = postEntityExpirationSessionIds;
    }

    public String getPostEntityGroupName() {
        return postEntityGroupName;
    }

    public void setPostEntityGroupName(String postEntityGroupName) {
        this.postEntityGroupName = postEntityGroupName;
    }

    public String getPostEntityDays() {
        return postEntityDays;
    }

    public void setPostEntityDays(String postEntityDays) {
        this.postEntityDays = postEntityDays;
    }

    public String getPostEntityChallengeImageUrl() {
        return postEntityChallengeImageUrl;
    }

    public void setPostEntityChallengeImageUrl(String postEntityChallengeImageUrl) {
        this.postEntityChallengeImageUrl = postEntityChallengeImageUrl;
    }

    public String getPostEntityChallengeVideoUrl() {
        return postEntityChallengeVideoUrl;
    }

    public void setPostEntityChallengeVideoUrl(String postEntityChallengeVideoUrl) {
        this.postEntityChallengeVideoUrl = postEntityChallengeVideoUrl;
    }

    public String getPostEntityCategoryName() {
        return postEntityCategoryName;
    }

    public void setPostEntityCategoryName(String postEntityCategoryName) {
        this.postEntityCategoryName = postEntityCategoryName;
    }

    public String getPostEntityAcceptedDateFormatted() {
        return postEntityAcceptedDateFormatted;
    }

    public void setPostEntityAcceptedDateFormatted(String postEntityAcceptedDateFormatted) {
        this.postEntityAcceptedDateFormatted = postEntityAcceptedDateFormatted;
    }

    public String getPostEntityChallengeResult() {
        return postEntityChallengeResult;
    }

    public void setPostEntityChallengeResult(String postEntityChallengeResult) {
        this.postEntityChallengeResult = postEntityChallengeResult;
    }

    public String getPostEntityAchievedScore() {
        return postEntityAchievedScore;
    }

    public void setPostEntityAchievedScore(String postEntityAchievedScore) {
        this.postEntityAchievedScore = postEntityAchievedScore;
    }

    public String getPostEntityAchievedTime() {
        return postEntityAchievedTime;
    }

    public void setPostEntityAchievedTime(String postEntityAchievedTime) {
        this.postEntityAchievedTime = postEntityAchievedTime;
    }

    public String getPostEntityAchievedDate() {
        return postEntityAchievedDate;
    }

    public void setPostEntityAchievedDate(String postEntityAchievedDate) {
        this.postEntityAchievedDate = postEntityAchievedDate;
    }

    public String getPostEntityAchievedDateFormatted() {
        return postEntityAchievedDateFormatted;
    }

    public void setPostEntityAchievedDateFormatted(String postEntityAchievedDateFormatted) {
        this.postEntityAchievedDateFormatted = postEntityAchievedDateFormatted;
    }

    public String getIsLikeByYou() {
        return isLikeByYou;
    }

    public void setIsLikeByYou(String isLikeByYou) {
        this.isLikeByYou = isLikeByYou;
    }

    public String getEntityLikes() {
        return entityLikes;
    }

    public void setEntityLikes(String entityLikes) {
        this.entityLikes = entityLikes;
    }

    public String getEntityComments() {
        return entityComments;
    }

    public void setEntityComments(String entityComments) {
        this.entityComments = entityComments;
    }

    public String getActionPerformedProfilePicUrl() {
        return actionPerformedProfilePicUrl;
    }

    public void setActionPerformedProfilePicUrl(String actionPerformedProfilePicUrl) {
        this.actionPerformedProfilePicUrl = actionPerformedProfilePicUrl;
    }

    public String getActionPerformFullName() {
        return actionPerformFullName;
    }

    public void setActionPerformFullName(String actionPerformFullName) {
        this.actionPerformFullName = actionPerformFullName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getCreatedAtDate() {
        return createdAtDate;
    }

    public void setCreatedAtDate(String createdAtDate) {
        this.createdAtDate = createdAtDate;
    }

    public String getPostedDateFormatted() {
        return postedDateFormatted;
    }

    public void setPostedDateFormatted(String postedDateFormatted) {
        this.postedDateFormatted = postedDateFormatted;
    }

    public String getPostedTimeFormatted() {
        return postedTimeFormatted;
    }

    public void setPostedTimeFormatted(String postedTimeFormatted) {
        this.postedTimeFormatted = postedTimeFormatted;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getAchievedBy() {
        return achievedBy;
    }

    public void setAchievedBy(String achievedBy) {
        this.achievedBy = achievedBy;
    }

    public String getIsSynced() {
        return isSynced;
    }

    public void setIsSynced(String isSynced) {
        this.isSynced = isSynced;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getYouTubeEmbedId() {
        return youTubeEmbedId;
    }

    public void setYouTubeEmbedId(String youTubeEmbedId) {
        this.youTubeEmbedId = youTubeEmbedId;
    }
}
