package dao;

import models.Jote;
import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;


import java.util.PrimitiveIterator;

import static org.junit.Assert.*;

public class Sql2oJoteDaoTest {

    private  static Sql2oJoteDao joteDao;
    private static Connection con;
    @BeforeClass
    public static void setUp() throws Exception {
        String connectionStr="jdbc:postgresql://localhost:5432/jotdown_test";
        Sql2o sql2o = new Sql2o(connectionStr,"pkminor","password");

        joteDao = new Sql2oJoteDao(sql2o);
        con = sql2o.open();
        joteDao.clearAllJotes(); //start with empty table
    }

    @After
    public void tearDown() throws Exception {
        joteDao.clearAllJotes(); //ensure after each test table is cleared.
    }

    @AfterClass
    public static void shutDown() throws Exception {
        con.close();
    }

    @Test
    public void getAll() {
        Jote jote = new Jote(1,"Angular","cli","ng create");
        Jote jote2 = new Jote(1,"Angular","cli","ng create");

        joteDao.add(jote);
        joteDao.add(jote2);
        assertEquals(2,joteDao.getAll().size());
    }

    @Test
    public void getTopics() {
        Jote jote = new Jote(1,"Angular","cli","ng create");
        Jote jote2 = new Jote(1,"Node","cli","ng create");
        Jote jote3 = new Jote(1,"Node","cli","ng create");
        Jote jote4 = new Jote(1,"git","cli","ng create");

        joteDao.add(jote);
        joteDao.add(jote2);
        joteDao.add(jote3);
        joteDao.add(jote4);
        assertEquals(3,joteDao.getTopics().size());
    }

    @Test
    public void add() {
        Jote jote = new Jote(1,"Angular","cli","ng create");
        Jote jote2 = new Jote(1,"Angular","cli","ng create");

        joteDao.add(jote);
        joteDao.add(jote2);
        assertTrue(joteDao.getAll().contains(jote));
        assertTrue(joteDao.getAll().contains(jote2));
    }

    @Test
    public void findById() {
        Jote jote = new Jote(1,"Angular","cli","ng create");
        Jote jote2 = new Jote(1,"Angular","cli","ng create");

        int jote_id = jote.getId();

        joteDao.add(jote);
        joteDao.add(jote2);

        assertNotEquals(jote_id, jote.getId());
        assertEquals(jote2, joteDao.findById(jote2.getId()));


    }

    @Test
    public void update() {
        Jote jote = new Jote(1,"Angular","cli","ng create");
        Jote jote2 = new Jote(1,"Angular","cli","ng create");

        joteDao.add(jote);
        joteDao.add(jote2);

        joteDao.update(jote2.getId(),"Angular","cli","ng generate component");

        assertEquals("ng generate component",joteDao.findById(jote2.getId()).getContent());
        //noted that updated don't change the object, you have to fetch from the db.
        //but you can implement this in the logic just like id was changed to match db value.
    }
}