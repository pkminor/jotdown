package models;

import java.util.Objects;

public class TopicStatus {
    private String topic;
    private boolean active;

    public TopicStatus(String topic, boolean active) {
        this.topic = topic;
        this.active = active;
    }

    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
    public boolean getActive(boolean active) {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicStatus that = (TopicStatus) o;
        return active == that.active &&
                Objects.equals(topic, that.topic);
    }
    @Override
    public int hashCode() {
        return Objects.hash(topic, active);
    }
}
