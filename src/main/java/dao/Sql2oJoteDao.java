package dao;

import models.Jote;
import models.Label;
import models.Topic;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oJoteDao implements JoteDao {

    private final Sql2o sql2o;
    public Sql2oJoteDao(Sql2o sql2o) {
        this.sql2o=sql2o;
    }

    @Override
    public List<Jote> getAll() {
        String sql ="Select * from jotes";
        try(Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .executeAndFetch(Jote.class);
        }
    }

    @Override
    public List<Topic> getTopics() {
        String sql ="Select distinct topic from topics order by topic";
        try(Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .executeAndFetch(Topic.class);
        }
    }

    @Override
    public List<Label> getTopicLabels(String topic) {
        String sql ="Select distinct label from jotes where topic =:topic order by label";
        try(Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .addParameter("topic",topic)
                    .executeAndFetch(Label.class);
        }
    }

    @Override
    public void add(Jote jote) {
        String sql="insert into jotes (topic,label,content) values (:topic, :label, :content)";
        try(Connection con = sql2o.open()){
            int id = (int) con.createQuery(sql,true)
                    .bind(jote)
                    .executeUpdate()
                    .getKey();
            jote.setId(id);
        }catch(Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void addTopic(Topic topic) {
        String sql ="insert into topics (topic) values (:topic)";
        try(Connection con = sql2o.open()){
            con.createQuery(sql).bind(topic).executeUpdate();
        }

    }

    @Override
    public Jote findById(int id) {
        String sql="select * from jotes where id= :id";
        try(Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(Jote.class);
        }
    }

    @Override
    public List<Jote> findByTopic(String topic) {
        String sql="select * from jotes where topic= :topic";
        try(Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .addParameter("topic",topic)
                    .executeAndFetch(Jote.class);
        }
    }

    @Override
    public List<Jote> findByTopicLabel(String topic, String label) {
        String sql="select * from jotes where (topic,label) = (:topic, :label) ";
        try(Connection con = sql2o.open()){
            return con.createQuery(sql)
                    .addParameter("topic",topic)
                    .addParameter("label",label)
                    .executeAndFetch(Jote.class);
        }
    }

    @Override
    public void update(int id, String topic, String label, String content) {
        String sql="update jotes set (topic,label,content) = (:topic, :label, :content) where id=:id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("id",id)
                    .addParameter("topic",topic)
                    .addParameter("label",label)
                    .addParameter("content",content)
                    .executeUpdate();
        }catch(Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void clearAllJotes() {
        try(Connection con = sql2o.open()){
            con.createQuery("delete from jotes")
                    .executeUpdate();
        }
    }
}
