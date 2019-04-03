package cloud_.converter

case class DoubleAsScala(d: java.lang.Double) {
  def asScala: Option[Int] = Option(d).map(_.asInstanceOf[Int])
}
