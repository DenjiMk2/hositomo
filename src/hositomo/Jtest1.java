package hositomo;

import static org.junit.Assert.*;

import org.junit.Test;
import org.hamcrest.*;

public class Jtest1 {

	@Test
	public void test() {
		TextTree tree2 = new TextTree();
		tree2.init();
		tree2.insert("曇天");
		tree2.insert("です。");
		assertThat(tree2.getSentence(), ("曇天です。"));//TODO
	}

}
