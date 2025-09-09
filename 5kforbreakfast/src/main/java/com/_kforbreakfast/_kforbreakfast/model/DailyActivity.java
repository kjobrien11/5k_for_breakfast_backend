package com._kforbreakfast._kforbreakfast.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_activity_status")
public class DailyActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "is_complete", nullable = false)
    private Boolean isComplete = false;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    public DailyActivity() {
    }

    public DailyActivity(Activity activity, LocalDate date) {
        this.activity = activity;
        this.date = date;
        this.isComplete = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(Boolean isComplete) {
        this.isComplete = isComplete;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "DailyActivity{" +
                "id=" + id +
                ", activity=" + activity +
                ", date=" + date +
                ", isComplete=" + isComplete +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
