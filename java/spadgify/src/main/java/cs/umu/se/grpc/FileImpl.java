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
import java.util.List;

import org.apache.logging.log4j.Logger;

public class FileImpl extends FileGrpc.FileImplBase {
//    private ClientBackend clientBackend;
    private Node node;
    private int m;
    private MediaUtil mediaUtil;
    private StorageBackend storageBackend;
    private String directory = "./media-spadgify/";
    private int chunkSize = 2048;
    private LRUCache lruCache;
    private Logger logger;

    public FileImpl(Node node, int cacheSize, Logger logger, StorageBackend storageBackend) {
        this.logger = logger;
        this.logger.info("File service is up for node: " + node);

        this.node = node;
        m = node.getM();
        lruCache = new LRUCache(cacheSize);
        mediaUtil = new MediaUtil(m);
        this.storageBackend = storageBackend;
    }

    @Override
    public StreamObserver<Chord.FileChunk> upload(StreamObserver<Chord.UploadStatus> resp) {
//        System.out.println("SERVER GOT upload REQUEST!");

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
//                    System.out.println("Artist: " + chordMediaInfo.getArtist());
                    i++;
                }

                try {
                    byteArrayOutputStream.write(value.getContent().toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("upload() onNext method caught exception!");
                }

            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
                logger.error("upload() onError method caught exception!");
            }

