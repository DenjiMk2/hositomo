package hositomo;

public class test2 {

	public static void main(String[] args) {
		TextTree tree = new TextTree();
		tree.init();
		Cell c;
		c = new Cell(2);
		c.enable = true;
		c.text = "本日は";
		c.linkCell(tree.cells.get(1), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(3);
		c.enable = true;
		c.text = "晴天";
		c.linkCell(tree.cells.get(2), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(4);
		c.enable = true;
		c.text = "なり。";
		c.linkCell(tree.cells.get(3), null);
		tree.cells.put(c.id, c);
		
		c = new Cell(5);
		c.enable = false;
		c.text = "曇天";
		c.linkCell(tree.cells.get(2), tree.cells.get(4));
		tree.cells.put(c.id, c);
		
		System.out.println(tree.getSentence());
		
		TextTree tree2 = new TextTree();
		tree2.init();
		tree2.insert("曇天");
		System.out.println(tree2.getSentence());
				
		
	}

}
