package com.prog2.labs.model;

/**
 * The Response Container class
 *  Parameterized A first, B second, C third
 * 	Implements three component Container
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class ResponseContainer<A,B,C> {

	private A first;
	private B second;
	private C third;

	public C getThird() {
		return third;
	}

	public void setThird(C third) {
		this.third = third;
	}

	public A getFirst() {
		return first;
	}

	public void setFirst(A first) {
		this.first = first;
	}

	public B getSecond() {
		return second;
	}

	public void setSecond(B second) {
		this.second = second;
	}
}
