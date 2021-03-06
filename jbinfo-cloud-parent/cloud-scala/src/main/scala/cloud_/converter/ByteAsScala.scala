package cloud_.converter

case class ByteAsScala(b: java.lang.Byte) {
  def asScala: Option[Byte] = Option(b).map(_.asInstanceOf[Byte])
}
