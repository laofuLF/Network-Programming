import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;


public class Client {

    public static void main(String[] args) throws IOException {
        String instruction, temp;
        ArrayList<String> file_names = new ArrayList<>();

        Scanner sc = new Scanner(System.in);
        Socket s = new Socket("localhost", 5101); // connect client with server
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        PrintStream p = new PrintStream(s.getOutputStream()); // pass the input number to the server

        while (true) {
            System.out.println("Enter your instruction: (index or filename): ");
            instruction = sc.nextLine();
            p.println(instruction);
            if (instruction.toLowerCase().equals("index")) {
                temp = in.readLine(); //accept the result from the server
                StringTokenizer tokenized = new StringTokenizer(temp);

                while (tokenized.hasMoreTokens()) {
                    String file_name = tokenized.nextToken();
                    file_names.add(file_name);
                    System.out.println(file_name);
                }
            }else {
                while ((temp = in.readLine()) != null) {
                    System.out.println(temp);
                }
                s.close();
                break;
            }

        }
    }
}
