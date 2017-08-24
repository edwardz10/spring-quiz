package hello.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import hello.entities.Quiz;
import hello.services.QuizService;

@Controller
public class QuizController {

	private static final Logger LOG = LoggerFactory.getLogger(QuizController.class);
	
	private QuizService quizService;

	private Quiz previous;
	
	@Autowired
	public QuizController(QuizService quizService) {
		this.quizService = quizService;
	}

	@RequestMapping(value = "/randomquestion", method = RequestMethod.GET)
	public String redirectRequestToRegistrationPage(ModelMap modelMap) {
		Quiz randomQuestion = quizService.getRandomQuestion();

		while (randomQuestion.equals(previous)) {
			randomQuestion = quizService.getRandomQuestion();
		}

		modelMap.put("question", randomQuestion);
		previous = randomQuestion;
		
		return "random";
	}
}
