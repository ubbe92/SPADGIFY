package cs.umu.se.grpc;


import cs.umu.se.chord.FingerTableEntry;
import cs.umu.se.chord.Node;
import cs.umu.se.types.MediaInfo;
import cs.umu.se.util.MediaUtil;
import io.grpc.stub.StreamObserver;
import proto.Chord;
import proto.FileGrpc;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Target;

public class FileImpl extends FileGrpc.FileImplBase {
    private Node node;
    private int m;
    private MediaUtil mediaUtil;
    private String filePath = "/Users/antondacklin/Downloads/testMedia/output-music/testUpload.mp3";

    public FileImpl(Node node) {
        System.out.println("File service up!");
        this.node = node;
        m = node.getM();
        mediaUtil = new MediaUtil(m);
    }

    @Override
    public StreamObserver<Chord.FileChunk> upload(StreamObserver<Chord.UploadStatus> resp) {
        System.out.println("SERVER GOT upload REQUEST!");

        return new StreamObserver<Chord.FileChunk>() {
            ByteArrayOutputStream fileOutputStream = new ByteArrayOutputStream();
            Chord.MediaInfo chordMediaInfo;
            int i = 0;

            @Override
            public void onNext(Chord.FileChunk value) {
                if (i == 0) {
                    chordMediaInfo = value.getMediaInfo();
                    System.out.println("Artist: " + chordMediaInfo.getArtist());
                    i++;
                }

                try {
                    fileOutputStream.write(value.getContent().toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("upload() onNext method caught exception!");
                }

            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                System.out.println("upload() onError method!");
            }

            @Override
            public void onCompleted() {
                try {

                    // Create media info and hash song
                    MediaInfo mediaInfo = mediaUtil.convertGRPCChordMediaInfoToMediaInfo(chordMediaInfo);
                    int hash = mediaInfo.getHash();
                    System.out.println("Hash for song: " + mediaInfo.getSong() + " is: " + hash);

                    // Check which node that is responsible for the interval containing the hash value for the song
                    Node destinationNode = mediaUtil.getResponsibleNodeForHash(node, hash);

                    if (destinationNode.equals(node)) { // Store in this node
                        System.out.println("We will store song: " + mediaInfo.getSong() + " in this node: " + destinationNode);
                    } else { // Forward data to destination node
                        System.out.println("Forwarding song to node: " + destinationNode);
                    }

                    // If hash == this node -> store message in this node and save to disc

                    // else search for correct node and transfer file to that node.



                    // Save the accumulated data to a file
                    saveToFile(fileOutputStream.toByteArray(), filePath);

                    // Send the response to the client
                    resp.onNext(Chord.UploadStatus.newBuilder()
                            .setMessage("File uploaded and saved successfully")
                            .setSuccess(true)
                            .build());
                    resp.onCompleted();
                } catch (IOException e) {
                    resp.onError(e);
                }
            }

            private void saveToFile(byte[] fileData, String filePath) throws IOException {
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    fos.write(fileData);
                }
            }
        };
    }
}
