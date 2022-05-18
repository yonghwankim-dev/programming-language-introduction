package lexer;


public enum TokenType {
	INT,
	ID, 
	TRUE, FALSE, NOT,
	PLUS, MINUS, TIMES, DIV,   //special chractor
	LT, GT, EQ, APOSTROPHE,    //special chractor
	L_PAREN, R_PAREN,QUESTION, //special chractor
	DEFINE, LAMBDA, COND, QUOTE,
	CAR, CDR, CONS,
	ATOM_Q, NULL_Q, EQ_Q; 
	
	/**
	 * 특수문자를 입력받았을 때 해당 문자에 속하는 정수값을 반환한다.
	 * @param ch 특수문자
	 * @return 해당 특수문자에 속하는 정수값
	 */
	static TokenType fromSpecialCharactor(char ch) {
		switch ( ch ) {
			case '+':
				return PLUS;
				//나머지 Special Character에 대해 토큰을 반환하도록 작성
			case '-':
				return MINUS;
			case '*':
				return TIMES;
			case '/':
				return DIV;
			case '<':
				return LT;
			case '>':
				return GT;
			case '=':
				return EQ;
			case '\'':
				return APOSTROPHE;
			case '(':
				return L_PAREN;
			case ')':
				return R_PAREN;
			case '?':
				return QUESTION;
			default:
				throw new IllegalArgumentException("unregistered char: " + ch);
		}
	}
}
