package lexer;

import static lexer.TransitionOutput.GOTO_ACCEPT_ID;
import static lexer.TransitionOutput.GOTO_ACCEPT_INT;
import static lexer.TransitionOutput.GOTO_EOS;
import static lexer.TransitionOutput.GOTO_FAILED;
import static lexer.TransitionOutput.GOTO_MATCHED;
import static lexer.TransitionOutput.GOTO_SHARP;
import static lexer.TransitionOutput.GOTO_SIGN;
import static lexer.TransitionOutput.GOTO_START;
import static lexer.TokenType.FALSE;
import static lexer.TokenType.INT;
import static lexer.TokenType.MINUS;
import static lexer.TokenType.PLUS;
import static lexer.TokenType.TRUE;


enum State {
	START {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch ( ch.type() ) {
				case LETTER:	//문자인경우
					context.append(v);
					return GOTO_ACCEPT_ID;
				case DIGIT:	//숫자인경우
					context.append(v);
					return GOTO_ACCEPT_INT;
				case SPECIAL_CHAR: //special character가 들어온 경우
					
					//특수문자에 부호인경우는 +와 -가 있다. 이 +,-에 해당하는 상태이면 INT상태로 이동한다.
					if (v=='+' || v=='-') { //부호인 경우에 상태 반환
						context.append(v);
						return GOTO_SIGN;
					}
					
					//#으로 시작하는 의미는 boolean의 의미로 SHARP 상태로 이동한다.
					else if (v=='#') {  //boolean인 경우에 상태 반환
						context.append(v);
						return GOTO_SHARP;
					}
					else { //그 외에는 type을 알아내서 알맞은 상태로
						context.append(v);
						//입력받은 문자의 토큰 타입 객체 생성
						TokenType tokentype = TokenType.fromSpecialCharactor(v);
						//그외의 경우는 더이상 이어지지 않으므로 MATCHED 상태로 이동한다. 생성자로는 토큰타입과 입력받은 문자이다.
						return GOTO_MATCHED(tokentype,context.getLexime());
					}
					
				case WS:	//공백인 경우
					return GOTO_START;
				case END_OF_STREAM:	//입력이 끝난 경우
					return GOTO_EOS;
				default:
					throw new AssertionError();
			}
		}
	},
	ACCEPT_ID {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch ( ch.type() ) {
				case LETTER:
				case DIGIT:
					context.append(v);
					return GOTO_ACCEPT_ID;
				case SPECIAL_CHAR:
					return GOTO_FAILED;
				case WS:
				case END_OF_STREAM:
					return GOTO_MATCHED(Token.ofName(context.getLexime()));
				default:
					throw new AssertionError();
			}
		}
	},
	ACCEPT_INT {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			switch ( ch.type() ) {
				case LETTER:
					return GOTO_FAILED;
				case DIGIT:
					context.append(ch.value());
					return GOTO_ACCEPT_INT;
				case SPECIAL_CHAR:
					return GOTO_FAILED;
				case WS:
					return GOTO_MATCHED(INT, context.getLexime());
				case END_OF_STREAM:
					return GOTO_FAILED;
				default:
					throw new AssertionError();
			}
		}
	},
	SHARP {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch ( ch.type() ) {
				case LETTER:
					switch ( v ) {
						case 'T':
							context.append(v);
							return GOTO_MATCHED(TRUE, context.getLexime());
						case 'F':
							context.append(v);
							return GOTO_MATCHED(FALSE, context.getLexime());
						default:
							return GOTO_FAILED;
					}
				default:
					return GOTO_FAILED;
			}
		}
	},
	SIGN {
		@Override
		public TransitionOutput transit(ScanContext context) {
			Char ch = context.getCharStream().nextChar();
			char v = ch.value();
			switch ( ch.type() ) {
				case LETTER:
					return GOTO_FAILED;
				case DIGIT:
					context.append(v);
					return GOTO_ACCEPT_INT;
				case SPECIAL_CHAR:
					return GOTO_FAILED;
				case WS:
					String lexme = context.getLexime();
					switch ( lexme ) {
						case "+":
							return GOTO_MATCHED(PLUS, lexme);
						case "-":
							return GOTO_MATCHED(MINUS, lexme);
						default:
							throw new AssertionError();
					}
				case END_OF_STREAM:
					return GOTO_FAILED;
				default:
					throw new AssertionError();
			}
		}
	},
	MATCHED {
		@Override
		public TransitionOutput transit(ScanContext context) {
			throw new IllegalStateException("at final state");
		}
	},
	FAILED{
		@Override
		public TransitionOutput transit(ScanContext context) {
			throw new IllegalStateException("at final state");
		}
	},
	EOS {
		@Override
		public TransitionOutput transit(ScanContext context) {
			return GOTO_EOS;
		}
	};
	
	abstract TransitionOutput transit(ScanContext context);
}
