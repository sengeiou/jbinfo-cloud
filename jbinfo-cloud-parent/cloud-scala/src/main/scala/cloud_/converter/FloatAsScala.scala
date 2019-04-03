package cloud_.converter

case class FloatAsScala(f: java.lang.Float) {
  def asScala: Option[Float] = Option(f).map(_.asInstanceOf[Float])
}
