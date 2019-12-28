import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    public static void main(String[] args) throws IOException {

        final File Folder = new File(args[0]);
        File[] FileList = Folder.listFiles();

        String instruction, output = "";
        ServerSocket s1 = new ServerSocket(5101);
        Socket ss = s1.accept(); // accept incoming request

        BufferedReader in = new BufferedReader(new InputStreamReader(ss.getInputStream())); // accept the number that the client wants to pass from client
        PrintStream p = new PrintStream(ss.getOutputStream()); // pass temp back to client

        while (true) {

            while ((instruction = in.readLine()) != null) {
                if (instruction.toLowerCase().equals("index")) {
                    output = "";
                    for (File file : FileList) {
                        output += " " + file.getName(); // add all file_names together separated by space
                    }
                    p.println(output); // pass the output to client
                }  else {
                    try {
                        file_reader(instruction, p);
                        ss.close();
                        s1.close();
                    } catch (IOException e) {
                        p.println("Error: file does not exist"); // error message from server indicate file doesn't exist
                        ss.close();
                        s1.close();
                    }
                }
            }
        }

    }

    public static void

    // function to read every line of the file and pass them to the client
    public static void file_reader(String file, PrintStream p) throws IOException {
        String file_path = "/Users/RogerF/Desktop/242/Exercise3/test/" + file;
        BufferedReader file_reader = new BufferedReader(new FileReader(file_path));
        String output = "Ok, file was found. Here is the content: ";
        p.println(output);
        String line;
        while ((line = file_reader.readLine()) != null) {
            p.println(line);
        }
    }
}
