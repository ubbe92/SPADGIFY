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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;

/**
 * Implementation of File service responsible for handling file operations such as upload,
 * download, deletion, and song listing within a distributed network using the Chord protocol.
 */
public class FileImpl extends FileGrpc.FileImplBase {
    private final Node node;
    private final int m;
    private final MediaUtil mediaUtil;
    private final StorageBackend storageBackend;
    private final String directory = "./media-spadgify/";
    private final int chunkSize = 2048;
    private final LRUCache lruCache;
    private final Logger logger;

    public FileImpl(Node node, int cacheSize, Logger logger, StorageBackend storageBackend) {
        this.logger = logger;
        this.logger.info("File service is up for node: {}", node);

        this.node = node;
        m = node.getM();
        lruCache = new LRUCache(cacheSize);
        mediaUtil = new MediaUtil(m);
        this.storageBackend = storageBackend;
    }

    /**
     * Handles upload of file chunks from client.
     * Assembles chunks into a file and stores it, either locally or forwards it to the responsible node.
     *
     * @param resp StreamObserver to send upload status back to the client.
     * @return StreamObserver for receiving file chunks from the client.
     */
    @Override
    public StreamObserver<Chord.FileChunk> upload(StreamObserver<Chord.UploadStatus> resp) {
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
                    i++;
                }

