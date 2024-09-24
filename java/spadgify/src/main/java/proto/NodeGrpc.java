package proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * ----------------------------------------------------
 * Service symbolizing the Messenger interface
 * ----------------------------------------------------
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: chord.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class NodeGrpc {

  private NodeGrpc() {}

  public static final java.lang.String SERVICE_NAME = "Node";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<proto.Chord.FindSuccessorRequest,
      proto.Chord.FindSuccessorReply> getFindSuccessorMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "FindSuccessor",
      requestType = proto.Chord.FindSuccessorRequest.class,
      responseType = proto.Chord.FindSuccessorReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.FindSuccessorRequest,
      proto.Chord.FindSuccessorReply> getFindSuccessorMethod() {
    io.grpc.MethodDescriptor<proto.Chord.FindSuccessorRequest, proto.Chord.FindSuccessorReply> getFindSuccessorMethod;
    if ((getFindSuccessorMethod = NodeGrpc.getFindSuccessorMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getFindSuccessorMethod = NodeGrpc.getFindSuccessorMethod) == null) {
          NodeGrpc.getFindSuccessorMethod = getFindSuccessorMethod =
              io.grpc.MethodDescriptor.<proto.Chord.FindSuccessorRequest, proto.Chord.FindSuccessorReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "FindSuccessor"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.FindSuccessorRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.FindSuccessorReply.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("FindSuccessor"))
              .build();
        }
      }
    }
    return getFindSuccessorMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Chord.FindPredecessorRequest,
      proto.Chord.FindPredecessorReply> getFindPredecessorMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "FindPredecessor",
      requestType = proto.Chord.FindPredecessorRequest.class,
      responseType = proto.Chord.FindPredecessorReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.FindPredecessorRequest,
      proto.Chord.FindPredecessorReply> getFindPredecessorMethod() {
    io.grpc.MethodDescriptor<proto.Chord.FindPredecessorRequest, proto.Chord.FindPredecessorReply> getFindPredecessorMethod;
    if ((getFindPredecessorMethod = NodeGrpc.getFindPredecessorMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getFindPredecessorMethod = NodeGrpc.getFindPredecessorMethod) == null) {
          NodeGrpc.getFindPredecessorMethod = getFindPredecessorMethod =
              io.grpc.MethodDescriptor.<proto.Chord.FindPredecessorRequest, proto.Chord.FindPredecessorReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "FindPredecessor"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.FindPredecessorRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.FindPredecessorReply.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("FindPredecessor"))
              .build();
        }
      }
    }
    return getFindPredecessorMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Chord.ClosestPrecedingFingerRequest,
      proto.Chord.ClosestPrecedingFingerReply> getClosestPrecedingFingerMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ClosestPrecedingFinger",
      requestType = proto.Chord.ClosestPrecedingFingerRequest.class,
      responseType = proto.Chord.ClosestPrecedingFingerReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.ClosestPrecedingFingerRequest,
      proto.Chord.ClosestPrecedingFingerReply> getClosestPrecedingFingerMethod() {
    io.grpc.MethodDescriptor<proto.Chord.ClosestPrecedingFingerRequest, proto.Chord.ClosestPrecedingFingerReply> getClosestPrecedingFingerMethod;
    if ((getClosestPrecedingFingerMethod = NodeGrpc.getClosestPrecedingFingerMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getClosestPrecedingFingerMethod = NodeGrpc.getClosestPrecedingFingerMethod) == null) {
          NodeGrpc.getClosestPrecedingFingerMethod = getClosestPrecedingFingerMethod =
              io.grpc.MethodDescriptor.<proto.Chord.ClosestPrecedingFingerRequest, proto.Chord.ClosestPrecedingFingerReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ClosestPrecedingFinger"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.ClosestPrecedingFingerRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.ClosestPrecedingFingerReply.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("ClosestPrecedingFinger"))
              .build();
        }
      }
    }
    return getClosestPrecedingFingerMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Chord.JoinRequest,
      proto.Chord.JoinReply> getJoinMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Join",
      requestType = proto.Chord.JoinRequest.class,
      responseType = proto.Chord.JoinReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.JoinRequest,
      proto.Chord.JoinReply> getJoinMethod() {
    io.grpc.MethodDescriptor<proto.Chord.JoinRequest, proto.Chord.JoinReply> getJoinMethod;
    if ((getJoinMethod = NodeGrpc.getJoinMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getJoinMethod = NodeGrpc.getJoinMethod) == null) {
          NodeGrpc.getJoinMethod = getJoinMethod =
              io.grpc.MethodDescriptor.<proto.Chord.JoinRequest, proto.Chord.JoinReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Join"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.JoinRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.JoinReply.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("Join"))
              .build();
        }
      }
    }
    return getJoinMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NodeStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NodeStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NodeStub>() {
        @java.lang.Override
        public NodeStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NodeStub(channel, callOptions);
        }
      };
    return NodeStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NodeBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NodeBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NodeBlockingStub>() {
        @java.lang.Override
        public NodeBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NodeBlockingStub(channel, callOptions);
        }
      };
    return NodeBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NodeFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NodeFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NodeFutureStub>() {
        @java.lang.Override
        public NodeFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NodeFutureStub(channel, callOptions);
        }
      };
    return NodeFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * ----------------------------------------------------
   * Service symbolizing the Messenger interface
   * ----------------------------------------------------
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void findSuccessor(proto.Chord.FindSuccessorRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.FindSuccessorReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFindSuccessorMethod(), responseObserver);
    }

    /**
     */
    default void findPredecessor(proto.Chord.FindPredecessorRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.FindPredecessorReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFindPredecessorMethod(), responseObserver);
    }

    /**
     */
    default void closestPrecedingFinger(proto.Chord.ClosestPrecedingFingerRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.ClosestPrecedingFingerReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getClosestPrecedingFingerMethod(), responseObserver);
    }

    /**
     */
    default void join(proto.Chord.JoinRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.JoinReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getJoinMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service Node.
   * <pre>
   * ----------------------------------------------------
   * Service symbolizing the Messenger interface
   * ----------------------------------------------------
   * </pre>
   */
  public static abstract class NodeImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return NodeGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service Node.
   * <pre>
   * ----------------------------------------------------
   * Service symbolizing the Messenger interface
   * ----------------------------------------------------
   * </pre>
   */
  public static final class NodeStub
      extends io.grpc.stub.AbstractAsyncStub<NodeStub> {
    private NodeStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NodeStub(channel, callOptions);
    }

    /**
     */
    public void findSuccessor(proto.Chord.FindSuccessorRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.FindSuccessorReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFindSuccessorMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void findPredecessor(proto.Chord.FindPredecessorRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.FindPredecessorReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFindPredecessorMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void closestPrecedingFinger(proto.Chord.ClosestPrecedingFingerRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.ClosestPrecedingFingerReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getClosestPrecedingFingerMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void join(proto.Chord.JoinRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.JoinReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getJoinMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service Node.
   * <pre>
   * ----------------------------------------------------
   * Service symbolizing the Messenger interface
   * ----------------------------------------------------
   * </pre>
   */
  public static final class NodeBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<NodeBlockingStub> {
    private NodeBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NodeBlockingStub(channel, callOptions);
    }

    /**
     */
    public proto.Chord.FindSuccessorReply findSuccessor(proto.Chord.FindSuccessorRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFindSuccessorMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Chord.FindPredecessorReply findPredecessor(proto.Chord.FindPredecessorRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFindPredecessorMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Chord.ClosestPrecedingFingerReply closestPrecedingFinger(proto.Chord.ClosestPrecedingFingerRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getClosestPrecedingFingerMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Chord.JoinReply join(proto.Chord.JoinRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getJoinMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service Node.
   * <pre>
   * ----------------------------------------------------
   * Service symbolizing the Messenger interface
   * ----------------------------------------------------
   * </pre>
   */
  public static final class NodeFutureStub
      extends io.grpc.stub.AbstractFutureStub<NodeFutureStub> {
    private NodeFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NodeFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.FindSuccessorReply> findSuccessor(
        proto.Chord.FindSuccessorRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFindSuccessorMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.FindPredecessorReply> findPredecessor(
        proto.Chord.FindPredecessorRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFindPredecessorMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.ClosestPrecedingFingerReply> closestPrecedingFinger(
        proto.Chord.ClosestPrecedingFingerRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getClosestPrecedingFingerMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.JoinReply> join(
        proto.Chord.JoinRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getJoinMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_FIND_SUCCESSOR = 0;
  private static final int METHODID_FIND_PREDECESSOR = 1;
  private static final int METHODID_CLOSEST_PRECEDING_FINGER = 2;
  private static final int METHODID_JOIN = 3;

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
        case METHODID_FIND_SUCCESSOR:
          serviceImpl.findSuccessor((proto.Chord.FindSuccessorRequest) request,
              (io.grpc.stub.StreamObserver<proto.Chord.FindSuccessorReply>) responseObserver);
          break;
        case METHODID_FIND_PREDECESSOR:
          serviceImpl.findPredecessor((proto.Chord.FindPredecessorRequest) request,
              (io.grpc.stub.StreamObserver<proto.Chord.FindPredecessorReply>) responseObserver);
          break;
        case METHODID_CLOSEST_PRECEDING_FINGER:
          serviceImpl.closestPrecedingFinger((proto.Chord.ClosestPrecedingFingerRequest) request,
              (io.grpc.stub.StreamObserver<proto.Chord.ClosestPrecedingFingerReply>) responseObserver);
          break;
        case METHODID_JOIN:
          serviceImpl.join((proto.Chord.JoinRequest) request,
              (io.grpc.stub.StreamObserver<proto.Chord.JoinReply>) responseObserver);
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
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getFindSuccessorMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.FindSuccessorRequest,
              proto.Chord.FindSuccessorReply>(
                service, METHODID_FIND_SUCCESSOR)))
        .addMethod(
          getFindPredecessorMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.FindPredecessorRequest,
              proto.Chord.FindPredecessorReply>(
                service, METHODID_FIND_PREDECESSOR)))
        .addMethod(
          getClosestPrecedingFingerMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.ClosestPrecedingFingerRequest,
              proto.Chord.ClosestPrecedingFingerReply>(
                service, METHODID_CLOSEST_PRECEDING_FINGER)))
        .addMethod(
          getJoinMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.JoinRequest,
              proto.Chord.JoinReply>(
                service, METHODID_JOIN)))
        .build();
  }

  private static abstract class NodeBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    NodeBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return proto.Chord.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Node");
    }
  }

  private static final class NodeFileDescriptorSupplier
      extends NodeBaseDescriptorSupplier {
    NodeFileDescriptorSupplier() {}
  }

  private static final class NodeMethodDescriptorSupplier
      extends NodeBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    NodeMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (NodeGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new NodeFileDescriptorSupplier())
              .addMethod(getFindSuccessorMethod())
              .addMethod(getFindPredecessorMethod())
              .addMethod(getClosestPrecedingFingerMethod())
              .addMethod(getJoinMethod())
              .build();
        }
      }
    }
    return result;
  }
}
