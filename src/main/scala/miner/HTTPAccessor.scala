package miner

import java.util

import org.apache.http.Header
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.{CloseableHttpClient, HttpClientBuilder}
import org.apache.http.message.BasicHeader

/**
 * Created by christopher on 22/11/14.
 */
trait HTTPAccessor {

  def makeClient(): CloseableHttpClient = {

    val headers = new util.LinkedList[Header]()
    headers.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"))
    headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate,sdch"))
    headers.add(new BasicHeader("Accept-Language", "en-GB,en;q=0.8,en-US;q=0.6,sv;q=0.4"))
    headers.add(new BasicHeader("Cache-Control", "max-age=0"))
    headers.add(new BasicHeader("Connection", "keep-alive"))
    headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36"))

    HttpClientBuilder.create().setDefaultHeaders(headers).build()
  }

  def getGet(targetUrl: String) = {
    val request = new HttpGet(targetUrl)
    request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
    request.addHeader("Accept-Encoding", "gzip,deflate,sdch")
    request.addHeader("Accept-Language", "en-GB,en;q=0.8,en-US;q=0.6,sv;q=0.4")
    request.addHeader("Cache-Control", "max-age=0")
    request.addHeader("Connection", "keep-alive")
    request.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36")

    request
  }
}