                try {
                    byteArrayOutputStream.write(value.getContent().toByteArray());
                } catch (IOException e) {
                    logger.error("upload() onNext method caught exception! {}", Arrays.toString(e.getStackTrace()));
                }

            }

            @Override
            public void onError(Throwable t) {
                logger.error("upload() onError method caught exception! {}", Arrays.toString(t.getStackTrace()));
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

                // Store in this node
                if (destinationNode.equals(node) || mediaUtil.isNumberInIntervalExclusiveInclusive(hash, predIdentifier, nodeIdentifier)) {
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

                } else {
                    // Forward data to destination node
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

    /**
     * Handles the download of a song file requested by a client.
     * Checks if the song is available in the cache or storage backend and sends it to the client in chunks.
     * If the song is not found on the current node, the request is forwarded to the responsible node.
     *
     * @param req The request containing the identifier of the song to be downloaded.
     * @param responseObserver The observer to send song file chunks back to the client.
     */
    @Override
    public void download(Chord.DownloadRequest req, StreamObserver<Chord.FileChunk> responseObserver) {
        Song song;
        String identifierString = req.getIdentifierString();

        // Get hash for the song
        int hash =  Hash.getNodeIdentifierFromString(identifierString, m);

        // Do we have the song in our cache
        song = (Song) lruCache.get(identifierString);
        if (song != null) {
            sendChunks(song, responseObserver);
            logger.info("Found \"{}\" in cache", song);
            responseObserver.onCompleted();
            return;
        }

        int predIdentifier = node.getPredecessor().getMyIdentifier();
        int nodeIdentifier = node.getMyIdentifier();

        Node destinationNode = mediaUtil.getResponsibleNodeForHash(node, hash);

        // If song is stored in our backend
        if (destinationNode.equals(node) || mediaUtil.isNumberInIntervalExclusiveInclusive(hash, predIdentifier, nodeIdentifier)) {
            song = storageBackend.retrieve(identifierString);

            if (song == null) {
                Chord.FileChunk response = Chord.FileChunk.newBuilder().build();
                logger.info("Song not found in node {}", node);
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                return;
            }
        } else {
            // Forward request to destination node
            String destIp = destinationNode.getMyIp();
            int destPort = destinationNode.getMyPort();
            int destM = destinationNode.getM();
            ClientBackend clientBackend = new ClientBackend(destIp, destPort, "", destM);
            song = clientBackend.retrieve(identifierString);

            if (song == null) {
                Chord.FileChunk response = Chord.FileChunk.newBuilder().build();
                logger.info("Song not found in destination node {}", destinationNode);
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


    /**
     * Handles the deletion of a song file as requested by a client.
     * Determines if the current node is responsible for the song's hash and
     * deletes it from the storage backend or forwards the request to the responsible node.
     *
     * @param req The request containing the identifier of the song to be deleted.
     * @param resp StreamObserver to send the deletion status back to the client.
     */
    @Override
    public void delete(Chord.DeleteRequest req, StreamObserver<Chord.DeleteStatus> resp) {
        String identifierString = req.getIdentifierString();

        // Get hash for the song
        int hash =  Hash.getNodeIdentifierFromString(identifierString, m);
        boolean success = true;
        String message = "File: " + identifierString + " deleted successfully!";

        Node destinationNode = mediaUtil.getResponsibleNodeForHash(node, hash);
        Song song = (Song) lruCache.get(identifierString);

        if (song != null) {
            lruCache.remove(identifierString);
        }

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

    /**
     * Handles the listing of all songs in the distributed storage.
     * It gathers the song information from the current node and its successor,
     * merges the results, and sends them back to the client.
     *
     * @param req The request containing the identifier of the requesting node and its IP and port.
     * @param resp The observer to send the list of all songs back to the client.
     */
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


    /**
     * Lists all songs available in the network by traversing through each node
     * and collecting the songs stored on them. It handles the request from a client
     * and returns a consolidated list of songs.
     *
     * @param req The request object containing metadata for listing songs.
     * @param resp The StreamObserver used to send the list of songs back to the client.
     */
//    @Override
//    public void listAllSongs(Chord.ListAllSongsRequest req, StreamObserver<Chord.ListAllSongsReply> resp) {
//        // Create a set to keep track of already visited nodes
//        Set<String> visitedNodes = new HashSet<>();
//        Chord.ListAllSongsReply.Builder replyBuilder = Chord.ListAllSongsReply.newBuilder();
//        // Start from the current node
//        Node currentNode = this.node;
//
//        while (!visitedNodes.contains(currentNode.getMyIp() + ":" + currentNode.getMyPort()))
//        {
//            String nodeIdentifierString = currentNode.getMyIp() + ":" + currentNode.getMyPort();
//            // Mark the current node as visited
//            visitedNodes.add(nodeIdentifierString);
//
//            ClientBackend clientBackend = new ClientBackend(currentNode.getMyIp(), currentNode.getMyPort(), "", m);
//            // Fetch the songs from the current node either locally or remotely
////            MediaInfo[] mediaInfos = this.node.equals(currentNode) ? storageBackend.listNodeSongs() : clientBackend.listNodeSongs();
//            MediaInfo[] mediaInfos;
//
//            if (this.node.equals(currentNode)) {
//                logger.info("Retrieving local songs at node {}, successor: {}", currentNode, currentNode.getSuccessor());
//                mediaInfos = storageBackend.listNodeSongs();
//            } else {
//                logger.info("Retrieving remote songs at node {}, successor: {}", currentNode, currentNode.getSuccessor());
//                mediaInfos = clientBackend.listNodeSongs();
//            }
//
//            for (MediaInfo mediaInfo : mediaInfos) {
//                // Add songs to the reply
//                replyBuilder.addMediaInfos(mediaUtil.convertMediaInfoToGRPCChordMediaInfo(mediaInfo));
//            }
//
//            // this needs to be rpc call when more than 2 nodes because we don't know successors successor.
//            // Move to the successor node
//            currentNode = currentNode.getSuccessor();
//        }
//
//        // Send the accumulated list of songs to the client
//        resp.onNext(replyBuilder.build());
//        resp.onCompleted();
//    }

    /**
     * Retrieves a list of songs stored on the current node and sends this list back to the client.
     *
     * @param req The request object containing nothing.
     * @param resp The StreamObserver used to send the list of songs back to the client.
     */
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

    /**
     * Handles the transfer of songs to another node in the network. This method retrieves
     * the specified songs from the storage backend and sends them to the destination node.
     * Upon successful transfer, the songs are deleted from the current node's storage.
     *
     * @param request The request object containing the destination IP, port, and identifier of the songs to be transferred.
     * @param responseObserver The observer used to send the status of the transfer back to the requesting client.
     */
    @Override
    public void requestTransfer(Chord.RequestTransferRequest request, StreamObserver<Chord.RequestTransferReply> responseObserver) {
        String destIp = request.getIp();
        int destPort = (int) request.getPort();
        int identifier = (int) request.getIdentifier();

        Song[] songs = storageBackend.retrieve(identifier);

        ClientBackend clientBackend = new ClientBackend(destIp, destPort, "", m);
        for (Song s : songs) {
            logger.info("Sending song {}", s);
            clientBackend.transfer(s);
            storageBackend.delete(s.getIdentifierString());
        }

        Chord.RequestTransferReply.Builder responseBuilder = Chord.RequestTransferReply.newBuilder();
        Chord.RequestTransferReply reply;

        String message = "Transfer to node: " + destIp + ": " + destPort + " id: " + identifier + " complete!";
        reply = responseBuilder.setMessage(message).setSuccess(true).build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    /**
     * Handles the transfer of file chunks from client to the server.
     * This method receives chunks of a file from the client, assembles them, and stores
     * the complete file in the storage backend. It also sends the upload status back to the client.
     *
     * @param resp StreamObserver for sending upload status back to the client.
     * @return StreamObserver for receiving file chunks from the client.
     */
    @Override
    public StreamObserver<Chord.FileChunk> transfer(StreamObserver<Chord.UploadStatus> resp) {

        return new StreamObserver<Chord.FileChunk>() {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Chord.MediaInfo chordMediaInfo;
            boolean success = true;
            String message = "File transferred successfully";
            int i = 0;

            @Override
            public void onNext(Chord.FileChunk value) {
                if (i == 0) {
                    chordMediaInfo = value.getMediaInfo();
                    i++;
                }

                try {
                    byteArrayOutputStream.write(value.getContent().toByteArray());
                } catch (IOException e) {
                    logger.error("transfer() onNext method caught exception! {}", Arrays.toString(e.getStackTrace()));
                }

            }

            @Override
            public void onError(Throwable t) {
                logger.error("transfer() onError method caught exception! {}", Arrays.toString(t.getStackTrace()));
            }

            @Override
            public void onCompleted() {
                // Create media info and hash song
                MediaInfo mediaInfo = mediaUtil.convertGRPCChordMediaInfoToMediaInfo(chordMediaInfo);
                int hash = mediaInfo.getHash();
                byte[] data = byteArrayOutputStream.toByteArray();
                String filePath = directory + mediaInfo.getIdentifierString() + ".mp3";

                Song song = new Song(mediaInfo, filePath, data);
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
                // Send the response to the client
                resp.onNext(Chord.UploadStatus.newBuilder().setMessage(message).setSuccess(success).build());
                resp.onCompleted();
            }
        };
    }

    /**
     * Sends the song data in chunks through the responseObserver.
     *
     * @param song The Song object containing the data to be sent.
     * @param responseObserver The StreamObserver used to send chunked data responses.
     */
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
        logger.info("Node: {} sent chunks of file: \"{}.mp3\"", node, song);
    }

}
