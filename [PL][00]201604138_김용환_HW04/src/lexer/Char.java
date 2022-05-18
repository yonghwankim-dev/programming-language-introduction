package lexer;


class Char {
	private final char value;
	private final CharacterType type;

	enum CharacterType {
		LETTER, DIGIT, SPECIAL_CHAR, WS, END_OF_STREAM,
	}
	
	static Char of(char ch) {
		return new Char(ch, getType(ch));
	}
	
	static Char end() {
		return new Char(Character.MIN_VALUE, CharacterType.END_OF_STREAM);
	}
	
	private Char(char ch, CharacterType type) {
		this.value = ch;
		this.type = type;
	}
	
	char value() {
		return this.value;
	}
	
	CharacterType type() {
		return this.type;
	}
	
	private static CharacterType getType(char ch) {
		int code = (int)ch;
		
		//ch�� ���������� �������� �˻��Ѵ�.(��ҹ��� ����)
		//�ƽ��ڵ尪 10������ 65~90�� �빮�� 97~122�� �ҹ��� 63�� ?�̴�.
		if ((code>=65 && code<=90) || (code>=97 && code<=122) || code==63) { //letter�� �Ǵ� ���ǽ��� �˸°� ä���
			return CharacterType.LETTER;
		}
		
		//�������� �˻�
		if ( Character.isDigit(ch) ) {
			return CharacterType.DIGIT;
		}
		
		switch ( ch ) {
			case '-': case '+': case '*': case '/':
			case '(': case ')':
			case '<': case '=': case '>':
			case '#': case '\'':
				return CharacterType.SPECIAL_CHAR;
		}
		
		if ( Character.isWhitespace(ch) ) {
			return CharacterType.WS;
		}

		throw new IllegalArgumentException("input=" + ch);
	}
}
