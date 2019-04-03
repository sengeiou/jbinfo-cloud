package cloud_.converter

import java.util.Optional

import cloud_.ScalaConverters

case class OptionalAsScala[A](opt: Optional[A]) {
  def asScala: Option[A] = ScalaConverters.toScalaOption(opt)
}
