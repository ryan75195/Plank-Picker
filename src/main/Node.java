package main;

import java.io.IOException;

public abstract class Node {
	
	public main m;
	
	public Node(main m) {
		this.m = m;
	}

	public abstract boolean validate();

    public abstract int execute() throws InterruptedException, IOException;
	
	
}
