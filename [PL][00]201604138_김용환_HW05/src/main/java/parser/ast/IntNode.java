package parser.ast;



public class IntNode extends Node {
	public int value;
	
	@Override
	public String toString(){
		return "INT: " + Integer.toString(value);
	}
}
