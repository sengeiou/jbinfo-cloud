package cn.jbinfo.conf

import cloud_.JavaConverters.{JavaList, JavaMap}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.google.common.collect.Maps
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class ScalaWebConfig extends WebMvcConfigurer {

  override def configureMessageConverters(converters: JavaList[HttpMessageConverter[_]]): Unit = {
    val defaultConverters: JavaMap[String, HttpMessageConverter[_]] = getDefaultMessageConverters
    val scalaConverter = createJsonMessageConverterWithScalaSupport
    defaultConverters.put(scalaConverter.getClass.toString, scalaConverter)
    converters.addAll(defaultConverters.values)
  }

  private def getDefaultMessageConverters = {
    val dummyTemplate = new RestTemplate
    val defaultConverters: JavaMap[String, HttpMessageConverter[_]] = Maps.newHashMap[String, HttpMessageConverter[_]]()
    dummyTemplate.getMessageConverters.forEach(c => {
      defaultConverters.put(c.getClass.toString, c)
    })
    defaultConverters
  }

  private def createJsonMessageConverterWithScalaSupport: MappingJackson2HttpMessageConverter = {
    val mapper = createObjectMapper
    mapper.registerModule(new DefaultScalaModule)
    val converter = createJacksonMessageConverter
    converter.setObjectMapper(mapper)
    converter
  }

  @Bean def createJacksonMessageConverter = new MappingJackson2HttpMessageConverter

  @Bean def createObjectMapper = new ObjectMapper

}
