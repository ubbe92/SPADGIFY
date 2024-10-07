package cs.umu.se.grpc;


import com.google.protobuf.ByteString;
import cs.umu.se.chord.Hash;
import cs.umu.se.chord.Node;
import cs.umu.se.client.ClientBackend;
import cs.umu.se.storage.StorageBackend;
import cs.umu.se.types.MediaInfo;
import cs.umu.se.types.Song;
import cs.umu.se.util.LRUCache;
import cs.umu.se.util.MediaUtil;
import io.grpc.stub.StreamObserver;
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
    private LRUCache lruCache;


    public FileImpl(Node node, int cacheSize) {
        System.out.println("File service up!");
        this.node = node;
        m = node.getM();
        lruCache = new LRUCache(cacheSize);
        mediaUtil = new MediaUtil(m);
        storageBackend = new StorageBackend(m);
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
                Song song = new Song(mediaInfo, filePath, data);
                if (destinationNode.equals(node)) { // Store in this node
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
                    String destIp = destinationNode.getMyIp();
                    int destPort = destinationNode.getMyPort();
                    int destM = destinationNode.getM();
                    clientBackend = new ClientBackend(destIp, destPort, "", destM);

                    try {
                        byteArrayOutputStream.close();
                        clientBackend.store(song);
                        message = message + " at node: " + node;
                    } catch (Exception e) {
                        message = e.getMessage();
                        success = false;
                    }
                }

                // Send the response to the client
                resp.onNext(Chord.UploadStatus.newBuilder().setMessage(message).setSuccess(success).build());
                resp.onCompleted();
            }
        };
    }

    @Override
    public void download(Chord.DownloadRequest req, StreamObserver<Chord.FileChunk> responseObserver) {
        Song song = null;
        String identifierString = req.getIdentifierString();

        // Get hash for the song
        int hash =  Hash.getNodeIdentifierFromString(identifierString, m);

        // Do we have the song in our cache
        song = (Song) lruCache.get(identifierString);
        if (song != null) {
            sendChunks(song, responseObserver);
            responseObserver.onCompleted();
            return;
        }

        Node destinationNode = mediaUtil.getResponsibleNodeForHash(node, hash);
        if (destinationNode.equals(node)) { // If song is stored in our backend
            song = storageBackend.retrieve(identifierString);

            if (song == null) {
                Chord.FileChunk response = Chord.FileChunk.newBuilder().build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }
        } else { // Forward request to destination node
            String destIp = destinationNode.getMyIp();
            int destPort = destinationNode.getMyPort();
            int destM = destinationNode.getM();
            clientBackend = new ClientBackend(destIp, destPort, "", destM);
            song = clientBackend.retrieve(identifierString);

            if (song == null) {
                Chord.FileChunk response = Chord.FileChunk.newBuilder().build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }
        }

//        byte[] data = song.getData();
//        Chord.MediaInfo chordMediaInfo = mediaUtil.convertMediaInfoToGRPCChordMediaInfo(song.getMediaInfo());
//        while (offset < data.length) {
//            int remaining = data.length - offset;
//            int currentChunkSize = Math.min(chunkSize, remaining);
//
//            ByteString chunk = ByteString.copyFrom(data, offset, currentChunkSize);
//            Chord.FileChunk response = Chord.FileChunk.newBuilder()
//                    .setContent(chunk)
//                    .setMediaInfo(chordMediaInfo)
//                    .build();
//            responseObserver.onNext(response);
//
//            offset += currentChunkSize;
//        }

        // Send the song in chunks
        sendChunks(song, responseObserver);

        // Make deep copy to store in cache
        Song songCopy = new Song(song);
        lruCache.put(songCopy.getIdentifierString(), songCopy);

        song.setData(null); // we don't need to hold this in memory, retrieve method will add the data back

        responseObserver.onCompleted();
    }


    @Override
    public void delete(Chord.DeleteRequest req, StreamObserver<Chord.DeleteStatus> resp) {
        String identifierString = req.getIdentifierString();

        // Get hash for the song
        int hash =  Hash.getNodeIdentifierFromString(identifierString, m);
        boolean success = true;
        String message = "File: " + identifierString + " deleted successfully!";


        Node destinationNode = mediaUtil.getResponsibleNodeForHash(node, hash);
        Song song = (Song) lruCache.get(identifierString);

        if (song != null)
            lruCache.remove(identifierString);

        if (destinationNode.equals(node)) { // If song is stored in our backend
            try {
                storageBackend.delete(identifierString);
            } catch (Exception e) {
                success = false;
                message = "Delete of file: " + identifierString + " failed. Reason: " + e.getMessage();
            }
        } else {
            String destIp = destinationNode.getMyIp();
            int destPort = destinationNode.getMyPort();
            int destM = destinationNode.getM();
            clientBackend = new ClientBackend(destIp, destPort, "", destM);
            clientBackend.delete(identifierString);
        }



        Chord.DeleteStatus response = Chord.DeleteStatus.newBuilder().setMessage(message).setSuccess(success).build();
        resp.onNext(response);
        resp.onCompleted();
    }


    private void sendChunks(Song song, StreamObserver<Chord.FileChunk> responseObserver) {
        int offset = 0;

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

}
