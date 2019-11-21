import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.js.HandlebarsJs;
import com.google.gson.Gson;
import dao.Sql2oJoteDao;
import models.Jote;
import models.Label;
import models.Topic;
import models.TopicStatus;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.*;

import static spark.Spark.*;
public class App {
    public static void main(String[] args) {

        ProcessBuilder process = new ProcessBuilder();
        Integer port;

        // This tells our app that if Heroku sets a port for us, we need to use that port.
        // Otherwise, if they do not, continue using port 4567.

        if (process.environment().get("PORT") != null) {
            port = Integer.parseInt(process.environment().get("PORT"));
        } else {
            port = 4567;
        }

        port(port);

        //port(1423);

        String host="ec2-107-22-253-158.compute-1.amazonaws.com";
        String DBURI="dehnj8vomqipr4";
        String user="tjooxldvntsnoq";
        String pwd="308e09b0d572b70fea5ccb8257c3e7a864586dd15628ca6e5e73d3b383be1c52";

        String connectionStr="jdbc:postgresql://"+host+":5432/"+DBURI;
        Sql2o sql2o = new Sql2o(connectionStr,user,pwd);

        Sql2oJoteDao joteDao = new Sql2oJoteDao(sql2o);

        get("/all",(req,res)-> new Gson().toJson(joteDao.getAll()));

        get("/", (req,res)->{

            String topic= (req.session().attribute("topic") == null)?
                    "" : req.session().attribute("topic") ;

            String label= (req.session().attribute("label") == null)?
                    "" : req.session().attribute("label") ;

            Map<String,Object> model = new HashMap<>();
            List<Jote> jotes = topic.length()==0? joteDao.getAll() :
                    label.length()==0? joteDao.findByTopic(topic) : joteDao.findByTopicLabel(topic,label);

            List<Topic> topics =  joteDao.getTopics();
            List<TopicStatus> topic_status = new ArrayList<>();

            topics.forEach(t-> topic_status.add(new TopicStatus(t.getTopic(), (t.getTopic().equals(topic))?true:false)));

            List<Label> labels = topic.length() ==0 ? new ArrayList<Label>() : joteDao.getTopicLabels(topic);

            model.put("jotes",jotes);
            model.put("topics",topic_status);
            model.put("filtered_topic",topic);
            model.put("labels",labels);
            model.put("label",label);
            return new ModelAndView(model,"index.hbs");
        }, new HandlebarsTemplateEngine());

        get("/topics/:topic", (req,res)->{

            String topic =  req.params("topic");
            req.session().attribute("topic",topic);
            req.session().attribute("label",null);

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

            if(topic.length()>0 && !joteDao.getTopics().contains(topic)){
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

           //after adding a jote, the topics option should be as was added and the labels filtered to added label
            req.session().attribute("topic",topic);
            req.session().attribute("label",label);



            if(topic.length()>0 && !joteDao.getTopics().contains(topic)){
                joteDao.addTopic(new Topic(topic));
            }

            //avoid empty entries...
            boolean data_provided = topic.length()>0 && label.length()>0 && content.length()>0;
            if(data_provided) {
                Jote jote = new Jote(1, topic, label, content);
                joteDao.add(jote);
            }

            res.redirect("/");

            return null;
        });




    }
}
