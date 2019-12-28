import java.io.*;
import java.io.IOException;
import java.net.*;

public class Server {

    public static void main(String[] args) throws IOException {
        DatagramSocket ServerSocket = new DatagramSocket(9876);
        final File Folder = new File("/Users/RogerF/Desktop/242/Exercise4/test");
        File[] FileList = Folder.listFiles();
        int transfer_data_size = 50;

        while (true) {
            int count = 0;
            byte[] receiveData = new byte[transfer_data_size];
            String output = null;
            DatagramPacket ReceivePacket = new DatagramPacket(receiveData, receiveData.length);
            ServerSocket.receive(ReceivePacket); // receive packet from client

            String Instruction = new String(ReceivePacket.getData()); // convert instruction from bytes to string
            InetAddress IPAddress = ReceivePacket.getAddress(); // get IP address from client
            int port = ReceivePacket.getPort(); // get port number from client
            
            if (Instruction.toLowerCase().contains("index")) {
                output = "";
                for (File file : FileList) {
                    output += " " + file.getName(); // add all file_names together separated by space
                }
            }else {

                Instruction = modified_instruction(Instruction); //get rid off all the empty byte slots at the end of packet
                try {
                    output = file_reader_string(Instruction); // attempt to read input file name

                }catch (IOException e){
                    output = "Error: file not found"; // file name not found in the directory
                }
            }
            count = (output.length() + 27) / transfer_data_size + 1;
            output = "Total " + count + " packets will be sent" + output + "\n";
            byte[] sendData = output.getBytes();
            if (sendData.length <= transfer_data_size){
                DatagramPacket SendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                ServerSocket.send(SendPacket); // send packet directly to client
            }else{
                DatagramPacket SendPacket = new DatagramPacket(sendData, transfer_data_size, IPAddress, port);
                int bytesSent = 0;
                while (bytesSent < sendData.length) {
                    ServerSocket.send(SendPacket); // break down big files to chunks and send pieces each time
                    bytesSent += SendPacket.getLength();
                    int BytesToSend = sendData.length - bytesSent;
                    int size = (BytesToSend > transfer_data_size) ? transfer_data_size : BytesToSend;
                    SendPacket.setData(sendData, bytesSent, size); // remove empty slots of the last packet
                }
            }
        }
    }


    // read every line of the file
    public static String file_reader_string(String file) throws IOException {
        String file_path = "/Users/RogerF/Desktop/242/Exercise4/test/" + file;
        BufferedReader buffer_reader = new BufferedReader(new FileReader(file_path));
        String result = "\nOk, file was found. Here is the content: \n";
        String line;
        while ((line = buffer_reader.readLine()) != null) {
            result = result + line + "\n";
        }
        return result;
    }


    // change instruction string so that it doesn't contain empty byte slots
    public static String modified_instruction(String instruction) {
        for (int i = 0; i < instruction.length(); i++){
            if (instruction.charAt(i) == '.') {     // assume that all files in the path end with .txt
                return instruction.substring(0, i+4);
            }
        }
        return "Wrong file format";
    }
}