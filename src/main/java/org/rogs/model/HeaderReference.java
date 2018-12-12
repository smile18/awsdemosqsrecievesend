package org.rogs.model;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import org.rogs.model.serializer.RogsOffsetDateTimeDeserializer;

import java.time.OffsetDateTime;


@ApiModel(description = "Header Parameters.")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-03-15T20:40:54.626-04:00")
public class HeaderReference {

  @SerializedName("applicationId")
  private String applicationId = null;

  @SerializedName("senderId")
  private String senderId = null;

  @SerializedName("sessionId")
  private String sessionId = null;

  @SerializedName("timestamp")
  @JsonDeserialize(using = RogsOffsetDateTimeDeserializer.class)
  private String timestamp = null;


  public HeaderReference applicationId(String applicationId) {
    this.applicationId = applicationId;
    return this;
  }


  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  public String getSenderId() {
    return senderId;
  }

  public void setSenderId(String senderId) {
    this.senderId = senderId;
  }

  public HeaderReference sessionId(String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public OffsetDateTime getTimestamp() {
    if(timestamp!=null && !timestamp.isEmpty())
      return OffsetDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME);
    else
      return null;
  }

  // These getter setters are for timestamp to get it in String format
  @JsonIgnore
  public String getTimestampString() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HeaderReference headerReference = (HeaderReference) o;
    return
        Objects.equals(this.applicationId, headerReference.applicationId) &&
        Objects.equals(this.senderId, headerReference.senderId) &&
        Objects.equals(this.sessionId, headerReference.sessionId) &&
        Objects.equals(this.timestamp, headerReference.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(applicationId, senderId, sessionId, timestamp);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HeaderReference {\n");
    
    sb.append("    applicationId: ").append(toIndentedString(applicationId)).append("\n");
    sb.append("    senderId: ").append(toIndentedString(senderId)).append("\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
}

