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

		// BinaryOpNode에 대하여 작성
		// +, -, /, *가 해당
		case DIV:	// "/"
		case EQ:	// "="
		case MINUS:	// "-"
		case GT:	// ">"
		case PLUS:	// "+"
		case TIMES:	// "*"
		case LT:	// "<"
			/*
			여기 채우시오.
			*/
			BinaryOpNode binaryNode = new BinaryOpNode(tType);
			return binaryNode;

		// FunctionNode에 대하여 작성
		// 키워드가 FunctionNode에 해당
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
			여기 채우시오.
			*/
			FunctionNode funtionNode = new FunctionNode(tType);

			return funtionNode;
			
		// BooleanNode에 대하여 작성
		case FALSE:
			return BooleanNode.FALSE_NODE;
		case TRUE:
			
			return BooleanNode.TRUE_NODE;

		// case L_PAREN일 경우와 case R_PAREN일 경우에 대해서 작성
		// L_PAREN일 경우 parseExprList()를 호출하여 처리
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
			// head의 next를 만들고 head를 반환하도록 작성
			System.out.println("Parsing Error!");
			return null;
		}

	}

	// List의 value를 생성하는 메소드
	private ListNode parseExprList() {
		// head 노드를 하나 생성하여 토큰들을 링크리스트 형식으로 R_PAREN이 나올때까지 연결한다.
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
