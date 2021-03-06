package hello.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hello.dao.QuizDAO;
import hello.entities.Quiz;

@Service
public class QuizService {

	private static final Logger LOG = LoggerFactory.getLogger(QuizLoader.class);

	/**
	 * Services
	 */
	private final QuizLoader quizLoader;
	private final QuizDAO quizDao;

	private List<Quiz> allQuestions;

	private class DBPopulator implements Runnable {

		private List<Quiz> questions;

		public DBPopulator(List<Quiz> allQuestions) {
			questions = allQuestions;
		}
		
		@Override
		public void run() {
			LOG.warn(Thread.currentThread() + ": start saving objects into the db...");
			
			for (Quiz q : questions) {
				quizDao.saveQuestion(q);
			}		

			LOG.warn(Thread.currentThread() + ": db population finished.");
		}
	}
	
	@PostConstruct
	public void initialize() {
		allQuestions = new ArrayList<>();
	}
	
	@Autowired
	public QuizService(QuizLoader quizLoader, QuizDAO quizDao) {
		this.quizLoader = quizLoader;
		this.quizDao = quizDao;
	}

	private void loadQuestions() {
		allQuestions = quizDao.getAllQuestions(); 

		if (allQuestions.isEmpty()) {
			allQuestions = quizLoader.loadQuestions(); 

			ExecutorService executorService = Executors.newCachedThreadPool();
			executorService.execute(new DBPopulator(allQuestions));
		}

/*		
		Iterable<Quiz> questions = quizRepository.findAll();
		Iterator<Quiz> it = questions.iterator();

		while (it.hasNext()) {
			allQuestions.add(it.next());
		}
*/
//		allQuestions.add(new Quiz("What is a.hashCode() used for? How is it related to a.equals(b)?",
//				"hashCode() method returns an int hash value corresponding to an object. It's used in hash based collection classes e.g Hashtable, HashMap, LinkedHashMap and so on. It's very tightly related to equals() method. According to Java specification, two objects which are equal to each other using equals() method must have same hash code."));
//
//		allQuestions.add(new Quiz("What is difference between dependency injection and factory design pattern?",
//				"Though both patterns help to take out object creation part from application logic, use of dependency injection results in cleaner code than factory pattern. By using dependency injection, your classes are nothing but POJO which only knows about dependency but doesn't care how they are acquired. In the case of factory pattern, the class also needs to know about factory to acquire dependency. hence, DI results in more testable classes than factory pattern. Please see the answer for a more detailed discussion on this topic."));
//
//		allQuestions.add(new Quiz("The difference between abstract class and interface in Java?",
//				"There are multiple differences between abstract class and interface in Java, but the most important one is Java's restriction on allowing a class to extend just one class but allows it to implement multiple interfaces. An abstract class is good to define default behavior for a family of class, but the interface is good to define Type which is later used to leverage Polymorphism. Please check the answer for a more thorough discussion of this question."));
//
//		allQuestions.add(new Quiz("Can two equal object have the different hash code?",
//				"No, thats not possible according to hash code contract."));
//
//		allQuestions.add(new Quiz("How HashSet works internally in Java?",
//				"HashSet is internally implemented using an HashMap. Since a Map needs key and value, a default value is used for all keys. Similar to HashMap, HashSet doesn't allow duplicate keys and only one null key, I mean you can only store one null object in HashSet."));
//
//		allQuestions.add(new Quiz("What is a compile time constant in Java? What is the risk of using it?",
//				"public static final variables are also known as a compile time constant, the public is optional there. They are replaced with actual values at compile time because compiler know their value up-front and also knows that it cannot be changed during run-time. One of the problem with this is that if you happened to use a public static final variable from some in-house or third party library and their value changed later than your client will still be using old value even after you deploy a new version of JARs. To avoid that, make sure you compile your program when you upgrade dependency JAR files."));
	}
	
	public Quiz getRandomQuestion() {

		if (allQuestions.isEmpty()) {
			loadQuestions();
		}

		Collections.shuffle(allQuestions);
		return allQuestions.get(0);
//		return allQuestions.get(new Random().nextInt(allQuestions.size()));
	}
}