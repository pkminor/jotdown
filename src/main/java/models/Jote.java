package models;

import java.util.Objects;

public class Jote {
    private int id;
    private String topic;
    private String label;
    private String content;

    public Jote(int id, String topic, String label, String content) {
        this.id = id;
        this.topic = topic;
        this.label = label;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jote jote = (Jote) o;
        return id == jote.id &&
                Objects.equals(topic, jote.topic) &&
                Objects.equals(label, jote.label) &&
                Objects.equals(content, jote.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, topic, label, content);
    }
}
