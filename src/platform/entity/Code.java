package platform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
@Entity
@Table(name = "code")

public class Code {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    @JsonIgnore
    private String id;

    @Column(name = "code", length = 10000000)
    private String code;

    @Column(name = "date")
    private String date;

    @Column(name = "time")
    private Long time;

    @Column(name = "views")
    private Long views;

    @Column(name = "restriction_time")
    @JsonIgnore
    private boolean restrictionTime = false;

    @Column(name = "restriction_views")
    @JsonIgnore
    private boolean restrictionViews = false;

    @JsonIgnore
    private long remainTime = 0;


    public static final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss.SSS";

    public Code(String code, String date, Long time, Long views) {
        this.code = code;
        this.date = date;
        this.time = time;
        this.views = views;
        if (time > 0) {
            restrictionTime = true;
        }
        if (views > 0) {
            restrictionViews = true;
        }
    }

    public Code() {

    }

    public static String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
        return date.format(formatter);
    }

    @Override
    public String toString() {
        return "code : " + code + "\n" +
                "date : " + date + "\n";
    }

    public String getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public boolean isRestrictionTime() {
        return restrictionTime;
    }

    public void setRestrictionTime(boolean restrictionTime) {
        this.restrictionTime = restrictionTime;
    }

    public boolean isRestrictionViews() {
        return restrictionViews;
    }

    public void setRestrictionViews(boolean restrictionViews) {
        this.restrictionViews = restrictionViews;
    }

    public long getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(long remainTime) {
        this.remainTime = remainTime;
    }
}
