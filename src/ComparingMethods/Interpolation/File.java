package ComparingMethods.Interpolation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class File {


    private Scanner scanner;
    private PrintWriter printWriter;

    public File(String input, String output) throws IOException {
        java.io.File in = new java.io.File(input);
        scanner = new Scanner(in);
        printWriter = new PrintWriter(output);
    }

    public int readInt(){
        return scanner.nextInt();
    }

    public double readDouble(){
        return scanner.nextDouble();
    }

    public String readLine(){
        return scanner.nextLine();
    }

    public void write(String s) {
        printWriter.print(s);
        printWriter.flush();
    }

    public void close() {
        scanner.close();
        printWriter.close();
    }
}
