package hello.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import hello.entities.Quiz;

@Repository
@Transactional
public class QuizDAO {
	private static final Logger LOG = LoggerFactory.getLogger(QuizDAO.class);

	@Autowired
    private SessionFactory sessionFactory;
 
    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
 
    public String saveQuestion(Quiz question) {
    	LOG.info("Saving question " + question + " to the db...");
        Long isSuccess = (Long)getSession().save(question);
        if (isSuccess >= 1){
            return "Success";
        }else{
            return "Error while Saving Person";
        }
         
    }
 
    public boolean delete(Quiz question) {
        getSession().delete(question);
        return true;
    }
 
    @SuppressWarnings("unchecked")
    public List<Quiz> getAllQuestions() {
        return getSession().createQuery("from Quiz").list();
    }
}
