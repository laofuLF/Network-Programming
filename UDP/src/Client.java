import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.StringTokenizer;


public class Client{

    public static void main(String[] args) throws IOException {
        DatagramSocket ClientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName("localhost");
        Scanner sc = new Scanner(System.in);
        int transfer_data_size = 50; // datagram size for each transfer

        while (true) {
            byte[] sendData = new byte[transfer_data_size];

            System.out.println("Enter your instruction: (index or filename): ");
            String instruction = sc.nextLine(); // read user input

            sendData = instruction.getBytes();
            DatagramPacket SendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
            ClientSocket.send(SendPacket); // send user instruction to the server

            String result = "";
            String temp;
            int count = 0;
            while (true) {
                byte[] receiveData = new byte[transfer_data_size];
                DatagramPacket ReceivePacket = new DatagramPacket(receiveData, receiveData.length);
                ClientSocket.receive(ReceivePacket);
                temp = new String(ReceivePacket.getData());

                int length = ReceivePacket.getLength(); // check the length of each packet received eg: 50, 50, 20
                if (length < 50){
                    temp = temp.substring(0, length); // remove all the empty slots
                    result += temp;
                    count ++;
                    break; // stop receiving packet when the length is less than transfer data size, meaning that's the last packet
                } else {
                    result += temp;
                    count ++;
                }
            }

            if(instruction.toLowerCase().equals("index")) {
                StringTokenizer tokenized = new StringTokenizer(result);
                while (tokenized.hasMoreTokens()) {
                    String file_name = tokenized.nextToken();
                    System.out.println(file_name); // print each files name in the path
                }
                System.out.println("Total " + count + " packets were received");
            }else {
                System.out.println(result);
                System.out.println("Total " + count + " packets were received");
                ClientSocket.close(); // close the socket when file content is sent or file not found
                break;
            }
        }
    }
}