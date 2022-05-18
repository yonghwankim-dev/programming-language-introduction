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
		
		//ch가 변수명으로 적합한지 검사한다.(대소문자 무시)
		//아스코드값 10진수로 65~90은 대문자 97~122은 소문자 63은 ?이다.
		if ((code>=65 && code<=90) || (code>=97 && code<=122) || code==63) { //letter가 되는 조건식을 알맞게 채우기
			return CharacterType.LETTER;
		}
		
		//숫자인지 검사
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
