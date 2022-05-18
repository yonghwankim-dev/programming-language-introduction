package parser.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;

import parser.ast.*;
import parser.ast.BinaryOpNode.BinType;
import lexer.Scanner;
import lexer.ScannerMain;
import lexer.Token;
import lexer.TokenType;

public class CuteParser {
	private Iterator<Token> tokens;
	private static Node END_OF_LIST = new Node(){};
	public CuteParser(File file) {
		try {
			tokens = Scanner.scan(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Token getNextToken() {
		if (!tokens.hasNext())
			return null;
		return tokens.next();
	}

	public Node parseExpr() {
		Token t = getNextToken();
		if (t == null) {
			System.out.println("No more token");
			return null;
		}
		TokenType tType = t.type();
		String tLexeme = t.lexme();

		switch (tType) {
		case ID:
			return new IdNode(tLexeme);
		case INT:
			
			if (tLexeme == null)
				System.out.println("???");	
			return new IntNode(tLexeme);

		// BinaryOpNode�� ���Ͽ� �ۼ�
		// +, -, /, *�� �ش�
		case DIV:	// "/"
		case EQ:	// "="
		case MINUS:	// "-"
		case GT:	// ">"
		case PLUS:	// "+"
		case TIMES:	// "*"
		case LT:	// "<"
			/*
			���� ä��ÿ�.
			*/
			BinaryOpNode binaryNode = new BinaryOpNode(tType);
			return binaryNode;

		// FunctionNode�� ���Ͽ� �ۼ�
		// Ű���尡 FunctionNode�� �ش�
		case ATOM_Q:
		case CAR:
		case CDR:
		case COND:
		case CONS:
		case DEFINE:
		case EQ_Q:
		case LAMBDA:
		case NOT:
		case NULL_Q:
			/*
			���� ä��ÿ�.
			*/
			FunctionNode funtionNode = new FunctionNode(tType);

			return funtionNode;
			
		// BooleanNode�� ���Ͽ� �ۼ�
		case FALSE:
			return BooleanNode.FALSE_NODE;
		case TRUE:
			
			return BooleanNode.TRUE_NODE;

		// case L_PAREN�� ���� case R_PAREN�� ��쿡 ���ؼ� �ۼ�
		// L_PAREN�� ��� parseExprList()�� ȣ���Ͽ� ó��
		case L_PAREN:
			return parseExprList();
		case R_PAREN:
			return END_OF_LIST;
		case APOSTROPHE:
			// if the input is `( a b ) or ` 2
			QuoteNode quoteNode = new QuoteNode();
			Node QuotedNode = parseExpr();	//will be (a b) or 2
			/*
			if(QuotedNode instanceof ListNode) {
				// `(a b)
				((ListNode) QuotedNode).setQuotedIn();
				ListNode listnode = ListNode.cons(QuotedNode,ListNode.ENDLIST);
				ListNode new_listNode = ListNode.cons(quoteNode, listnode);
				return new_listNode;
			}
			else if(QuotedNode instanceof QuotableNode) {
				// `2
				((QuotableNode) QuotedNode).setQuoted();
				ListNode li = ListNode.cons(QuotedNode, ListNode.ENDLIST);
				ListNode listNode = ListNode.cons(quoteNode, li);
				return listNode;
			}
			*/
			if(QuotedNode instanceof ListNode) {
				((ListNode) QuotedNode).setQuotedIn();
				ListNode listNode = ListNode.cons(quoteNode,(ListNode)QuotedNode);
				return listNode;
			}else if(QuotedNode instanceof QuotableNode) {
				((QuotableNode) QuotedNode).setQuoted();
				ListNode li = ListNode.cons(QuotedNode, ListNode.ENDLIST);
				ListNode listNode = ListNode.cons(quoteNode, li);
				return listNode;
			}
		default:
			// head�� next�� ����� head�� ��ȯ�ϵ��� �ۼ�
			System.out.println("Parsing Error!");
			return null;
		}

	}

	// List�� value�� �����ϴ� �޼ҵ�
	private ListNode parseExprList() {
		// head ��带 �ϳ� �����Ͽ� ��ū���� ��ũ����Ʈ �������� R_PAREN�� ���ö����� �����Ѵ�.
		Node head = parseExpr();
		
		if (head == null)
		{
			return null;
		}
			
		if (head == END_OF_LIST) // if next token is RPAREN
		{
			return ListNode.ENDLIST;
		}
		ListNode tail = parseExprList(); 
		if (tail == null)
		{
			return null;
		}
		return ListNode.cons(head, tail);
	}
}
