import dao.Sql2oJoteDao;
import models.Jote;
import models.Label;
import models.Topic;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.*;

import static spark.Spark.*;
public class App {
    public static void main(String[] args) {

        String connectionStr="jdbc:postgresql://localhost:5432/jotdown";
        Sql2o sql2o = new Sql2o(connectionStr,"pkminor","password");

        Sql2oJoteDao joteDao = new Sql2oJoteDao(sql2o);

        get("/", (req,res)->{

            String topic= (req.session().attribute("topic") == null)?
                    "" : req.session().attribute("topic") ;

            String label= (req.session().attribute("label") == null)?
                    "" : req.session().attribute("label") ;

            Map<String,Object> model = new HashMap<>();
            List<Jote> jotes = topic.length()==0? joteDao.getAll() :
                    label.length()==0? joteDao.findByTopic(topic) : joteDao.findByTopicLabel(topic,label);

            List<Topic> topics =  joteDao.getTopics();
            List<Label> labels = topic.length() ==0 ? new ArrayList<Label>() : joteDao.getTopicLabels(topic);

            model.put("jotes",jotes);
            model.put("topics",topics);
            model.put("filtered_topic",topic);
            model.put("labels",labels);
            return new ModelAndView(model,"index.hbs");
        }, new HandlebarsTemplateEngine());

        get("/topics/:topic", (req,res)->{

            String topic =  req.params("topic");
            req.session().attribute("topic",topic);

            res.redirect("/");
            return null;

        });

        get("/topics/:topic/labels/:label", (req,res)->{

            String topic =  req.params("topic");
            String label =  req.params("label");
            req.session().attribute("topic",topic);
            req.session().attribute("label",label);

            res.redirect("/");
            return null;

        });

        post("/topics/new", (req,res)->{
            String topic = req.queryParams("topic");
            //Map<String,Object> model = new HashMap<>();

            if(!joteDao.getTopics().contains(topic)){
                joteDao.addTopic(new Topic(topic));
            }

            //NB : for redirect, no template engine and return null below the redirect.
            res.redirect("/");
            /*List<Jote> jotes = joteDao.getAll();
            List<Topic> topics =  joteDao.getTopics();
            model.put("jotes",jotes);
            model.put("topics",topics);

            return new ModelAndView(model,"index.hbs");*/
            return null;
        });

        post("/jotes/new", (req,res)->{
            String topic = req.queryParams("topic");
            String label = req.queryParams("label");
            String content = req.queryParams("content");

            if(!joteDao.getTopics().contains(topic)){
                joteDao.addTopic(new Topic(topic));
            }

            Jote jote =  new Jote(1,topic,label,content);
            joteDao.add(jote);

            res.redirect("/");

            return null;
        });


    }
}
