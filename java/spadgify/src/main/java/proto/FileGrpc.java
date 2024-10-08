package proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: chord.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class FileGrpc {

  private FileGrpc() {}

  public static final java.lang.String SERVICE_NAME = "File";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<proto.Chord.FileChunk,
      proto.Chord.UploadStatus> getUploadMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Upload",
      requestType = proto.Chord.FileChunk.class,
      responseType = proto.Chord.UploadStatus.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<proto.Chord.FileChunk,
      proto.Chord.UploadStatus> getUploadMethod() {
    io.grpc.MethodDescriptor<proto.Chord.FileChunk, proto.Chord.UploadStatus> getUploadMethod;
    if ((getUploadMethod = FileGrpc.getUploadMethod) == null) {
      synchronized (FileGrpc.class) {
        if ((getUploadMethod = FileGrpc.getUploadMethod) == null) {
          FileGrpc.getUploadMethod = getUploadMethod =
              io.grpc.MethodDescriptor.<proto.Chord.FileChunk, proto.Chord.UploadStatus>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Upload"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.FileChunk.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.UploadStatus.getDefaultInstance()))
              .setSchemaDescriptor(new FileMethodDescriptorSupplier("Upload"))
              .build();
        }
      }
    }
    return getUploadMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Chord.DownloadRequest,
      proto.Chord.FileChunk> getDownloadMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Download",
      requestType = proto.Chord.DownloadRequest.class,
      responseType = proto.Chord.FileChunk.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<proto.Chord.DownloadRequest,
      proto.Chord.FileChunk> getDownloadMethod() {
    io.grpc.MethodDescriptor<proto.Chord.DownloadRequest, proto.Chord.FileChunk> getDownloadMethod;
    if ((getDownloadMethod = FileGrpc.getDownloadMethod) == null) {
      synchronized (FileGrpc.class) {
        if ((getDownloadMethod = FileGrpc.getDownloadMethod) == null) {
          FileGrpc.getDownloadMethod = getDownloadMethod =
              io.grpc.MethodDescriptor.<proto.Chord.DownloadRequest, proto.Chord.FileChunk>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Download"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.DownloadRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.FileChunk.getDefaultInstance()))
              .setSchemaDescriptor(new FileMethodDescriptorSupplier("Download"))
              .build();
        }
      }
    }
    return getDownloadMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Chord.DeleteRequest,
      proto.Chord.DeleteStatus> getDeleteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Delete",
      requestType = proto.Chord.DeleteRequest.class,
      responseType = proto.Chord.DeleteStatus.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.DeleteRequest,
      proto.Chord.DeleteStatus> getDeleteMethod() {
    io.grpc.MethodDescriptor<proto.Chord.DeleteRequest, proto.Chord.DeleteStatus> getDeleteMethod;
    if ((getDeleteMethod = FileGrpc.getDeleteMethod) == null) {
      synchronized (FileGrpc.class) {
        if ((getDeleteMethod = FileGrpc.getDeleteMethod) == null) {
          FileGrpc.getDeleteMethod = getDeleteMethod =
              io.grpc.MethodDescriptor.<proto.Chord.DeleteRequest, proto.Chord.DeleteStatus>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Delete"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.DeleteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.DeleteStatus.getDefaultInstance()))
              .setSchemaDescriptor(new FileMethodDescriptorSupplier("Delete"))
              .build();
        }
      }
    }
    return getDeleteMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Chord.ListAllSongsRequest,
      proto.Chord.ListAllSongsReply> getListAllSongsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListAllSongs",
      requestType = proto.Chord.ListAllSongsRequest.class,
      responseType = proto.Chord.ListAllSongsReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.ListAllSongsRequest,
      proto.Chord.ListAllSongsReply> getListAllSongsMethod() {
    io.grpc.MethodDescriptor<proto.Chord.ListAllSongsRequest, proto.Chord.ListAllSongsReply> getListAllSongsMethod;
    if ((getListAllSongsMethod = FileGrpc.getListAllSongsMethod) == null) {
      synchronized (FileGrpc.class) {
        if ((getListAllSongsMethod = FileGrpc.getListAllSongsMethod) == null) {
          FileGrpc.getListAllSongsMethod = getListAllSongsMethod =
              io.grpc.MethodDescriptor.<proto.Chord.ListAllSongsRequest, proto.Chord.ListAllSongsReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListAllSongs"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.ListAllSongsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.ListAllSongsReply.getDefaultInstance()))
              .setSchemaDescriptor(new FileMethodDescriptorSupplier("ListAllSongs"))
              .build();
        }
      }
    }
    return getListAllSongsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Chord.ListNodeSongsRequest,
      proto.Chord.ListNodeSongsReply> getListNodeSongsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListNodeSongs",
      requestType = proto.Chord.ListNodeSongsRequest.class,
      responseType = proto.Chord.ListNodeSongsReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.ListNodeSongsRequest,
      proto.Chord.ListNodeSongsReply> getListNodeSongsMethod() {
    io.grpc.MethodDescriptor<proto.Chord.ListNodeSongsRequest, proto.Chord.ListNodeSongsReply> getListNodeSongsMethod;
    if ((getListNodeSongsMethod = FileGrpc.getListNodeSongsMethod) == null) {
      synchronized (FileGrpc.class) {
        if ((getListNodeSongsMethod = FileGrpc.getListNodeSongsMethod) == null) {
          FileGrpc.getListNodeSongsMethod = getListNodeSongsMethod =
              io.grpc.MethodDescriptor.<proto.Chord.ListNodeSongsRequest, proto.Chord.ListNodeSongsReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListNodeSongs"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.ListNodeSongsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.ListNodeSongsReply.getDefaultInstance()))
              .setSchemaDescriptor(new FileMethodDescriptorSupplier("ListNodeSongs"))
              .build();
        }
      }
    }
    return getListNodeSongsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Chord.ListSongsInIntervalRequest,
      proto.Chord.ListNodeSongsReply> getListSongsInIntervalMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListSongsInInterval",
      requestType = proto.Chord.ListSongsInIntervalRequest.class,
      responseType = proto.Chord.ListNodeSongsReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.ListSongsInIntervalRequest,
      proto.Chord.ListNodeSongsReply> getListSongsInIntervalMethod() {
    io.grpc.MethodDescriptor<proto.Chord.ListSongsInIntervalRequest, proto.Chord.ListNodeSongsReply> getListSongsInIntervalMethod;
    if ((getListSongsInIntervalMethod = FileGrpc.getListSongsInIntervalMethod) == null) {
      synchronized (FileGrpc.class) {
        if ((getListSongsInIntervalMethod = FileGrpc.getListSongsInIntervalMethod) == null) {
          FileGrpc.getListSongsInIntervalMethod = getListSongsInIntervalMethod =
              io.grpc.MethodDescriptor.<proto.Chord.ListSongsInIntervalRequest, proto.Chord.ListNodeSongsReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListSongsInInterval"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.ListSongsInIntervalRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.ListNodeSongsReply.getDefaultInstance()))
              .setSchemaDescriptor(new FileMethodDescriptorSupplier("ListSongsInInterval"))
              .build();
        }
      }
    }
    return getListSongsInIntervalMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Chord.DownloadRequest,
      proto.Chord.FileChunk> getDownloadFromNodeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DownloadFromNode",
      requestType = proto.Chord.DownloadRequest.class,
      responseType = proto.Chord.FileChunk.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<proto.Chord.DownloadRequest,
      proto.Chord.FileChunk> getDownloadFromNodeMethod() {
    io.grpc.MethodDescriptor<proto.Chord.DownloadRequest, proto.Chord.FileChunk> getDownloadFromNodeMethod;
    if ((getDownloadFromNodeMethod = FileGrpc.getDownloadFromNodeMethod) == null) {
      synchronized (FileGrpc.class) {
        if ((getDownloadFromNodeMethod = FileGrpc.getDownloadFromNodeMethod) == null) {
          FileGrpc.getDownloadFromNodeMethod = getDownloadFromNodeMethod =
              io.grpc.MethodDescriptor.<proto.Chord.DownloadRequest, proto.Chord.FileChunk>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DownloadFromNode"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.DownloadRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.FileChunk.getDefaultInstance()))
              .setSchemaDescriptor(new FileMethodDescriptorSupplier("DownloadFromNode"))
              .build();
        }
      }
    }
    return getDownloadFromNodeMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static FileStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FileStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FileStub>() {
        @java.lang.Override
        public FileStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FileStub(channel, callOptions);
        }
      };
    return FileStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static FileBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FileBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FileBlockingStub>() {
        @java.lang.Override
        public FileBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FileBlockingStub(channel, callOptions);
        }
      };
    return FileBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static FileFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<FileFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<FileFutureStub>() {
        @java.lang.Override
        public FileFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new FileFutureStub(channel, callOptions);
        }
      };
    return FileFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default io.grpc.stub.StreamObserver<proto.Chord.FileChunk> upload(
        io.grpc.stub.StreamObserver<proto.Chord.UploadStatus> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getUploadMethod(), responseObserver);
    }

    /**
     */
    default void download(proto.Chord.DownloadRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.FileChunk> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDownloadMethod(), responseObserver);
    }

    /**
     */
    default void delete(proto.Chord.DeleteRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.DeleteStatus> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteMethod(), responseObserver);
    }

    /**
     */
    default void listAllSongs(proto.Chord.ListAllSongsRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.ListAllSongsReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListAllSongsMethod(), responseObserver);
    }

    /**
     */
    default void listNodeSongs(proto.Chord.ListNodeSongsRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.ListNodeSongsReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListNodeSongsMethod(), responseObserver);
    }

    /**
     */
    default void listSongsInInterval(proto.Chord.ListSongsInIntervalRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.ListNodeSongsReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListSongsInIntervalMethod(), responseObserver);
    }

    /**
     */
    default void downloadFromNode(proto.Chord.DownloadRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.FileChunk> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDownloadFromNodeMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service File.
   */
  public static abstract class FileImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return FileGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service File.
   */
  public static final class FileStub
      extends io.grpc.stub.AbstractAsyncStub<FileStub> {
    private FileStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FileStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FileStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<proto.Chord.FileChunk> upload(
        io.grpc.stub.StreamObserver<proto.Chord.UploadStatus> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
          getChannel().newCall(getUploadMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public void download(proto.Chord.DownloadRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.FileChunk> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getDownloadMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void delete(proto.Chord.DeleteRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.DeleteStatus> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listAllSongs(proto.Chord.ListAllSongsRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.ListAllSongsReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListAllSongsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listNodeSongs(proto.Chord.ListNodeSongsRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.ListNodeSongsReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListNodeSongsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listSongsInInterval(proto.Chord.ListSongsInIntervalRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.ListNodeSongsReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListSongsInIntervalMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void downloadFromNode(proto.Chord.DownloadRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.FileChunk> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getDownloadFromNodeMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service File.
   */
  public static final class FileBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<FileBlockingStub> {
    private FileBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FileBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FileBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<proto.Chord.FileChunk> download(
        proto.Chord.DownloadRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getDownloadMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Chord.DeleteStatus delete(proto.Chord.DeleteRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Chord.ListAllSongsReply listAllSongs(proto.Chord.ListAllSongsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListAllSongsMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Chord.ListNodeSongsReply listNodeSongs(proto.Chord.ListNodeSongsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListNodeSongsMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Chord.ListNodeSongsReply listSongsInInterval(proto.Chord.ListSongsInIntervalRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListSongsInIntervalMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<proto.Chord.FileChunk> downloadFromNode(
        proto.Chord.DownloadRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getDownloadFromNodeMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service File.
   */
  public static final class FileFutureStub
      extends io.grpc.stub.AbstractFutureStub<FileFutureStub> {
    private FileFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected FileFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new FileFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.DeleteStatus> delete(
        proto.Chord.DeleteRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.ListAllSongsReply> listAllSongs(
        proto.Chord.ListAllSongsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListAllSongsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.ListNodeSongsReply> listNodeSongs(
        proto.Chord.ListNodeSongsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListNodeSongsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.ListNodeSongsReply> listSongsInInterval(
        proto.Chord.ListSongsInIntervalRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListSongsInIntervalMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_DOWNLOAD = 0;
  private static final int METHODID_DELETE = 1;
  private static final int METHODID_LIST_ALL_SONGS = 2;
  private static final int METHODID_LIST_NODE_SONGS = 3;
  private static final int METHODID_LIST_SONGS_IN_INTERVAL = 4;
  private static final int METHODID_DOWNLOAD_FROM_NODE = 5;
  private static final int METHODID_UPLOAD = 6;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_DOWNLOAD:
          serviceImpl.download((proto.Chord.DownloadRequest) request,
              (io.grpc.stub.StreamObserver<proto.Chord.FileChunk>) responseObserver);
          break;
        case METHODID_DELETE:
          serviceImpl.delete((proto.Chord.DeleteRequest) request,
              (io.grpc.stub.StreamObserver<proto.Chord.DeleteStatus>) responseObserver);
          break;
        case METHODID_LIST_ALL_SONGS:
          serviceImpl.listAllSongs((proto.Chord.ListAllSongsRequest) request,
              (io.grpc.stub.StreamObserver<proto.Chord.ListAllSongsReply>) responseObserver);
          break;
        case METHODID_LIST_NODE_SONGS:
          serviceImpl.listNodeSongs((proto.Chord.ListNodeSongsRequest) request,
              (io.grpc.stub.StreamObserver<proto.Chord.ListNodeSongsReply>) responseObserver);
          break;
        case METHODID_LIST_SONGS_IN_INTERVAL:
          serviceImpl.listSongsInInterval((proto.Chord.ListSongsInIntervalRequest) request,
              (io.grpc.stub.StreamObserver<proto.Chord.ListNodeSongsReply>) responseObserver);
          break;
        case METHODID_DOWNLOAD_FROM_NODE:
          serviceImpl.downloadFromNode((proto.Chord.DownloadRequest) request,
              (io.grpc.stub.StreamObserver<proto.Chord.FileChunk>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_UPLOAD:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.upload(
              (io.grpc.stub.StreamObserver<proto.Chord.UploadStatus>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getUploadMethod(),
          io.grpc.stub.ServerCalls.asyncClientStreamingCall(
            new MethodHandlers<
              proto.Chord.FileChunk,
              proto.Chord.UploadStatus>(
                service, METHODID_UPLOAD)))
        .addMethod(
          getDownloadMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              proto.Chord.DownloadRequest,
              proto.Chord.FileChunk>(
                service, METHODID_DOWNLOAD)))
        .addMethod(
          getDeleteMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.DeleteRequest,
              proto.Chord.DeleteStatus>(
                service, METHODID_DELETE)))
        .addMethod(
          getListAllSongsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.ListAllSongsRequest,
              proto.Chord.ListAllSongsReply>(
                service, METHODID_LIST_ALL_SONGS)))
        .addMethod(
          getListNodeSongsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.ListNodeSongsRequest,
              proto.Chord.ListNodeSongsReply>(
                service, METHODID_LIST_NODE_SONGS)))
        .addMethod(
          getListSongsInIntervalMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.ListSongsInIntervalRequest,
              proto.Chord.ListNodeSongsReply>(
                service, METHODID_LIST_SONGS_IN_INTERVAL)))
        .addMethod(
          getDownloadFromNodeMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              proto.Chord.DownloadRequest,
              proto.Chord.FileChunk>(
                service, METHODID_DOWNLOAD_FROM_NODE)))
        .build();
  }

  private static abstract class FileBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    FileBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return proto.Chord.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("File");
    }
  }

  private static final class FileFileDescriptorSupplier
      extends FileBaseDescriptorSupplier {
    FileFileDescriptorSupplier() {}
  }

  private static final class FileMethodDescriptorSupplier
      extends FileBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    FileMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (FileGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new FileFileDescriptorSupplier())
              .addMethod(getUploadMethod())
              .addMethod(getDownloadMethod())
              .addMethod(getDeleteMethod())
              .addMethod(getListAllSongsMethod())
              .addMethod(getListNodeSongsMethod())
              .addMethod(getListSongsInIntervalMethod())
              .addMethod(getDownloadFromNodeMethod())
              .build();
        }
      }
    }
    return result;
  }
}
