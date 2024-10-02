package cs.umu.se.grpc;


import io.grpc.stub.StreamObserver;
import proto.Chord;
import proto.FileGrpc;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileImpl extends FileGrpc.FileImplBase {

    private String filePath = "/Users/antondacklin/Downloads/testMedia/output-music/testUpload.mp3";
    public FileImpl() {
        System.out.println("File service up!");
    }

    @Override
    public StreamObserver<Chord.FileChunk> upload(StreamObserver<Chord.UploadStatus> resp) {
        System.out.println("SERVER GOT upload REQUEST!");

        return new StreamObserver<Chord.FileChunk>() {
            ByteArrayOutputStream fileOutputStream = new ByteArrayOutputStream();
            int i = 0;

            @Override
            public void onNext(Chord.FileChunk value) {
                if (i == 0) {
                    Chord.MediaInfo mediaInfo = value.getMediaInfo();
                    System.out.println("Artist: " + mediaInfo.getArtist());
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
