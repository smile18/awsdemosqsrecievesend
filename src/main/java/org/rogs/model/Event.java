package org.rogs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Event {
    @SerializedName("eventId")
    private String eventId = null;
    @SerializedName("eventType")
    private String eventType = null;
    @SerializedName("eventTime")
    @JsonDeserialize(
            using = RogsOffsetDateTimeDeserializer.class
    )
    private String eventTime = null;
    @SerializedName("eventProducerId")
    private String eventProducerId = null;
    @SerializedName("eventVersion")
    private String eventVersion = null;
    /*@SerializedName("specifications")
    private List<Specifications> specifications = new ArrayList();
    @SerializedName("auditInfo")
    private AuditInfo auditInfo = null;
    @SerializedName("headerReference")
    private HeaderReference headerReference = null;*/
    @SerializedName("payload")
    @JsonDeserialize(
            using = PayloadToStringDeSerializer.class,
            as = String.class
    )
    @JsonRawValue
    private Object payload = null;

    public Event() {
    }

    public Event eventId(String eventId) {
        this.eventId = eventId;
        return this;
    }


    public String getEventId() {
        return this.eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Event eventType(String eventType) {
        this.eventType = eventType;
        return this;
    }


    public String getEventType() {
        return this.eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Event eventTime(String eventTime) {
        this.eventTime = eventTime;
        return this;
    }


    public OffsetDateTime getEventTime() {
        return this.eventTime != null && !this.eventTime.isEmpty() ? OffsetDateTime.parse(this.eventTime, DateTimeFormatter.ISO_DATE_TIME) : null;
    }

    @JsonIgnore
    public String getEventTimeString() {
        return this.eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public Event eventProducerId(String eventProducerId) {
        this.eventProducerId = eventProducerId;
        return this;
    }


    public String getEventProducerId() {
        return this.eventProducerId;
    }

    public void setEventProducerId(String eventProducerId) {
        this.eventProducerId = eventProducerId;
    }

    public Event eventVersion(String eventVersion) {
        this.eventVersion = eventVersion;
        return this;
    }


    public String getEventVersion() {
        return this.eventVersion;
    }

    public void setEventVersion(String eventVersion) {
        this.eventVersion = eventVersion;
    }

   /* public Event specifications(List<Specifications> specifications) {
        this.specifications = specifications;
        return this;
    }

    public Event addSpecificationsItem(Specifications specificationsItem) {
        this.specifications.add(specificationsItem);
        return this;
    }


    public List<Specifications> getSpecifications() {
        return this.specifications;
    }

    public void setSpecifications(List<Specifications> specifications) {
        this.specifications = specifications;
    }

    public Event auditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
        return this;
    }

    public AuditInfo getAuditInfo() {
        if (this.auditInfo == null) {
            this.setAuditInfo(new AuditInfo());
        }

        return this.auditInfo;
    }

    public void setAuditInfo(AuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public Event headerReference(HeaderReference headerReference) {
        this.headerReference = headerReference;
        return this;
    }


    public HeaderReference getHeaderReference() {
        return this.headerReference;
    }

    public void setHeaderReference(HeaderReference headerReference) {
        this.headerReference = headerReference;
    }*/

    public Event payload(Object payload) {
        this.payload = payload;
        return this;
    }


    public Object getPayload() {
        return this.payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Event event = (Event)o;
            return Objects.equals(this.getEventId(), event.getEventId()) && Objects.equals(this.getEventType(), event.getEventType()) && Objects.equals(this.getEventTime(), event.getEventTime()) && Objects.equals(this.getEventProducerId(), event.getEventProducerId()) && Objects.equals(this.getEventVersion(), event.getEventVersion())
                    //&& Objects.equals(this.getSpecifications(), event.getSpecifications()) && Objects.equals(this.getAuditInfo(), event.getAuditInfo()) && Objects.equals(this.getHeaderReference(), event.getHeaderReference())
                    && Objects.equals(this.getPayload(), event.getPayload());
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.getEventId(), this.getEventType(), this.getEventTime(), this.getEventProducerId(), this.getEventVersion(),
                //this.getSpecifications(), this.getAuditInfo(), this.getHeaderReference(),
                this.getPayload()});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Event{");
        sb.append("eventId='").append(this.eventId).append('\'');
        sb.append(", eventType='").append(this.eventType).append('\'');
        sb.append(", eventTime=").append(this.eventTime);
        sb.append(", eventProducerId='").append(this.eventProducerId).append('\'');
        sb.append(", eventVersion='").append(this.eventVersion).append('\'');
        /*sb.append(", specifications=").append(this.specifications);
        sb.append(", auditInfo=").append(this.auditInfo);
        sb.append(", headerReference=").append(this.headerReference);*/
        sb.append(", payload=").append(this.payload);
        sb.append('}');
        return sb.toString();
    }

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }

}
