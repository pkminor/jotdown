package dao;

import models.Jote;
import models.Label;
import models.Topic;

import java.util.List;

public interface JoteDao {
    List<Jote> getAll();
    List<Topic> getTopics();

    //maybe have a generic column class?? rather than one class for each column??
    List<Label> getTopicLabels(String topic);

    void add(Jote jote);
    void addTopic(Topic topic);

    Jote findById(int id);
    List<Jote> findByTopic(String topic);
    List<Jote> findByTopicLabel(String topic, String label);

    void update(int id, String topic,  String label, String content);
    void clearAllJotes();
}
