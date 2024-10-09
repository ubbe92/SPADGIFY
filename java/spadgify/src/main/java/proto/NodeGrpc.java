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

  private static volatile io.grpc.MethodDescriptor<proto.Chord.NotifyRequest,
      proto.Chord.NotifyReply> getNotifyMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Notify",
      requestType = proto.Chord.NotifyRequest.class,
      responseType = proto.Chord.NotifyReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.NotifyRequest,
      proto.Chord.NotifyReply> getNotifyMethod() {
    io.grpc.MethodDescriptor<proto.Chord.NotifyRequest, proto.Chord.NotifyReply> getNotifyMethod;
    if ((getNotifyMethod = NodeGrpc.getNotifyMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getNotifyMethod = NodeGrpc.getNotifyMethod) == null) {
          NodeGrpc.getNotifyMethod = getNotifyMethod =
              io.grpc.MethodDescriptor.<proto.Chord.NotifyRequest, proto.Chord.NotifyReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Notify"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.NotifyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.NotifyReply.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("Notify"))
              .build();
        }
      }
    }
    return getNotifyMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Chord.PingNodeRequest,
      proto.Chord.PingNodeReply> getPingNodeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "PingNode",
      requestType = proto.Chord.PingNodeRequest.class,
      responseType = proto.Chord.PingNodeReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.PingNodeRequest,
      proto.Chord.PingNodeReply> getPingNodeMethod() {
    io.grpc.MethodDescriptor<proto.Chord.PingNodeRequest, proto.Chord.PingNodeReply> getPingNodeMethod;
    if ((getPingNodeMethod = NodeGrpc.getPingNodeMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getPingNodeMethod = NodeGrpc.getPingNodeMethod) == null) {
          NodeGrpc.getPingNodeMethod = getPingNodeMethod =
              io.grpc.MethodDescriptor.<proto.Chord.PingNodeRequest, proto.Chord.PingNodeReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "PingNode"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.PingNodeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.PingNodeReply.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("PingNode"))
              .build();
        }
      }
    }
    return getPingNodeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Chord.GetPredecessorRequest,
      proto.Chord.GetPredecessorReply> getGetPredecessorMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetPredecessor",
      requestType = proto.Chord.GetPredecessorRequest.class,
      responseType = proto.Chord.GetPredecessorReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.GetPredecessorRequest,
      proto.Chord.GetPredecessorReply> getGetPredecessorMethod() {
    io.grpc.MethodDescriptor<proto.Chord.GetPredecessorRequest, proto.Chord.GetPredecessorReply> getGetPredecessorMethod;
    if ((getGetPredecessorMethod = NodeGrpc.getGetPredecessorMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getGetPredecessorMethod = NodeGrpc.getGetPredecessorMethod) == null) {
          NodeGrpc.getGetPredecessorMethod = getGetPredecessorMethod =
              io.grpc.MethodDescriptor.<proto.Chord.GetPredecessorRequest, proto.Chord.GetPredecessorReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetPredecessor"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.GetPredecessorRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.GetPredecessorReply.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("GetPredecessor"))
              .build();
        }
      }
    }
    return getGetPredecessorMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Chord.SetPredecessorsSuccessorRequest,
      proto.Chord.SetPredecessorsSuccessorReply> getSetPredecessorsSuccessorMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SetPredecessorsSuccessor",
      requestType = proto.Chord.SetPredecessorsSuccessorRequest.class,
      responseType = proto.Chord.SetPredecessorsSuccessorReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.SetPredecessorsSuccessorRequest,
      proto.Chord.SetPredecessorsSuccessorReply> getSetPredecessorsSuccessorMethod() {
    io.grpc.MethodDescriptor<proto.Chord.SetPredecessorsSuccessorRequest, proto.Chord.SetPredecessorsSuccessorReply> getSetPredecessorsSuccessorMethod;
    if ((getSetPredecessorsSuccessorMethod = NodeGrpc.getSetPredecessorsSuccessorMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getSetPredecessorsSuccessorMethod = NodeGrpc.getSetPredecessorsSuccessorMethod) == null) {
          NodeGrpc.getSetPredecessorsSuccessorMethod = getSetPredecessorsSuccessorMethod =
              io.grpc.MethodDescriptor.<proto.Chord.SetPredecessorsSuccessorRequest, proto.Chord.SetPredecessorsSuccessorReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SetPredecessorsSuccessor"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.SetPredecessorsSuccessorRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.SetPredecessorsSuccessorReply.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("SetPredecessorsSuccessor"))
              .build();
        }
      }
    }
    return getSetPredecessorsSuccessorMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Chord.SetSuccessorsPredecessorRequest,
      proto.Chord.SetSuccessorsPredecessorReply> getSetSuccessorsPredecessorMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SetSuccessorsPredecessor",
      requestType = proto.Chord.SetSuccessorsPredecessorRequest.class,
      responseType = proto.Chord.SetSuccessorsPredecessorReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.SetSuccessorsPredecessorRequest,
      proto.Chord.SetSuccessorsPredecessorReply> getSetSuccessorsPredecessorMethod() {
    io.grpc.MethodDescriptor<proto.Chord.SetSuccessorsPredecessorRequest, proto.Chord.SetSuccessorsPredecessorReply> getSetSuccessorsPredecessorMethod;
    if ((getSetSuccessorsPredecessorMethod = NodeGrpc.getSetSuccessorsPredecessorMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getSetSuccessorsPredecessorMethod = NodeGrpc.getSetSuccessorsPredecessorMethod) == null) {
          NodeGrpc.getSetSuccessorsPredecessorMethod = getSetSuccessorsPredecessorMethod =
              io.grpc.MethodDescriptor.<proto.Chord.SetSuccessorsPredecessorRequest, proto.Chord.SetSuccessorsPredecessorReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SetSuccessorsPredecessor"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.SetSuccessorsPredecessorRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.SetSuccessorsPredecessorReply.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("SetSuccessorsPredecessor"))
              .build();
        }
      }
    }
    return getSetSuccessorsPredecessorMethod;
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
    default void notify(proto.Chord.NotifyRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.NotifyReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getNotifyMethod(), responseObserver);
    }

    /**
     */
    default void pingNode(proto.Chord.PingNodeRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.PingNodeReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPingNodeMethod(), responseObserver);
    }

    /**
     */
    default void getPredecessor(proto.Chord.GetPredecessorRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.GetPredecessorReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetPredecessorMethod(), responseObserver);
    }

    /**
     */
    default void setPredecessorsSuccessor(proto.Chord.SetPredecessorsSuccessorRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.SetPredecessorsSuccessorReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSetPredecessorsSuccessorMethod(), responseObserver);
    }

    /**
     */
    default void setSuccessorsPredecessor(proto.Chord.SetSuccessorsPredecessorRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.SetSuccessorsPredecessorReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSetSuccessorsPredecessorMethod(), responseObserver);
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
    public void notify(proto.Chord.NotifyRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.NotifyReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getNotifyMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void pingNode(proto.Chord.PingNodeRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.PingNodeReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPingNodeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getPredecessor(proto.Chord.GetPredecessorRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.GetPredecessorReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetPredecessorMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setPredecessorsSuccessor(proto.Chord.SetPredecessorsSuccessorRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.SetPredecessorsSuccessorReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSetPredecessorsSuccessorMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setSuccessorsPredecessor(proto.Chord.SetSuccessorsPredecessorRequest request,
        io.grpc.stub.StreamObserver<proto.Chord.SetSuccessorsPredecessorReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSetSuccessorsPredecessorMethod(), getCallOptions()), request, responseObserver);
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
    public proto.Chord.NotifyReply notify(proto.Chord.NotifyRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getNotifyMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Chord.PingNodeReply pingNode(proto.Chord.PingNodeRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPingNodeMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Chord.GetPredecessorReply getPredecessor(proto.Chord.GetPredecessorRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetPredecessorMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Chord.SetPredecessorsSuccessorReply setPredecessorsSuccessor(proto.Chord.SetPredecessorsSuccessorRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSetPredecessorsSuccessorMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Chord.SetSuccessorsPredecessorReply setSuccessorsPredecessor(proto.Chord.SetSuccessorsPredecessorRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSetSuccessorsPredecessorMethod(), getCallOptions(), request);
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
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.NotifyReply> notify(
        proto.Chord.NotifyRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getNotifyMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.PingNodeReply> pingNode(
        proto.Chord.PingNodeRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPingNodeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.GetPredecessorReply> getPredecessor(
        proto.Chord.GetPredecessorRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetPredecessorMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.SetPredecessorsSuccessorReply> setPredecessorsSuccessor(
        proto.Chord.SetPredecessorsSuccessorRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSetPredecessorsSuccessorMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.SetSuccessorsPredecessorReply> setSuccessorsPredecessor(
        proto.Chord.SetSuccessorsPredecessorRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSetSuccessorsPredecessorMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_FIND_SUCCESSOR = 0;
  private static final int METHODID_NOTIFY = 1;
  private static final int METHODID_PING_NODE = 2;
  private static final int METHODID_GET_PREDECESSOR = 3;
  private static final int METHODID_SET_PREDECESSORS_SUCCESSOR = 4;
  private static final int METHODID_SET_SUCCESSORS_PREDECESSOR = 5;

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
        case METHODID_NOTIFY:
          serviceImpl.notify((proto.Chord.NotifyRequest) request,
              (io.grpc.stub.StreamObserver<proto.Chord.NotifyReply>) responseObserver);
          break;
        case METHODID_PING_NODE:
          serviceImpl.pingNode((proto.Chord.PingNodeRequest) request,
              (io.grpc.stub.StreamObserver<proto.Chord.PingNodeReply>) responseObserver);
          break;
        case METHODID_GET_PREDECESSOR:
          serviceImpl.getPredecessor((proto.Chord.GetPredecessorRequest) request,
              (io.grpc.stub.StreamObserver<proto.Chord.GetPredecessorReply>) responseObserver);
          break;
        case METHODID_SET_PREDECESSORS_SUCCESSOR:
          serviceImpl.setPredecessorsSuccessor((proto.Chord.SetPredecessorsSuccessorRequest) request,
              (io.grpc.stub.StreamObserver<proto.Chord.SetPredecessorsSuccessorReply>) responseObserver);
          break;
        case METHODID_SET_SUCCESSORS_PREDECESSOR:
          serviceImpl.setSuccessorsPredecessor((proto.Chord.SetSuccessorsPredecessorRequest) request,
              (io.grpc.stub.StreamObserver<proto.Chord.SetSuccessorsPredecessorReply>) responseObserver);
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
          getNotifyMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.NotifyRequest,
              proto.Chord.NotifyReply>(
                service, METHODID_NOTIFY)))
        .addMethod(
          getPingNodeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.PingNodeRequest,
              proto.Chord.PingNodeReply>(
                service, METHODID_PING_NODE)))
        .addMethod(
          getGetPredecessorMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.GetPredecessorRequest,
              proto.Chord.GetPredecessorReply>(
                service, METHODID_GET_PREDECESSOR)))
        .addMethod(
          getSetPredecessorsSuccessorMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.SetPredecessorsSuccessorRequest,
              proto.Chord.SetPredecessorsSuccessorReply>(
                service, METHODID_SET_PREDECESSORS_SUCCESSOR)))
        .addMethod(
          getSetSuccessorsPredecessorMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.SetSuccessorsPredecessorRequest,
              proto.Chord.SetSuccessorsPredecessorReply>(
                service, METHODID_SET_SUCCESSORS_PREDECESSOR)))
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
              .addMethod(getNotifyMethod())
              .addMethod(getPingNodeMethod())
              .addMethod(getGetPredecessorMethod())
              .addMethod(getSetPredecessorsSuccessorMethod())
              .addMethod(getSetSuccessorsPredecessorMethod())
              .build();
        }
      }
    }
    return result;
  }
}
