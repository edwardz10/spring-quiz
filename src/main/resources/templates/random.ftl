<html>
<head>
	<title>120 Java Interview Questions </title>
</head>
<body>
	<script type="text/javascript" language="javascript">
		function clickOnAnswer() {
		    alert("Вместо меня должно стоять модальное окно");
		}

		function nextQuestion() {
			var xhr = new XMLHttpRequest();
			xhr.open('GET', 'randomquestion', true);
			xhr.send(null);
		}
	</script>

	<h2>
		<p>
		<div id="q">${question.question}</div>
		</p>
	</h2>
	<p>
		<div id="a" onclick="clickOnAnswer()">${question.answer}</div>
	</p>

	<button type="submit" onclick="window.location.reload()">Next question</button>
</body>
</html> 