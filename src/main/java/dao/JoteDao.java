package dao;

import models.Jote;

import java.util.List;

public interface JoteDao {
    List<Jote> getAll();

    void add(Jote jote);

    Jote findById(int id);

    void update(int id, String topic,  String label, String content);
    void clearAllJotes();
}
