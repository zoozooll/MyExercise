<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<style type="text/css">
		h1 { font: bold 15pt trebuchet ms, arial; }
		p { font: 10pt verdana; text-align: justify; }
		a:link, a:visited { text-decoration: none; }
		a:hover, a:active { text-decoration: underline; }
BODY {
	BACKGROUND:#FFF; MARGIN: 0px; TEXT-ALIGN: center
}
		</style>
	</head>
	<body>
		<script type="text/javascript">
		var id = parseInt((document.location.search.match(/\d+/) || [0])[0]);
		var seed = 93186752 + Math.round(new Date().getTime() / 1000000) + id * 10;

		function random(limit) {
			var a = 1588635695, m = 4294967291, q = 2, r = 1117695901;
			seed = Math.abs(Math.round(a * (seed % q) - r * (seed / q))) % m;
			return seed % limit;
		}

		function capitalize(words) {
			for (var i in words)
				words[i] = words[i].charAt(0).toUpperCase() + words[i].slice(1).toLowerCase();

			return words;
		}

		function capitalizeRandomly(words) {
		}

		function generateTitle(words) {
			var first = random(words.length - 3), last = first + 1 + random(3);
			return capitalize(words.slice(first, last)).join(' ');
		}

		function generateSentence(words) {
			var first = random(words.length - 20), last = first + 4 + random(16);
			words = words.slice(first, last);
			var result = '';

			for (var i in words) {
				if (i > 0) {
					if (random(5) == 0)
						result += ',';
					else if (random(20) == 0)
						result += ' -';
					result += ' ';
				}

				var anchor = random(15) == 0;

				if (anchor)
					result += '<a href="?id=' + random(100) + '">';

				result += i == 0 || random(5) == 0 ? words[i].charAt(0).toUpperCase() + words[i].slice(1).toLowerCase() : words[i];

				if (anchor)
					result += '</a>';
			}

			return result + '.';
		}

		function generateParagraph(words) {
			var result = '';

			for (var i = random(8) + 1; i > 0; i--)
				result += generateSentence(words) + ' ';

			return result;	
		}

		var fish = 'Sed ut perspiciatis, unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. nemo enim ipsam voluptatem, quia voluptas sit, aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos, qui ratione voluptatem sequi nesciunt, neque porro quisquam est, qui dolorem ipsum, quia dolor sit, amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt, ut labore et dolore magnam aliquam quaerat voluptatem. ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? quis autem vel eum iure reprehenderit, qui in ea voluptate velit esse, quam nihil molestiae consequatur, vel illum, qui dolorem eum fugiat, quo voluptas nulla pariatur?';
		var words = fish.split(/\W+/);
		</script>
	</body>
</html>
