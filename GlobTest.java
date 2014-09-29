import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

//use with -Djava.util.logging.SimpleFormatter.format="%4$s: %5$s %n"
public class GlobTest {

	private static Logger log = Logger.getLogger(GlobTest.class.getName());

	@Test
	public void testQuestionMark() {
		log.log(Level.INFO, "testQuestionMark");
		Glob glob = Glob.compile("abc?");
		assertFalse(glob.matches("abc")); // false => ? means exactly one character
		assertTrue(glob.matches("abcd")); // true
		assertFalse(glob.matches("abcde"));
	}

	@Test
	public void testStar() {
		log.log(Level.INFO, "testStar");
		Glob glob2 = Glob.compile("a*d");
		assertTrue(glob2.matches("abcd"));
		assertTrue(glob2.matches("abcujawiohtguahwuthawitthawuithawuthaithawtawutd"));
		assertFalse(glob2.matches("abcdej"));
		assertFalse(glob2.matches("abcujawiohtguahwuthawitthawuithawuthaithawtawutd1"));
	}

	@Test
	public void testStarWithFolders() {
		log.log(Level.INFO, "testStarWithFolders");
		Glob glob3 = Glob.compile("*.html");
		assertTrue(glob3.matches("index.html"));
		assertFalse(glob3.matches("index.htm")); // false - missing 'l'
		assertFalse(glob3.matches("directory/index.html")); // false - crossing directory boundaries
	}

	@Test
	public void testDoubleStar() {
		log.log(Level.INFO, "testDoubleStar");
		Glob glob5 = Glob.compile("/home/georgi/**index.html");
		assertTrue(glob5.matches("/home/georgi/testme/testme2/index.html"));
		assertTrue(glob5.matches("/home/georgi/testme/testme2/testME_index.html"));
		assertFalse(glob5.matches("/home/index.html"));

		Glob glob = Glob.compile("**index.html");
		assertTrue(glob.matches("/home/index.html"));
	}

	@Test
	public void testComposite() {
		log.log(Level.INFO, "testComposite");
		Glob glob5 = Glob.compile("/home**/test?/*list*/index.html");
		assertTrue(glob5.matches("/home/georgi/testme/test2/list/index.html"));
		assertTrue(glob5.matches("/home/georgi/testme/test2/list1/index.html"));
		assertTrue(glob5.matches("/home/georgi/testme/test2/1list/index.html"));
		assertTrue(glob5.matches("/home/georgi/testme/test2/1list1/index.html"));
		assertFalse(glob5.matches("/home/georgi/testme/test/list/index.html"));

		assertTrue(glob5.matches("/home/test2/list/index.html"));
		assertTrue(glob5.matches("/home/georgi/testme/test1/list/index.html"));
	}

	@Test
	public void testWithBrackets() {
		log.log(Level.INFO, "testWithBrackets");
		Glob glob = Glob.compile("/home/index{1,2,3}.html");
		assertTrue(glob.matches("/home/index1.html"));
		assertFalse(glob.matches("/home/index.html"));
		assertFalse(glob.matches("/home/index4.html"));
	}

}
