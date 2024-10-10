package cs.umu.se;

import cs.umu.se.types.Song;
import cs.umu.se.util.MediaUtil;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MusicStreamingServer {
    private byte[] mp3Data;  // Byte array holding the MP3 file data.

    public MusicStreamingServer(byte[] mp3Data) {
        this.mp3Data = mp3Data;
    }

    public void startServer() throws IOException {
        int port = 8080;  // You can choose any available port.
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Music streaming server started on port " + port);

        while (true) {
            try (Socket clientSocket = serverSocket.accept();
                 OutputStream outputStream = clientSocket.getOutputStream()) {

                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Send the MP3 data to the client
                outputStream.write(mp3Data);
                outputStream.flush();
                System.out.println("Music stream sent to client.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
        // Load your MP3 file into a byte[] array (you can adapt this)
//        File file = new File("path/to/your/musicfile.mp3");
//        byte[] mp3Data = new byte[(int) file.length()];

//        FileInputStream fileInputStream = new FileInputStream(file);
//        fileInputStream.read(mp3Data);
//        fileInputStream.close();


        int m = 3;
        MediaUtil mediaUtil = new MediaUtil(m);

        String inputDirectoryPath = "./../../testMedia/input-music";
        File[] files = mediaUtil.getAllFilesInDirectory(inputDirectoryPath);
        Song[] songs = mediaUtil.getSongsFromFiles(files);

        byte[] mp3Data = songs[0].getData();

        // Start the server
        MusicStreamingServer server = new MusicStreamingServer(mp3Data);
        server.startServer();
    }
}
