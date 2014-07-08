// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Pose2D.proto

package org.robocup_logistics.llsf_msgs;

public final class Pose2DProtos {
  private Pose2DProtos() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface Pose2DOrBuilder
      extends com.google.protobuf.MessageOrBuilder {

    // required .llsf_msgs.Time timestamp = 1;
    /**
     * <code>required .llsf_msgs.Time timestamp = 1;</code>
     *
     * <pre>
     * Time when this pose was measured
     * </pre>
     */
    boolean hasTimestamp();
    /**
     * <code>required .llsf_msgs.Time timestamp = 1;</code>
     *
     * <pre>
     * Time when this pose was measured
     * </pre>
     */
    org.robocup_logistics.llsf_msgs.TimeProtos.Time getTimestamp();
    /**
     * <code>required .llsf_msgs.Time timestamp = 1;</code>
     *
     * <pre>
     * Time when this pose was measured
     * </pre>
     */
    org.robocup_logistics.llsf_msgs.TimeProtos.TimeOrBuilder getTimestampOrBuilder();

    // required float x = 2;
    /**
     * <code>required float x = 2;</code>
     *
     * <pre>
     * X/Y coordinates in meters
     * </pre>
     */
    boolean hasX();
    /**
     * <code>required float x = 2;</code>
     *
     * <pre>
     * X/Y coordinates in meters
     * </pre>
     */
    float getX();

    // required float y = 3;
    /**
     * <code>required float y = 3;</code>
     */
    boolean hasY();
    /**
     * <code>required float y = 3;</code>
     */
    float getY();

    // required float ori = 4;
    /**
     * <code>required float ori = 4;</code>
     *
     * <pre>
     * Orientation in rad
     * </pre>
     */
    boolean hasOri();
    /**
     * <code>required float ori = 4;</code>
     *
     * <pre>
     * Orientation in rad
     * </pre>
     */
    float getOri();
  }
  /**
   * Protobuf type {@code llsf_msgs.Pose2D}
   *
   * <pre>
   * Pose information on 2D map
   * Data is relative to the LLSF field frame
   * </pre>
   */
  public static final class Pose2D extends
      com.google.protobuf.GeneratedMessage
      implements Pose2DOrBuilder {
    // Use Pose2D.newBuilder() to construct.
    private Pose2D(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
      super(builder);
      this.unknownFields = builder.getUnknownFields();
    }
    private Pose2D(boolean noInit) { this.unknownFields = com.google.protobuf.UnknownFieldSet.getDefaultInstance(); }

    private static final Pose2D defaultInstance;
    public static Pose2D getDefaultInstance() {
      return defaultInstance;
    }

    public Pose2D getDefaultInstanceForType() {
      return defaultInstance;
    }

    private final com.google.protobuf.UnknownFieldSet unknownFields;
    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
        getUnknownFields() {
      return this.unknownFields;
    }
    private Pose2D(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      initFields();
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              org.robocup_logistics.llsf_msgs.TimeProtos.Time.Builder subBuilder = null;
              if (((bitField0_ & 0x00000001) == 0x00000001)) {
                subBuilder = timestamp_.toBuilder();
              }
              timestamp_ = input.readMessage(org.robocup_logistics.llsf_msgs.TimeProtos.Time.PARSER, extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(timestamp_);
                timestamp_ = subBuilder.buildPartial();
              }
              bitField0_ |= 0x00000001;
              break;
            }
            case 21: {
              bitField0_ |= 0x00000002;
              x_ = input.readFloat();
              break;
            }
            case 29: {
              bitField0_ |= 0x00000004;
              y_ = input.readFloat();
              break;
            }
            case 37: {
              bitField0_ |= 0x00000008;
              ori_ = input.readFloat();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e.getMessage()).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return org.robocup_logistics.llsf_msgs.Pose2DProtos.internal_static_llsf_msgs_Pose2D_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return org.robocup_logistics.llsf_msgs.Pose2DProtos.internal_static_llsf_msgs_Pose2D_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D.class, org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D.Builder.class);
    }

    public static com.google.protobuf.Parser<Pose2D> PARSER =
        new com.google.protobuf.AbstractParser<Pose2D>() {
      public Pose2D parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new Pose2D(input, extensionRegistry);
      }
    };

    @java.lang.Override
    public com.google.protobuf.Parser<Pose2D> getParserForType() {
      return PARSER;
    }

    private int bitField0_;
    // required .llsf_msgs.Time timestamp = 1;
    public static final int TIMESTAMP_FIELD_NUMBER = 1;
    private org.robocup_logistics.llsf_msgs.TimeProtos.Time timestamp_;
    /**
     * <code>required .llsf_msgs.Time timestamp = 1;</code>
     *
     * <pre>
     * Time when this pose was measured
     * </pre>
     */
    public boolean hasTimestamp() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required .llsf_msgs.Time timestamp = 1;</code>
     *
     * <pre>
     * Time when this pose was measured
     * </pre>
     */
    public org.robocup_logistics.llsf_msgs.TimeProtos.Time getTimestamp() {
      return timestamp_;
    }
    /**
     * <code>required .llsf_msgs.Time timestamp = 1;</code>
     *
     * <pre>
     * Time when this pose was measured
     * </pre>
     */
    public org.robocup_logistics.llsf_msgs.TimeProtos.TimeOrBuilder getTimestampOrBuilder() {
      return timestamp_;
    }

    // required float x = 2;
    public static final int X_FIELD_NUMBER = 2;
    private float x_;
    /**
     * <code>required float x = 2;</code>
     *
     * <pre>
     * X/Y coordinates in meters
     * </pre>
     */
    public boolean hasX() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>required float x = 2;</code>
     *
     * <pre>
     * X/Y coordinates in meters
     * </pre>
     */
    public float getX() {
      return x_;
    }

    // required float y = 3;
    public static final int Y_FIELD_NUMBER = 3;
    private float y_;
    /**
     * <code>required float y = 3;</code>
     */
    public boolean hasY() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>required float y = 3;</code>
     */
    public float getY() {
      return y_;
    }

    // required float ori = 4;
    public static final int ORI_FIELD_NUMBER = 4;
    private float ori_;
    /**
     * <code>required float ori = 4;</code>
     *
     * <pre>
     * Orientation in rad
     * </pre>
     */
    public boolean hasOri() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>required float ori = 4;</code>
     *
     * <pre>
     * Orientation in rad
     * </pre>
     */
    public float getOri() {
      return ori_;
    }

    private void initFields() {
      timestamp_ = org.robocup_logistics.llsf_msgs.TimeProtos.Time.getDefaultInstance();
      x_ = 0F;
      y_ = 0F;
      ori_ = 0F;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      if (!hasTimestamp()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasX()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasY()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasOri()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!getTimestamp().isInitialized()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeMessage(1, timestamp_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeFloat(2, x_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeFloat(3, y_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeFloat(4, ori_);
      }
      getUnknownFields().writeTo(output);
    }

    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, timestamp_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeFloatSize(2, x_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeFloatSize(3, y_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeFloatSize(4, ori_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }

    public static org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }
    public static org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input);
    }
    public static org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseDelimitedFrom(input, extensionRegistry);
    }
    public static org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return PARSER.parseFrom(input);
    }
    public static org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return PARSER.parseFrom(input, extensionRegistry);
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code llsf_msgs.Pose2D}
     *
     * <pre>
     * Pose information on 2D map
     * Data is relative to the LLSF field frame
     * </pre>
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2DOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return org.robocup_logistics.llsf_msgs.Pose2DProtos.internal_static_llsf_msgs_Pose2D_descriptor;
      }

      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return org.robocup_logistics.llsf_msgs.Pose2DProtos.internal_static_llsf_msgs_Pose2D_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D.class, org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D.Builder.class);
      }

      // Construct using org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessage.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getTimestampFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }

      public Builder clear() {
        super.clear();
        if (timestampBuilder_ == null) {
          timestamp_ = org.robocup_logistics.llsf_msgs.TimeProtos.Time.getDefaultInstance();
        } else {
          timestampBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        x_ = 0F;
        bitField0_ = (bitField0_ & ~0x00000002);
        y_ = 0F;
        bitField0_ = (bitField0_ & ~0x00000004);
        ori_ = 0F;
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return org.robocup_logistics.llsf_msgs.Pose2DProtos.internal_static_llsf_msgs_Pose2D_descriptor;
      }

      public org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D getDefaultInstanceForType() {
        return org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D.getDefaultInstance();
      }

      public org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D build() {
        org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D buildPartial() {
        org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D result = new org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (timestampBuilder_ == null) {
          result.timestamp_ = timestamp_;
        } else {
          result.timestamp_ = timestampBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.x_ = x_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.y_ = y_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.ori_ = ori_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D) {
          return mergeFrom((org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D other) {
        if (other == org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D.getDefaultInstance()) return this;
        if (other.hasTimestamp()) {
          mergeTimestamp(other.getTimestamp());
        }
        if (other.hasX()) {
          setX(other.getX());
        }
        if (other.hasY()) {
          setY(other.getY());
        }
        if (other.hasOri()) {
          setOri(other.getOri());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasTimestamp()) {
          
          return false;
        }
        if (!hasX()) {
          
          return false;
        }
        if (!hasY()) {
          
          return false;
        }
        if (!hasOri()) {
          
          return false;
        }
        if (!getTimestamp().isInitialized()) {
          
          return false;
        }
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (org.robocup_logistics.llsf_msgs.Pose2DProtos.Pose2D) e.getUnfinishedMessage();
          throw e;
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      // required .llsf_msgs.Time timestamp = 1;
      private org.robocup_logistics.llsf_msgs.TimeProtos.Time timestamp_ = org.robocup_logistics.llsf_msgs.TimeProtos.Time.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          org.robocup_logistics.llsf_msgs.TimeProtos.Time, org.robocup_logistics.llsf_msgs.TimeProtos.Time.Builder, org.robocup_logistics.llsf_msgs.TimeProtos.TimeOrBuilder> timestampBuilder_;
      /**
       * <code>required .llsf_msgs.Time timestamp = 1;</code>
       *
       * <pre>
       * Time when this pose was measured
       * </pre>
       */
      public boolean hasTimestamp() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required .llsf_msgs.Time timestamp = 1;</code>
       *
       * <pre>
       * Time when this pose was measured
       * </pre>
       */
      public org.robocup_logistics.llsf_msgs.TimeProtos.Time getTimestamp() {
        if (timestampBuilder_ == null) {
          return timestamp_;
        } else {
          return timestampBuilder_.getMessage();
        }
      }
      /**
       * <code>required .llsf_msgs.Time timestamp = 1;</code>
       *
       * <pre>
       * Time when this pose was measured
       * </pre>
       */
      public Builder setTimestamp(org.robocup_logistics.llsf_msgs.TimeProtos.Time value) {
        if (timestampBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          timestamp_ = value;
          onChanged();
        } else {
          timestampBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>required .llsf_msgs.Time timestamp = 1;</code>
       *
       * <pre>
       * Time when this pose was measured
       * </pre>
       */
      public Builder setTimestamp(
          org.robocup_logistics.llsf_msgs.TimeProtos.Time.Builder builderForValue) {
        if (timestampBuilder_ == null) {
          timestamp_ = builderForValue.build();
          onChanged();
        } else {
          timestampBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>required .llsf_msgs.Time timestamp = 1;</code>
       *
       * <pre>
       * Time when this pose was measured
       * </pre>
       */
      public Builder mergeTimestamp(org.robocup_logistics.llsf_msgs.TimeProtos.Time value) {
        if (timestampBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              timestamp_ != org.robocup_logistics.llsf_msgs.TimeProtos.Time.getDefaultInstance()) {
            timestamp_ =
              org.robocup_logistics.llsf_msgs.TimeProtos.Time.newBuilder(timestamp_).mergeFrom(value).buildPartial();
          } else {
            timestamp_ = value;
          }
          onChanged();
        } else {
          timestampBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      /**
       * <code>required .llsf_msgs.Time timestamp = 1;</code>
       *
       * <pre>
       * Time when this pose was measured
       * </pre>
       */
      public Builder clearTimestamp() {
        if (timestampBuilder_ == null) {
          timestamp_ = org.robocup_logistics.llsf_msgs.TimeProtos.Time.getDefaultInstance();
          onChanged();
        } else {
          timestampBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      /**
       * <code>required .llsf_msgs.Time timestamp = 1;</code>
       *
       * <pre>
       * Time when this pose was measured
       * </pre>
       */
      public org.robocup_logistics.llsf_msgs.TimeProtos.Time.Builder getTimestampBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getTimestampFieldBuilder().getBuilder();
      }
      /**
       * <code>required .llsf_msgs.Time timestamp = 1;</code>
       *
       * <pre>
       * Time when this pose was measured
       * </pre>
       */
      public org.robocup_logistics.llsf_msgs.TimeProtos.TimeOrBuilder getTimestampOrBuilder() {
        if (timestampBuilder_ != null) {
          return timestampBuilder_.getMessageOrBuilder();
        } else {
          return timestamp_;
        }
      }
      /**
       * <code>required .llsf_msgs.Time timestamp = 1;</code>
       *
       * <pre>
       * Time when this pose was measured
       * </pre>
       */
      private com.google.protobuf.SingleFieldBuilder<
          org.robocup_logistics.llsf_msgs.TimeProtos.Time, org.robocup_logistics.llsf_msgs.TimeProtos.Time.Builder, org.robocup_logistics.llsf_msgs.TimeProtos.TimeOrBuilder> 
          getTimestampFieldBuilder() {
        if (timestampBuilder_ == null) {
          timestampBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              org.robocup_logistics.llsf_msgs.TimeProtos.Time, org.robocup_logistics.llsf_msgs.TimeProtos.Time.Builder, org.robocup_logistics.llsf_msgs.TimeProtos.TimeOrBuilder>(
                  timestamp_,
                  getParentForChildren(),
                  isClean());
          timestamp_ = null;
        }
        return timestampBuilder_;
      }

      // required float x = 2;
      private float x_ ;
      /**
       * <code>required float x = 2;</code>
       *
       * <pre>
       * X/Y coordinates in meters
       * </pre>
       */
      public boolean hasX() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>required float x = 2;</code>
       *
       * <pre>
       * X/Y coordinates in meters
       * </pre>
       */
      public float getX() {
        return x_;
      }
      /**
       * <code>required float x = 2;</code>
       *
       * <pre>
       * X/Y coordinates in meters
       * </pre>
       */
      public Builder setX(float value) {
        bitField0_ |= 0x00000002;
        x_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required float x = 2;</code>
       *
       * <pre>
       * X/Y coordinates in meters
       * </pre>
       */
      public Builder clearX() {
        bitField0_ = (bitField0_ & ~0x00000002);
        x_ = 0F;
        onChanged();
        return this;
      }

      // required float y = 3;
      private float y_ ;
      /**
       * <code>required float y = 3;</code>
       */
      public boolean hasY() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>required float y = 3;</code>
       */
      public float getY() {
        return y_;
      }
      /**
       * <code>required float y = 3;</code>
       */
      public Builder setY(float value) {
        bitField0_ |= 0x00000004;
        y_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required float y = 3;</code>
       */
      public Builder clearY() {
        bitField0_ = (bitField0_ & ~0x00000004);
        y_ = 0F;
        onChanged();
        return this;
      }

      // required float ori = 4;
      private float ori_ ;
      /**
       * <code>required float ori = 4;</code>
       *
       * <pre>
       * Orientation in rad
       * </pre>
       */
      public boolean hasOri() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>required float ori = 4;</code>
       *
       * <pre>
       * Orientation in rad
       * </pre>
       */
      public float getOri() {
        return ori_;
      }
      /**
       * <code>required float ori = 4;</code>
       *
       * <pre>
       * Orientation in rad
       * </pre>
       */
      public Builder setOri(float value) {
        bitField0_ |= 0x00000008;
        ori_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required float ori = 4;</code>
       *
       * <pre>
       * Orientation in rad
       * </pre>
       */
      public Builder clearOri() {
        bitField0_ = (bitField0_ & ~0x00000008);
        ori_ = 0F;
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:llsf_msgs.Pose2D)
    }

    static {
      defaultInstance = new Pose2D(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:llsf_msgs.Pose2D)
  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_llsf_msgs_Pose2D_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_llsf_msgs_Pose2D_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\014Pose2D.proto\022\tllsf_msgs\032\nTime.proto\"O\n" +
      "\006Pose2D\022\"\n\ttimestamp\030\001 \002(\0132\017.llsf_msgs.T" +
      "ime\022\t\n\001x\030\002 \002(\002\022\t\n\001y\030\003 \002(\002\022\013\n\003ori\030\004 \002(\002B/" +
      "\n\037org.robocup_logistics.llsf_msgsB\014Pose2" +
      "DProtos"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_llsf_msgs_Pose2D_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_llsf_msgs_Pose2D_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_llsf_msgs_Pose2D_descriptor,
              new java.lang.String[] { "Timestamp", "X", "Y", "Ori", });
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          org.robocup_logistics.llsf_msgs.TimeProtos.getDescriptor(),
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}
