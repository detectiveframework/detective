package detective.task;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderNames.COOKIE;
import static io.netty.handler.codec.http.HttpHeaderNames.SET_COOKIE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpUtil.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import detective.common.annotation.ThreadSafe;
import detective.core.Parameters;
import detective.core.TestTask;
import detective.core.config.ConfigException;
import detective.core.dsl.ParametersImpl;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.netty.util.CharsetUtil;

@ThreadSafe
public class HttpServerTask implements TestTask {
  
  public interface ServerHandler{
    void close();
  }

  public Parameters execute(Parameters config) throws ConfigException {
    if (config == null)
      throw ConfigException.configCantEmpty();
    
    if (config.get("httpserver.port") == null)
      throw new ConfigException("httpserver.port can't be empty, it's a number for example 8080");
    Integer port = Integer.parseInt(config.get("httpserver.port").toString());
    
    // Configure the server.
    final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    final EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(
          new HttpSnoopServerInitializer());

      try {
        Channel ch = b.bind(port).sync().channel();
        ChannelFuture future = ch.closeFuture();
        Parameters result = new ParametersImpl();
        result.put("channel", future);
        result.put("handler", new ServerHandler(){

          public void close() {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
          }});
        return result;
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return null;
    } finally {
      //Close in handler
    }
  }

  public class HttpSnoopServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
      // Create a default pipeline implementation.
      ChannelPipeline p = ch.pipeline();

      // Uncomment the following line if you want HTTPS
      // SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
      // engine.setUseClientMode(false);
      // p.addLast("ssl", new SslHandler(engine));

      p.addLast("decoder", new HttpRequestDecoder());
      // Uncomment the following line if you don't want to handle HttpChunks.
      // p.addLast("aggregator", new HttpObjectAggregator(1048576));
      p.addLast("encoder", new HttpResponseEncoder());
      // Remove the following line if you don't want automatic content compression.
      // p.addLast("deflater", new HttpContentCompressor());
      p.addLast("handler", new HttpSnoopServerHandler());
    }
  }

  public static class HttpSnoopServerHandler extends SimpleChannelInboundHandler<Object> {

    private HttpRequest request;
    /** Buffer that stores the response content */
    private final StringBuilder buf = new StringBuilder();

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
      ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
      if (msg instanceof HttpRequest) {
        HttpRequest request = this.request = (HttpRequest) msg;

        if (is100ContinueExpected(request)) {
          send100Continue(ctx);
        }

        buf.setLength(0);
        buf.append("WELCOME TO THE WILD WILD WEB SERVER\r\n");
        buf.append("===================================\r\n");

        buf.append("VERSION: ").append(request.protocolVersion()).append("\r\n");
        buf.append("HOSTNAME: ").append(request.headers().get("Host", "unknown")).append("\r\n");
        buf.append("REQUEST_URI: ").append(request.uri()).append("\r\n\r\n");

        HttpHeaders headers = request.headers();
        if (!headers.isEmpty()) {
          for (Map.Entry<String, String> h : headers) {
            String key = h.getKey();
            String value = h.getValue();
            buf.append("HEADER: ").append(key).append(" = ").append(value).append("\r\n");
          }
          buf.append("\r\n");
        }

        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.uri());
        Map<String, List<String>> params = queryStringDecoder.parameters();
        if (!params.isEmpty()) {
          for (Entry<String, List<String>> p : params.entrySet()) {
            String key = p.getKey();
            List<String> vals = p.getValue();
            for (String val : vals) {
              buf.append("PARAM: ").append(key).append(" = ").append(val).append("\r\n");
            }
          }
          buf.append("\r\n");
        }

        appendDecoderResult(buf, request);
      }

      if (msg instanceof HttpContent) {
        HttpContent httpContent = (HttpContent) msg;

        ByteBuf content = httpContent.content();
        if (content.isReadable()) {
          buf.append("CONTENT: ");
          buf.append(content.toString(CharsetUtil.UTF_8));
          buf.append("\r\n");
          appendDecoderResult(buf, request);
        }

        if (msg instanceof LastHttpContent) {
          buf.append("END OF CONTENT\r\n");

          LastHttpContent trailer = (LastHttpContent) msg;
          if (!trailer.trailingHeaders().isEmpty()) {
            buf.append("\r\n");
            for (String name : trailer.trailingHeaders().names()) {
              for (String value : trailer.trailingHeaders().getAll(name)) {
                buf.append("TRAILING HEADER: ");
                buf.append(name).append(" = ").append(value).append("\r\n");
              }
            }
            buf.append("\r\n");
          }

          writeResponse(trailer, ctx);
        }
      }
    }

    private static void appendDecoderResult(StringBuilder buf, HttpObject o) {
      DecoderResult result = o.decoderResult();
      if (result.isSuccess()) {
        return;
      }

      buf.append(".. WITH DECODER FAILURE: ");
      buf.append(result.cause());
      buf.append("\r\n");
    }

    private boolean writeResponse(HttpObject currentObj, ChannelHandlerContext ctx) {
      // Decide whether to close the connection or not.
      boolean keepAlive = isKeepAlive(request);
      // Build the response object.
      FullHttpResponse response =
          new DefaultFullHttpResponse(HTTP_1_1, currentObj.decoderResult().isSuccess() ? OK
              : BAD_REQUEST, Unpooled.copiedBuffer(buf.toString(), CharsetUtil.UTF_8));

      response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

      if (keepAlive) {
        // Add 'Content-Length' header only for a keep-alive connection.
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        // Add keep alive header as per:
        // - http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
        response.headers().set(CONNECTION, KEEP_ALIVE);
      }

      // Encode the cookie.
      String cookieString = request.headers().get(COOKIE);
      if (cookieString != null) {
    	  Set<Cookie> cookies = ServerCookieDecoder.STRICT.decode(cookieString);
        if (!cookies.isEmpty()) {
          // Reset the cookies if necessary.
          for (Cookie cookie : cookies) {
            response.headers().add(SET_COOKIE, ServerCookieEncoder.LAX.encode(cookie));
          }
        }
      } else {
        // Browser sent no cookie. Add some.
        response.headers().add(SET_COOKIE, ServerCookieEncoder.LAX.encode("key1", "value1"));
        response.headers().add(SET_COOKIE, ServerCookieEncoder.LAX.encode("key2", "value2"));
      }

      // Write the response.
      ctx.write(response);

      return keepAlive;
    }

    private static void send100Continue(ChannelHandlerContext ctx) {
      FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, CONTINUE);
      ctx.write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      cause.printStackTrace();
      ctx.close();
    }
  }

}
