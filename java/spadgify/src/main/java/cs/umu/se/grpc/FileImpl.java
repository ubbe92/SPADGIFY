package cs.umu.se.grpc;


import com.google.protobuf.ByteString;
import cs.umu.se.chord.Node;
import cs.umu.se.client.ClientBackend;
import cs.umu.se.storage.StorageBackend;
import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;
import cs.umu.se.util.MediaUtil;
import io.grpc.stub.StreamObserver;
import org.restlet.Client;
import proto.Chord;
import proto.FileGrpc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FileImpl extends FileGrpc.FileImplBase {
    private ClientBackend clientBackend;
    private Node node;
    private int m;
    private MediaUtil mediaUtil;
    private StorageBackend storageBackend;
    private String directory = "./media-spadgify/";
    private int chunkSize = 2048;


    public FileImpl(Node node) {
        System.out.println("File service up!");
        this.node = node;
        m = node.getM();
        mediaUtil = new MediaUtil(m);
        storageBackend = new StorageBackend(m);
        this.clientBackend = new ClientBackend(node.getMyIp(), node.getMyPort(), directory, node.getM());
    }

    @Override
    public StreamObserver<Chord.FileChunk> upload(StreamObserver<Chord.UploadStatus> resp) {
        System.out.println("SERVER GOT upload REQUEST!");

        return new StreamObserver<Chord.FileChunk>() {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Chord.MediaInfo chordMediaInfo;
            boolean success = true;
            String message = "File uploaded and saved successfully";
            int i = 0;

            @Override
            public void onNext(Chord.FileChunk value) {
                if (i == 0) {
                    chordMediaInfo = value.getMediaInfo();
                    System.out.println("Artist: " + chordMediaInfo.getArtist());
                    i++;
                }

                try {
                    byteArrayOutputStream.write(value.getContent().toByteArray());
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
                // Create media info and hash song
                MediaInfo mediaInfo = mediaUtil.convertGRPCChordMediaInfoToMediaInfo(chordMediaInfo);
                int hash = mediaInfo.getHash();
                byte[] data = byteArrayOutputStream.toByteArray();
                String filePath = directory + mediaInfo.getIdentifierString() + ".mp3";

                System.out.println("Hash for song: " + mediaInfo.getSong() + " is: " + hash);

                // Check which node that is responsible for the interval containing the hash value for the song
                Node destinationNode = mediaUtil.getResponsibleNodeForHash(node, hash);

                if (destinationNode.equals(node)) { // Store in this node
                    Song song = new Song(mediaInfo, filePath, data);
                    try {
                        byteArrayOutputStream.close();
                        storageBackend.store(song);
                        message = message + " at node: " + node;
                    } catch (Exception e) {
                        message = e.getMessage();
                        success = false;
                    }

                } else { // Forward data to destination node
                    System.out.println("Forwarding song to node: " + destinationNode);

                }

                // If hash == this node -> store message in this node and save to disc

                // else search for correct node and transfer file to that node.


                // Save the accumulated data to a file
//                    saveToFile(fileOutputStream.toByteArray(), filePath);


                // Send the response to the client
                resp.onNext(Chord.UploadStatus.newBuilder().setMessage(message).setSuccess(success).build());
                resp.onCompleted();
            }
        };
    }

    @Override
    public void download(Chord.DownloadRequest req, StreamObserver<Chord.FileChunk> responseObserver) {
        int offset = 0;
        String identifierString = req.getIdentifierString();

        Song song = storageBackend.retrieve(identifierString);

        if (song == null) {
            Chord.FileChunk response = Chord.FileChunk.newBuilder().build();
            responseObserver.onNext(response);
        } else {
            byte[] data = song.getData();
            Chord.MediaInfo chordMediaInfo = mediaUtil.convertMediaInfoToGRPCChordMediaInfo(song.getMediaInfo());
            while (offset < data.length) {
                int remaining = data.length - offset;
                int currentChunkSize = Math.min(chunkSize, remaining);

                ByteString chunk = ByteString.copyFrom(data, offset, currentChunkSize);
                Chord.FileChunk response = Chord.FileChunk.newBuilder()
                        .setContent(chunk)
                        .setMediaInfo(chordMediaInfo)
                        .build();
                responseObserver.onNext(response);

                offset += currentChunkSize;
            }

        }

        responseObserver.onCompleted();
    }


    @Override
    public void delete(Chord.DeleteRequest req, StreamObserver<Chord.DeleteStatus> resp) {
        String identifierString = req.getIdentifierString();
        boolean success = true;
        String message = "File: " + identifierString + " deleted successfully!";

        try {
            storageBackend.delete(identifierString);
        } catch (Exception e) {
            success = false;
            message = "Delete of file: " + identifierString + " failed. Reason: " + e.getMessage();
        }

        Chord.DeleteStatus response = Chord.DeleteStatus.newBuilder().setMessage(message).setSuccess(success).build();
        resp.onNext(response);
        resp.onCompleted();
    }

}
