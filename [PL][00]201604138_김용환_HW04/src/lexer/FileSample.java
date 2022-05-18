package lexer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class FileSample {
    public static void main(String[] ar) throws IOException{
        FileSample ex = new FileSample();
        ClassLoader cloader = ScannerMain.class.getClassLoader();
        String temp = cloader.getResource("lexer/as04.txt").getFile();
        temp = URLDecoder.decode(temp,"UTF-8");

        String pathName = "C:\\Users\\qkdlf\\java workspace backup\\프로그래밍언어개론\\[PL][00]201604138_김용환_HW04\\src\\lexer\\as04.txt";
//        ex.checkPath(pathName);

//        /Users/onsil/game is exist ? false
        ex.checkPath(temp);
//        /Users/onsil/programming is exist ? true
    }

    public void checkPath(String pathName) throws IOException{
        File file = new File(pathName);
        System.out.println(pathName + " is exist ? " + file.exists());
        
        FileReader fr = new FileReader(file);
        int singleCh =0;
        while((singleCh=fr.read())!=-1)
        {
        	System.out.print((char)singleCh);
        }
        fr.close();
    }
}