            @Override
            public void onCompleted() {
                // Create media info and hash song
                MediaInfo mediaInfo = mediaUtil.convertGRPCChordMediaInfoToMediaInfo(chordMediaInfo);
                int hash = mediaInfo.getHash();
                byte[] data = byteArrayOutputStream.toByteArray();
                String filePath = directory + mediaInfo.getIdentifierString() + ".mp3";

                int predIdentifier = node.getPredecessor().getMyIdentifier();
                int nodeIdentifier = node.getMyIdentifier();

                // Check which node that is responsible for the interval containing the hash value for the song
                Node destinationNode = mediaUtil.getResponsibleNodeForHash(node, hash);
                Song song = new Song(mediaInfo, filePath, data);
                if (destinationNode.equals(node) || mediaUtil.isNumberInIntervalExclusiveInclusive(hash, predIdentifier, nodeIdentifier)) { // Store in this node
                    try {
                        byteArrayOutputStream.close();
                        storageBackend.store(song);
                        message = message + " at node: " + node + " Song: \"" + song + "\" Hash: \"" + hash + "\"";
                        logger.info(message);
                    } catch (Exception e) {
                        message = e.getMessage();
                        logger.error(message);
                        success = false;
                    }

                } else { // Forward data to destination node
                    logger.info("Forwarding song \"" + song + "\" with hash: \"" +  hash + "\" to node: " + destinationNode);

                    String destIp = destinationNode.getMyIp();
                    int destPort = destinationNode.getMyPort();
                    int destM = destinationNode.getM();
                    ClientBackend clientBackend = new ClientBackend(destIp, destPort, "", destM);

                    try {
                        byteArrayOutputStream.close();
                        clientBackend.store(song);
                        message = "Forwarding Song \"" + song + " with Hash: \"" + hash + "\" to node: " + destinationNode;
                        logger.info(message);
                    } catch (Exception e) {
                        message = e.getMessage();
                        logger.error(message);
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
            logger.info("Found \"" + song + "\" in cache");
            responseObserver.onCompleted();
            return;
        }

        int predIdentifier = node.getPredecessor().getMyIdentifier();
        int nodeIdentifier = node.getMyIdentifier();

        Node destinationNode = mediaUtil.getResponsibleNodeForHash(node, hash);
        if (destinationNode.equals(node) || mediaUtil.isNumberInIntervalExclusiveInclusive(hash, predIdentifier, nodeIdentifier)) { // If song is stored in our backend
            song = storageBackend.retrieve(identifierString);

            if (song == null) {
                Chord.FileChunk response = Chord.FileChunk.newBuilder().build();
                logger.info("Song not found in node " + node);
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }
        } else { // Forward request to destination node
            String destIp = destinationNode.getMyIp();
            int destPort = destinationNode.getMyPort();
            int destM = destinationNode.getM();
            ClientBackend clientBackend = new ClientBackend(destIp, destPort, "", destM);
            song = clientBackend.retrieve(identifierString);

            if (song == null) {
                Chord.FileChunk response = Chord.FileChunk.newBuilder().build();
                logger.info("Song not found in destination node " + destinationNode);
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }
        }

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

        int predIdentifier = node.getPredecessor().getMyIdentifier();
        int nodeIdentifier = node.getMyIdentifier();

        if (destinationNode.equals(node) || mediaUtil.isNumberInIntervalExclusiveInclusive(hash, predIdentifier, nodeIdentifier)) { // If song is stored in our backend
            try {
                storageBackend.delete(identifierString);
                logger.info(message);
            } catch (Exception e) {
                success = false;
                message = "Delete of file: " + identifierString + " failed. Reason: " + e.getMessage();
                logger.error(message);
            }
        } else {
            String destIp = destinationNode.getMyIp();
            int destPort = destinationNode.getMyPort();
            int destM = destinationNode.getM();
            ClientBackend clientBackend = new ClientBackend(destIp, destPort, "", destM);
            clientBackend.delete(identifierString);
            logger.info(message);
        }

        Chord.DeleteStatus response = Chord.DeleteStatus.newBuilder().setMessage(message).setSuccess(success).build();
        resp.onNext(response);
        resp.onCompleted();
    }

    @Override
    public void listAllSongs(Chord.ListAllSongsRequest req, StreamObserver<Chord.ListAllSongsReply> resp) {
        int sourceIdentifier = (int) req.getIdentifier();
        int successorIdentifier = node.getSuccessor().getMyIdentifier();
        Node successor = node.getSuccessor();
        String firstNodeIdentifierString = req.getIp() + ":" + req.getPort();

        MediaInfo[] mediaInfos = null;
        MediaInfo[] theirMediaInfos = null;

        // Ask successor for its content
        if (successorIdentifier != sourceIdentifier) {
            System.out.println("Asking successor for its content!");
            String destIp = successor.getMyIp();
            int destPort = successor.getMyPort();
            int destM = successor.getM();
            ClientBackend clientBackend = new ClientBackend(destIp, destPort, "", destM);
            theirMediaInfos = clientBackend.listAllSongs(firstNodeIdentifierString);
        }

        // Add our content
        MediaInfo[] ourMediaInfos = storageBackend.listAllSongs(firstNodeIdentifierString);
        mediaInfos = mediaUtil.mergeMediaUtilsArrays(ourMediaInfos, theirMediaInfos);

        // send reply
        Chord.ListAllSongsReply.Builder responseBuilder = Chord.ListAllSongsReply.newBuilder();
        Chord.ListAllSongsReply reply;

        if (mediaInfos != null) {
            List<Chord.MediaInfo> chordMediaInfos = mediaUtil.convertMediaInfosToGRPCChordMediaInfos(mediaInfos);
            reply = responseBuilder.addAllMediaInfos(chordMediaInfos).build();
        } else {
            reply = responseBuilder.build();
        }

        resp.onNext(reply);
        resp.onCompleted();
    }

    @Override
    public void listNodeSongs(Chord.ListNodeSongsRequest req, StreamObserver<Chord.ListNodeSongsReply> resp) {
        MediaInfo[] mediaInfos = storageBackend.listNodeSongs();

        Chord.ListNodeSongsReply.Builder responseBuilder = Chord.ListNodeSongsReply.newBuilder();
        Chord.ListNodeSongsReply reply;

        if (mediaInfos != null) {
            List<Chord.MediaInfo> chordMediaInfos = mediaUtil.convertMediaInfosToGRPCChordMediaInfos(mediaInfos);
            reply = responseBuilder.addAllMediaInfos(chordMediaInfos).build();
        } else {
            reply = responseBuilder.build();
        }

        resp.onNext(reply);
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

        logger.info("Node: " + node + " sent chunks of file: \"" + song + ".mp3\"");
    }

}
