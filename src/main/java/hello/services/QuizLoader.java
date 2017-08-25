package hello.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import hello.entities.Quiz;

@Service
public class QuizLoader {

	private static final Logger LOG = LoggerFactory.getLogger(QuizLoader.class);

	private RestTemplate restTemplate;
	private HttpHeaders headers;
	private Map<String, String> restParams;

	@Value("${url}")
	private String url;

	@PostConstruct
	public void initialize() {
		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		restParams = new LinkedHashMap<>();
	}

	private String castToQuestion(String string) {
		String question = null;
		int index = string.indexOf(")");

		if (index != -1) {
			try {
				Integer.valueOf(string.substring(0, index));
				question = string.substring(index + 1).trim()
						.replaceAll("&nbsp;"," ");
			} catch (NumberFormatException e) {}
		}
		
		return question;
	}

	private String cutOutHtmlTags(String answer, String...tags) {
		StringBuilder sb = new StringBuilder(answer);
		int start, end;
		
		for (String tag : tags) {
			start = sb.indexOf("<" + tag, 0);

			if (start != -1) {
				end = sb.indexOf(">", start + 1);
				sb.replace(start, end + 1, "");

				start = sb.indexOf("</" + tag, 0);

				if (start != -1) {
					end = sb.indexOf(">", start + 1);
					sb.replace(start, end + 1, "");
				}

			}
		}

		return sb.toString();
	}
	
	public String getAnswer(StringBuilder string, int pos) {
		int start = string.indexOf("<br>", pos);
		int end = string.indexOf("<br>", start + 1);

		String answer = string.substring(start + 4, end).trim();
//		answer = cutOutHtmlTags(answer, "a", "span", "b")
//				.replaceAll("&nbsp;"," ")
//				.replaceAll("\n","");
		return answer;
	}

	private List<Quiz> getQuestionsFromString(StringBuilder part) {
		List<Quiz> questions = new ArrayList<>();
		String bChunk, question, answer, reference;
		int start = 0, end = 0, refStart = 0, refEnd = 0;

		while (true) {
			start = part.indexOf("<b>", end);

			if (start != -1) {
				end = part.indexOf("</b>", start);

				if (end != -1) {
					bChunk = part.substring(start + 3, end);
					question = castToQuestion(bChunk);
					
					if (question != null) {
						/**
						 * Look for the reference "(<a href=" part..
						 */
						refStart = part.indexOf("<a href", end);
						
						if (refStart < end + 10) {
							refEnd = part.indexOf("</a>", refStart);

							reference = "(" + part.substring(refStart, refEnd + 4) + ")"; 
							LOG.info("reference: " + reference);
							question += " " + reference;
						}
						
						answer = getAnswer(part, end);
						questions.add(new Quiz(question, answer));
					}
				}
			} 

			if (start == -1 || end == -1) break;
	}
		
		return questions;
	}

	public List<Quiz> loadQuestions() {
		String url1 = "http://javarevisited.blogspot.ru/2015/10/133-java-interview-questions-answers-from-last-5-years.html";
		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		restParams = new LinkedHashMap<>();

		String response = restTemplate.getForObject(url1, String.class, restParams);

		Document doc = Jsoup.parse(response);
		StringBuilder questionsChunk = new StringBuilder(doc.select("div[dir=ltr]").first().html());

		return getQuestionsFromString(questionsChunk);
		
	}
	
	public static void main(String[] args) {
		List<Quiz> quiz = new QuizLoader().loadQuestions();

		for (Quiz q : quiz) {
			LOG.info(q.toString());
		}
//		String aaa = "the throw is used to actually throw an instance of <span style=\"font-family: &quot;courier new&quot; , &quot;courier&quot; , monospace;\">java.lang.Throwable</span> class, which means you can throw both Error and Exception using throw keyword e.g.";
//		LOG.info(new QuizLoader().cutOutHtmlTags(aaa, "a", "span"));

//		String aaa = "question=Give me an example of design pattern which is based upon open closed principle?, answer=This is one of the practical questions I ask experienced Java programmer. I expect them to know about OOP design principles as well as patterns. Open closed design principle asserts that your code should be open for extension but closed for modification. Which means if you want to add new functionality, you can add it easily using the new code but without touching already tried and tested code. &nbsp;There are several design patterns which are based upon open closed design principle e.g." + 
//"Strategy pattern if you need a new strategy, just implement the interface and configure, no need to modify core logic. One working example is&nbsp;" +
//"Collections.sort()&nbsp;method which is based on Strategy pattern and follows the open-closed principle, you don't modify" + 
//"<span style=\"font-family: &quot;courier new&quot; , &quot;courier&quot; , monospace;\">sort()</span> method to sort a new object, what you do is just implement Comparator in your own way.";
//
//		String bbb = aaa.replaceAll("&nbsp;", "");
//		LOG.info(bbb);
	}
}
