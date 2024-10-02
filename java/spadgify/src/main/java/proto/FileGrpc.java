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
  }

  private static final int METHODID_UPLOAD = 0;

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
              .build();
        }
      }
    }
    return result;
  }
}