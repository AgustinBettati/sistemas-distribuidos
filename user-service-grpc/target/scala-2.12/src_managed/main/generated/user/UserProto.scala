// Generated by the Scala Plugin for the Protocol Buffer Compiler.
// Do not edit!
//
// Protofile syntax: PROTO3

package generated.user

object UserProto extends _root_.scalapb.GeneratedFileObject {
  lazy val dependencies: Seq[_root_.scalapb.GeneratedFileObject] = Seq(
  )
  lazy val messagesCompanions: Seq[_root_.scalapb.GeneratedMessageCompanion[_]] = Seq(
    generated.user.User,
    generated.user.UserList,
    generated.user.UserRequest,
    generated.user.UsersRequest,
    generated.user.ProductUserRequest,
    generated.user.ProductReferences
  )
  private lazy val ProtoBytes: Array[Byte] =
      scalapb.Encoding.fromBase64(scala.collection.Seq(
  """Cgp1c2VyLnByb3RvImwKBFVzZXISDgoCaWQYASABKAVSAmlkEhIKBG5hbWUYAiABKAlSBG5hbWUSQAoRcHJvZHVjdFJlZmVyZ
  W5jZXMYAyABKAsyEi5Qcm9kdWN0UmVmZXJlbmNlc1IRcHJvZHVjdFJlZmVyZW5jZXMiJwoIVXNlckxpc3QSGwoFdXNlcnMYASADK
  AsyBS5Vc2VyUgV1c2VycyIdCgtVc2VyUmVxdWVzdBIOCgJpZBgBIAEoBVICaWQiDgoMVXNlcnNSZXF1ZXN0IkoKElByb2R1Y3RVc
  2VyUmVxdWVzdBIWCgZpZFVzZXIYASABKAVSBmlkVXNlchIcCglpZFByb2R1Y3QYAiABKAVSCWlkUHJvZHVjdCIjChFQcm9kdWN0U
  mVmZXJlbmNlcxIOCgJpZBgBIAMoBVICaWQy4QEKC1VzZXJTZXJ2aWNlEh4KB0dldFVzZXISDC5Vc2VyUmVxdWVzdBoFLlVzZXISJ
  AoIR2V0VXNlcnMSDS5Vc2Vyc1JlcXVlc3QaCS5Vc2VyTGlzdBIoCgpBZGRQcm9kdWN0EhMuUHJvZHVjdFVzZXJSZXF1ZXN0GgUuV
  XNlchIrCg1EZWxldGVQcm9kdWN0EhMuUHJvZHVjdFVzZXJSZXF1ZXN0GgUuVXNlchI1ChFHZXRQcm9kdWN0c09mVXNlchIMLlVzZ
  XJSZXF1ZXN0GhIuUHJvZHVjdFJlZmVyZW5jZXNCDQoJZ2VuZXJhdGVkUAFiBnByb3RvMw=="""
      ).mkString)
  lazy val scalaDescriptor: _root_.scalapb.descriptors.FileDescriptor = {
    val scalaProto = com.google.protobuf.descriptor.FileDescriptorProto.parseFrom(ProtoBytes)
    _root_.scalapb.descriptors.FileDescriptor.buildFrom(scalaProto, dependencies.map(_.scalaDescriptor))
  }
  lazy val javaDescriptor: com.google.protobuf.Descriptors.FileDescriptor = {
    val javaProto = com.google.protobuf.DescriptorProtos.FileDescriptorProto.parseFrom(ProtoBytes)
    com.google.protobuf.Descriptors.FileDescriptor.buildFrom(javaProto, Array(
    ))
  }
  @deprecated("Use javaDescriptor instead. In a future version this will refer to scalaDescriptor.", "ScalaPB 0.5.47")
  def descriptor: com.google.protobuf.Descriptors.FileDescriptor = javaDescriptor
}