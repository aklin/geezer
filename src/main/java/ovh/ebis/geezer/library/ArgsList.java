package ovh.ebis.geezer.library;

import java.util.ArrayDeque;

public class ArgsList<T> {

	private final ArrayDeque<T> hello;

	public ArgsList(){
		hello = new ArrayDeque<>();	
}

	public ArrayDeque<T> getContainer() {
		return hello;
	}
	
	private T getLast(){
		return hello.getLast();
	}
}
