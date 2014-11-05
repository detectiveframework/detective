# httpclient

Httpclient task act as a http client. it can do most your browser can do except renderering.

HTTP Client Task

  <p>
  Support GET, PUT, POST, DELETE, HEAD and OPTIONS

  <h4>Input</h4>

  <pre>

    http.use_shared_cookies: default true, identify if this httpclient will read cookies from share data section

    http.cookies: optional, output of other HTTPClientTask

    http.address: a String or a Java URI

    http.method: GET, PUT, POST, DELETE, HEAD, OPTIONS, optional, default to POST



    http.post.string: optional, the data as a plain text which will post to server



    http.post.file.filename: optional, the file you'd like to upload



  </pre>

  <h4>Output</h4>

  <pre>

    http.cookies: the http context current task have (may created by this task or passed in from input)

    http.content: http content returned from server
    http.content.string: http content returned from server, converted into string

    http.status.code

    http.header. : all headers returned from server

  </pre>



  <ul>

   <li>

     GET  The HTTP GET method is defined in section 9.3 of

  <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>:

  The GET method means retrieve whatever information (in the form of an

  entity) is identified by the Request-URI.

   </li>

   <li>

     PUT HTTP PUT method.

  The HTTP PUT method is defined in section 9.6 of

  <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>:

  The PUT method requests that the enclosed entity be stored under the

  supplied Request-URI.

   </li>

   <li>

     POST HTTP POST method.

  The HTTP POST method is defined in section 9.5 of

  <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>:

  The POST method is used to request that the origin server accept the entity

  enclosed in the request as a new subordinate of the resource identified by

  the Request-URI in the Request-Line.

   </li>

   <li>

     DELETE HTTP DELETE method

  The HTTP DELETE method is defined in section 9.7 of

  <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>:

  The DELETE method requests that the origin server delete the resource

  identified by the Request-URI.

   </li>

   <li>

     HEAD HTTP HEAD method.

  The HTTP HEAD method is defined in section 9.4 of

  <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>:

  The HEAD method is identical to GET except that the server MUST NOT

  return a message-body in the response.

   </li>

   <li>

   OPTIONS HTTP OPTIONS method.

  The HTTP OPTIONS method is defined in section 9.2 of

  <a href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>:

   The OPTIONS method represents a request for information about the

   communication options available on the request/response chain

   identified by the Request-URI.

   </li>



  </ul>

  It able to share cookies which means you can share session between different requests in most cases.


  HttpClient Connection Manager Document Link: http://hc.apache.org/httpcomponents-client-4.3.x/tutorial/html/connmgmt.html

  </p>


