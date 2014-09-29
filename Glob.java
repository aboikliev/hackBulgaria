import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Glob {
	private static final Logger log = Logger.getLogger(Glob.class.getName());
	private String globString;
	private Pattern pattern;

	static {
		ConsoleHandler h = new ConsoleHandler();
		h.setLevel(Level.ALL);
		h.setFormatter(new SimpleFormatter());
	}

	private Glob(String glob, Pattern pattern) {
		globString = glob;
		this.pattern = pattern;
	}

	private static String createRegexFromGlob(String glob) {
		String result = "";
		int brackets = 0;
		for (int i = 0; i < glob.length(); ++i) {
			final char character = glob.charAt(i);
			switch (character) {
			case '*':
				if (glob.charAt(i + 1) == '*') {
					result += ".*";
					++i;
					break;
				}
				else {
					result += "\\w*";
					break;
				}
			case '?':
				result += '.';
				break;
			case '{': {
				brackets++;
				result += '(';
				break;
			}
			case '}': {
				--brackets;
				result += ')';
				break;
			}
			case ',':
				if (brackets > 0) {
					result += '|';
				}
				else {
					result += ',';
				}
				break;
			default:
				result += character;
			}
		}
		log.log(Level.INFO, "globe RegEx:" + result);
		return result;
	}

	@Override
	public String toString() {
		return globString;
	}

	public boolean matches(String path) {
		Matcher m = pattern.matcher(path);
		return m.matches();
	}

	public static Glob compile(String glob) {
		log.log(Level.INFO, "Creating Glob from string:" + glob);
		return new Glob(glob, Pattern.compile(createRegexFromGlob(glob)));
	}

}
