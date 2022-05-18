package lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScannerMain {
    public static final void main(String... args) throws Exception {
        ClassLoader cloader = ScannerMain.class.getClassLoader();
        String path =cloader.getResource("lexer/as04.txt").getFile();	//��� ����
        path = URLDecoder.decode(path,"UTF-8");	//path������ ��ΰ� �����Ƿ� utf-8�� ���ڵ��� �Ͽ� �ٽ� �����Ѵ�.
        File file = new File(path);
        testTokenStream(file);  
    }
    
    // use tokens as a Stream 
    private static void testTokenStream(File file) throws FileNotFoundException {
    	
        Stream<Token> tokens = Scanner.stream(file);
        tokens.map(ScannerMain::toString).forEach(System.out::println);
        
        tokens = Scanner.stream(file);
        List<String> datas = tokens.map(ScannerMain::toString).collect(Collectors.toList());
        
        
        File out_file = new File("hw04.txt");
        FileWriter writer = null;
        try {
        	//���� ������ ���뿡 �̾ ������ true, ���� ���� ���ְ� ���� ������ false
        	writer = new FileWriter(out_file,false);
        	
        	//out_file�� token ���� ���
        	for(int i=0;i<datas.size();i++)
            {
            	writer.write(datas.get(i) + "\n");
            }
        	//���� ���۸� �������� �����Ѵ�.
        	writer.flush();
        }catch(IOException e) {
        	e.printStackTrace();
        }finally {
        	try {
                if(writer != null) writer.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }    
    
    private static String toString(Token token) {
        return String.format("%-3s\t\t%s", token.type().name(), token.lexme());
    }
   
}
