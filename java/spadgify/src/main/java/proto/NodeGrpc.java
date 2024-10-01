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
  private static volatile io.grpc.MethodDescriptor<proto.Chord.FindSuccessorRequestWIKI,
      proto.Chord.FindSuccessorReplyWIKI> getFindSuccessorWIKIMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "FindSuccessorWIKI",
      requestType = proto.Chord.FindSuccessorRequestWIKI.class,
      responseType = proto.Chord.FindSuccessorReplyWIKI.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.FindSuccessorRequestWIKI,
      proto.Chord.FindSuccessorReplyWIKI> getFindSuccessorWIKIMethod() {
    io.grpc.MethodDescriptor<proto.Chord.FindSuccessorRequestWIKI, proto.Chord.FindSuccessorReplyWIKI> getFindSuccessorWIKIMethod;
    if ((getFindSuccessorWIKIMethod = NodeGrpc.getFindSuccessorWIKIMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getFindSuccessorWIKIMethod = NodeGrpc.getFindSuccessorWIKIMethod) == null) {
          NodeGrpc.getFindSuccessorWIKIMethod = getFindSuccessorWIKIMethod =
              io.grpc.MethodDescriptor.<proto.Chord.FindSuccessorRequestWIKI, proto.Chord.FindSuccessorReplyWIKI>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "FindSuccessorWIKI"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.FindSuccessorRequestWIKI.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.FindSuccessorReplyWIKI.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("FindSuccessorWIKI"))
              .build();
        }
      }
    }
    return getFindSuccessorWIKIMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Chord.NotifyRequestWIKI,
      proto.Chord.NotifyReplyWIKI> getNotifyWIKIMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "NotifyWIKI",
      requestType = proto.Chord.NotifyRequestWIKI.class,
      responseType = proto.Chord.NotifyReplyWIKI.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.NotifyRequestWIKI,
      proto.Chord.NotifyReplyWIKI> getNotifyWIKIMethod() {
    io.grpc.MethodDescriptor<proto.Chord.NotifyRequestWIKI, proto.Chord.NotifyReplyWIKI> getNotifyWIKIMethod;
    if ((getNotifyWIKIMethod = NodeGrpc.getNotifyWIKIMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getNotifyWIKIMethod = NodeGrpc.getNotifyWIKIMethod) == null) {
          NodeGrpc.getNotifyWIKIMethod = getNotifyWIKIMethod =
              io.grpc.MethodDescriptor.<proto.Chord.NotifyRequestWIKI, proto.Chord.NotifyReplyWIKI>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "NotifyWIKI"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.NotifyRequestWIKI.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.NotifyReplyWIKI.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("NotifyWIKI"))
              .build();
        }
      }
    }
    return getNotifyWIKIMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Chord.PingNodeRequestWIKI,
      proto.Chord.PingNodeReplyWIKI> getPingNodeWIKIMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "PingNodeWIKI",
      requestType = proto.Chord.PingNodeRequestWIKI.class,
      responseType = proto.Chord.PingNodeReplyWIKI.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.PingNodeRequestWIKI,
      proto.Chord.PingNodeReplyWIKI> getPingNodeWIKIMethod() {
    io.grpc.MethodDescriptor<proto.Chord.PingNodeRequestWIKI, proto.Chord.PingNodeReplyWIKI> getPingNodeWIKIMethod;
    if ((getPingNodeWIKIMethod = NodeGrpc.getPingNodeWIKIMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getPingNodeWIKIMethod = NodeGrpc.getPingNodeWIKIMethod) == null) {
          NodeGrpc.getPingNodeWIKIMethod = getPingNodeWIKIMethod =
              io.grpc.MethodDescriptor.<proto.Chord.PingNodeRequestWIKI, proto.Chord.PingNodeReplyWIKI>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "PingNodeWIKI"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.PingNodeRequestWIKI.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.PingNodeReplyWIKI.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("PingNodeWIKI"))
              .build();
        }
      }
    }
    return getPingNodeWIKIMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Chord.GetPredecessorRequestWIKI,
      proto.Chord.GetPredecessorReplyWIKI> getGetPredecessorWIKIMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetPredecessorWIKI",
      requestType = proto.Chord.GetPredecessorRequestWIKI.class,
      responseType = proto.Chord.GetPredecessorReplyWIKI.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.GetPredecessorRequestWIKI,
      proto.Chord.GetPredecessorReplyWIKI> getGetPredecessorWIKIMethod() {
    io.grpc.MethodDescriptor<proto.Chord.GetPredecessorRequestWIKI, proto.Chord.GetPredecessorReplyWIKI> getGetPredecessorWIKIMethod;
    if ((getGetPredecessorWIKIMethod = NodeGrpc.getGetPredecessorWIKIMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getGetPredecessorWIKIMethod = NodeGrpc.getGetPredecessorWIKIMethod) == null) {
          NodeGrpc.getGetPredecessorWIKIMethod = getGetPredecessorWIKIMethod =
              io.grpc.MethodDescriptor.<proto.Chord.GetPredecessorRequestWIKI, proto.Chord.GetPredecessorReplyWIKI>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetPredecessorWIKI"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.GetPredecessorRequestWIKI.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.GetPredecessorReplyWIKI.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("GetPredecessorWIKI"))
              .build();
        }
      }
    }
    return getGetPredecessorWIKIMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Chord.SetPredecessorsSuccessorRequestWIKI,
      proto.Chord.SetPredecessorsSuccessorReplyWIKI> getSetPredecessorsSuccessorWIKIMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SetPredecessorsSuccessorWIKI",
      requestType = proto.Chord.SetPredecessorsSuccessorRequestWIKI.class,
      responseType = proto.Chord.SetPredecessorsSuccessorReplyWIKI.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.SetPredecessorsSuccessorRequestWIKI,
      proto.Chord.SetPredecessorsSuccessorReplyWIKI> getSetPredecessorsSuccessorWIKIMethod() {
    io.grpc.MethodDescriptor<proto.Chord.SetPredecessorsSuccessorRequestWIKI, proto.Chord.SetPredecessorsSuccessorReplyWIKI> getSetPredecessorsSuccessorWIKIMethod;
    if ((getSetPredecessorsSuccessorWIKIMethod = NodeGrpc.getSetPredecessorsSuccessorWIKIMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getSetPredecessorsSuccessorWIKIMethod = NodeGrpc.getSetPredecessorsSuccessorWIKIMethod) == null) {
          NodeGrpc.getSetPredecessorsSuccessorWIKIMethod = getSetPredecessorsSuccessorWIKIMethod =
              io.grpc.MethodDescriptor.<proto.Chord.SetPredecessorsSuccessorRequestWIKI, proto.Chord.SetPredecessorsSuccessorReplyWIKI>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SetPredecessorsSuccessorWIKI"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.SetPredecessorsSuccessorRequestWIKI.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.SetPredecessorsSuccessorReplyWIKI.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("SetPredecessorsSuccessorWIKI"))
              .build();
        }
      }
    }
    return getSetPredecessorsSuccessorWIKIMethod;
  }

  private static volatile io.grpc.MethodDescriptor<proto.Chord.SetSuccessorsPredecessorRequestWIKI,
      proto.Chord.SetSuccessorsPredecessorReplyWIKI> getSetSuccessorsPredecessorWIKIMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SetSuccessorsPredecessorWIKI",
      requestType = proto.Chord.SetSuccessorsPredecessorRequestWIKI.class,
      responseType = proto.Chord.SetSuccessorsPredecessorReplyWIKI.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<proto.Chord.SetSuccessorsPredecessorRequestWIKI,
      proto.Chord.SetSuccessorsPredecessorReplyWIKI> getSetSuccessorsPredecessorWIKIMethod() {
    io.grpc.MethodDescriptor<proto.Chord.SetSuccessorsPredecessorRequestWIKI, proto.Chord.SetSuccessorsPredecessorReplyWIKI> getSetSuccessorsPredecessorWIKIMethod;
    if ((getSetSuccessorsPredecessorWIKIMethod = NodeGrpc.getSetSuccessorsPredecessorWIKIMethod) == null) {
      synchronized (NodeGrpc.class) {
        if ((getSetSuccessorsPredecessorWIKIMethod = NodeGrpc.getSetSuccessorsPredecessorWIKIMethod) == null) {
          NodeGrpc.getSetSuccessorsPredecessorWIKIMethod = getSetSuccessorsPredecessorWIKIMethod =
              io.grpc.MethodDescriptor.<proto.Chord.SetSuccessorsPredecessorRequestWIKI, proto.Chord.SetSuccessorsPredecessorReplyWIKI>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SetSuccessorsPredecessorWIKI"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.SetSuccessorsPredecessorRequestWIKI.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  proto.Chord.SetSuccessorsPredecessorReplyWIKI.getDefaultInstance()))
              .setSchemaDescriptor(new NodeMethodDescriptorSupplier("SetSuccessorsPredecessorWIKI"))
              .build();
        }
      }
    }
    return getSetSuccessorsPredecessorWIKIMethod;
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
    default void findSuccessorWIKI(proto.Chord.FindSuccessorRequestWIKI request,
        io.grpc.stub.StreamObserver<proto.Chord.FindSuccessorReplyWIKI> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFindSuccessorWIKIMethod(), responseObserver);
    }

    /**
     */
    default void notifyWIKI(proto.Chord.NotifyRequestWIKI request,
        io.grpc.stub.StreamObserver<proto.Chord.NotifyReplyWIKI> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getNotifyWIKIMethod(), responseObserver);
    }

    /**
     */
    default void pingNodeWIKI(proto.Chord.PingNodeRequestWIKI request,
        io.grpc.stub.StreamObserver<proto.Chord.PingNodeReplyWIKI> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPingNodeWIKIMethod(), responseObserver);
    }

    /**
     */
    default void getPredecessorWIKI(proto.Chord.GetPredecessorRequestWIKI request,
        io.grpc.stub.StreamObserver<proto.Chord.GetPredecessorReplyWIKI> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetPredecessorWIKIMethod(), responseObserver);
    }

    /**
     */
    default void setPredecessorsSuccessorWIKI(proto.Chord.SetPredecessorsSuccessorRequestWIKI request,
        io.grpc.stub.StreamObserver<proto.Chord.SetPredecessorsSuccessorReplyWIKI> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSetPredecessorsSuccessorWIKIMethod(), responseObserver);
    }

    /**
     */
    default void setSuccessorsPredecessorWIKI(proto.Chord.SetSuccessorsPredecessorRequestWIKI request,
        io.grpc.stub.StreamObserver<proto.Chord.SetSuccessorsPredecessorReplyWIKI> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSetSuccessorsPredecessorWIKIMethod(), responseObserver);
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
    public void findSuccessorWIKI(proto.Chord.FindSuccessorRequestWIKI request,
        io.grpc.stub.StreamObserver<proto.Chord.FindSuccessorReplyWIKI> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFindSuccessorWIKIMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void notifyWIKI(proto.Chord.NotifyRequestWIKI request,
        io.grpc.stub.StreamObserver<proto.Chord.NotifyReplyWIKI> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getNotifyWIKIMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void pingNodeWIKI(proto.Chord.PingNodeRequestWIKI request,
        io.grpc.stub.StreamObserver<proto.Chord.PingNodeReplyWIKI> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPingNodeWIKIMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getPredecessorWIKI(proto.Chord.GetPredecessorRequestWIKI request,
        io.grpc.stub.StreamObserver<proto.Chord.GetPredecessorReplyWIKI> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetPredecessorWIKIMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setPredecessorsSuccessorWIKI(proto.Chord.SetPredecessorsSuccessorRequestWIKI request,
        io.grpc.stub.StreamObserver<proto.Chord.SetPredecessorsSuccessorReplyWIKI> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSetPredecessorsSuccessorWIKIMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setSuccessorsPredecessorWIKI(proto.Chord.SetSuccessorsPredecessorRequestWIKI request,
        io.grpc.stub.StreamObserver<proto.Chord.SetSuccessorsPredecessorReplyWIKI> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSetSuccessorsPredecessorWIKIMethod(), getCallOptions()), request, responseObserver);
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
    public proto.Chord.FindSuccessorReplyWIKI findSuccessorWIKI(proto.Chord.FindSuccessorRequestWIKI request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFindSuccessorWIKIMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Chord.NotifyReplyWIKI notifyWIKI(proto.Chord.NotifyRequestWIKI request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getNotifyWIKIMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Chord.PingNodeReplyWIKI pingNodeWIKI(proto.Chord.PingNodeRequestWIKI request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPingNodeWIKIMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Chord.GetPredecessorReplyWIKI getPredecessorWIKI(proto.Chord.GetPredecessorRequestWIKI request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetPredecessorWIKIMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Chord.SetPredecessorsSuccessorReplyWIKI setPredecessorsSuccessorWIKI(proto.Chord.SetPredecessorsSuccessorRequestWIKI request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSetPredecessorsSuccessorWIKIMethod(), getCallOptions(), request);
    }

    /**
     */
    public proto.Chord.SetSuccessorsPredecessorReplyWIKI setSuccessorsPredecessorWIKI(proto.Chord.SetSuccessorsPredecessorRequestWIKI request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSetSuccessorsPredecessorWIKIMethod(), getCallOptions(), request);
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
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.FindSuccessorReplyWIKI> findSuccessorWIKI(
        proto.Chord.FindSuccessorRequestWIKI request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFindSuccessorWIKIMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.NotifyReplyWIKI> notifyWIKI(
        proto.Chord.NotifyRequestWIKI request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getNotifyWIKIMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.PingNodeReplyWIKI> pingNodeWIKI(
        proto.Chord.PingNodeRequestWIKI request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPingNodeWIKIMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.GetPredecessorReplyWIKI> getPredecessorWIKI(
        proto.Chord.GetPredecessorRequestWIKI request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetPredecessorWIKIMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.SetPredecessorsSuccessorReplyWIKI> setPredecessorsSuccessorWIKI(
        proto.Chord.SetPredecessorsSuccessorRequestWIKI request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSetPredecessorsSuccessorWIKIMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<proto.Chord.SetSuccessorsPredecessorReplyWIKI> setSuccessorsPredecessorWIKI(
        proto.Chord.SetSuccessorsPredecessorRequestWIKI request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSetSuccessorsPredecessorWIKIMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_FIND_SUCCESSOR_WIKI = 0;
  private static final int METHODID_NOTIFY_WIKI = 1;
  private static final int METHODID_PING_NODE_WIKI = 2;
  private static final int METHODID_GET_PREDECESSOR_WIKI = 3;
  private static final int METHODID_SET_PREDECESSORS_SUCCESSOR_WIKI = 4;
  private static final int METHODID_SET_SUCCESSORS_PREDECESSOR_WIKI = 5;

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
        case METHODID_FIND_SUCCESSOR_WIKI:
          serviceImpl.findSuccessorWIKI((proto.Chord.FindSuccessorRequestWIKI) request,
              (io.grpc.stub.StreamObserver<proto.Chord.FindSuccessorReplyWIKI>) responseObserver);
          break;
        case METHODID_NOTIFY_WIKI:
          serviceImpl.notifyWIKI((proto.Chord.NotifyRequestWIKI) request,
              (io.grpc.stub.StreamObserver<proto.Chord.NotifyReplyWIKI>) responseObserver);
          break;
        case METHODID_PING_NODE_WIKI:
          serviceImpl.pingNodeWIKI((proto.Chord.PingNodeRequestWIKI) request,
              (io.grpc.stub.StreamObserver<proto.Chord.PingNodeReplyWIKI>) responseObserver);
          break;
        case METHODID_GET_PREDECESSOR_WIKI:
          serviceImpl.getPredecessorWIKI((proto.Chord.GetPredecessorRequestWIKI) request,
              (io.grpc.stub.StreamObserver<proto.Chord.GetPredecessorReplyWIKI>) responseObserver);
          break;
        case METHODID_SET_PREDECESSORS_SUCCESSOR_WIKI:
          serviceImpl.setPredecessorsSuccessorWIKI((proto.Chord.SetPredecessorsSuccessorRequestWIKI) request,
              (io.grpc.stub.StreamObserver<proto.Chord.SetPredecessorsSuccessorReplyWIKI>) responseObserver);
          break;
        case METHODID_SET_SUCCESSORS_PREDECESSOR_WIKI:
          serviceImpl.setSuccessorsPredecessorWIKI((proto.Chord.SetSuccessorsPredecessorRequestWIKI) request,
              (io.grpc.stub.StreamObserver<proto.Chord.SetSuccessorsPredecessorReplyWIKI>) responseObserver);
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
          getFindSuccessorWIKIMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.FindSuccessorRequestWIKI,
              proto.Chord.FindSuccessorReplyWIKI>(
                service, METHODID_FIND_SUCCESSOR_WIKI)))
        .addMethod(
          getNotifyWIKIMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.NotifyRequestWIKI,
              proto.Chord.NotifyReplyWIKI>(
                service, METHODID_NOTIFY_WIKI)))
        .addMethod(
          getPingNodeWIKIMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.PingNodeRequestWIKI,
              proto.Chord.PingNodeReplyWIKI>(
                service, METHODID_PING_NODE_WIKI)))
        .addMethod(
          getGetPredecessorWIKIMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.GetPredecessorRequestWIKI,
              proto.Chord.GetPredecessorReplyWIKI>(
                service, METHODID_GET_PREDECESSOR_WIKI)))
        .addMethod(
          getSetPredecessorsSuccessorWIKIMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.SetPredecessorsSuccessorRequestWIKI,
              proto.Chord.SetPredecessorsSuccessorReplyWIKI>(
                service, METHODID_SET_PREDECESSORS_SUCCESSOR_WIKI)))
        .addMethod(
          getSetSuccessorsPredecessorWIKIMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              proto.Chord.SetSuccessorsPredecessorRequestWIKI,
              proto.Chord.SetSuccessorsPredecessorReplyWIKI>(
                service, METHODID_SET_SUCCESSORS_PREDECESSOR_WIKI)))
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
              .addMethod(getFindSuccessorWIKIMethod())
              .addMethod(getNotifyWIKIMethod())
              .addMethod(getPingNodeWIKIMethod())
              .addMethod(getGetPredecessorWIKIMethod())
              .addMethod(getSetPredecessorsSuccessorWIKIMethod())
              .addMethod(getSetSuccessorsPredecessorWIKIMethod())
              .build();
        }
      }
    }
    return result;
  }
}
