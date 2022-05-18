package parser.parse;

import java.io.File;
import java.net.URLDecoder;

public class ParserMain {
	public static final void main(String... args) throws Exception {
        ClassLoader cloader = ParserMain.class.getClassLoader();
        String path = cloader.getResource("parser/as05.txt").getFile();
        path = URLDecoder.decode(path,"utf-8");
		File file = new File(path);
		
          
		CuteParser cuteParser = new CuteParser(file);
		
		
		NodePrinter nodePrinter = new NodePrinter(cuteParser.parseExpr());
		nodePrinter.prettyPrint();

    }
}
