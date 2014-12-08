package miner

import org.apache.http.client.HttpClient

import scala.xml.{NodeSeq, XML}

/**
 * Created by christopher on 23/11/14.
 */
trait XMLRetriever extends HTTPAccessor {
  def getXML(url: String, httpClient: HttpClient): NodeSeq = {
    try {
      val request = getGet(url)
      val httpResponse = httpClient.execute(request)
      val entity = httpResponse.getEntity
      if (entity != null) {
        val inputStream = entity.getContent
        val content = io.Source.fromInputStream(inputStream).getLines().mkString
        inputStream.close()
        XML.loadString(content)
      } else {
        XML.loadString("<result>error</result>")
      }
    } catch {
      case e: Exception =>
        XML.loadString(s"<exception>${e.getMessage}</exception>")
    }
  }

}