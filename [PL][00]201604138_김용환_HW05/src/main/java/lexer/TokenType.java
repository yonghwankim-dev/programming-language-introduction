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
	 * Ư�����ڸ� �Է¹޾��� �� �ش� ���ڿ� ���ϴ� �������� ��ȯ�Ѵ�.
	 * @param ch Ư������
	 * @return �ش� Ư�����ڿ� ���ϴ� ������
	 */
	static TokenType fromSpecialCharactor(char ch) {
		switch ( ch ) {
			case '+':
				return PLUS;
				//������ Special Character�� ���� ��ū�� ��ȯ�ϵ��� �ۼ�
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
