package com.luangeng.rest.server;

import com.luangeng.rest.impl.HttpRequest;
import com.luangeng.rest.impl.HttpResponse;
import com.luangeng.rest.impl.ServletCfg;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import javax.servlet.Servlet;
import java.nio.charset.Charset;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by LG on 2017/12/4.
 */
public class ServletHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext context, FullHttpRequest httpRequest) throws Exception {
        System.out.println("channel read");

        String uri = httpRequest.uri();

        Servlet servlet = ServletMng.getServlet(uri);
        if (servlet == null) {
            sendError(context, HttpResponseStatus.NOT_FOUND);
            return;
        }

        ServletCfg config = new ServletCfg();
        servlet.init(config);

        HttpRequest request = new HttpRequest(httpRequest);
        HttpResponse response = new HttpResponse();
        servlet.service(request, response);


        FullHttpResponse nettyResponse = getHttpResponse(response);
        nettyResponse.setProtocolVersion(httpRequest.protocolVersion());

        context.writeAndFlush(nettyResponse);
        context.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status);
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.content().writeBytes(Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
        ctx.channel().write(response).addListener(ChannelFutureListener.CLOSE);
    }

    private FullHttpResponse getHttpResponse(HttpResponse response) {
        int statusCode = response.getStatus();
        FullHttpResponse nettyResponse = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.valueOf(statusCode));

        HttpUtil.setContentLength(nettyResponse, response.getContentLength());

        Map<String, String> responseHeaders = response.getHeaders();
        for (Map.Entry<String, String> header : responseHeaders.entrySet()) {
            nettyResponse.headers().add(header.getKey(), header.getValue());
        }
        String responseString = response.getResult();
        if (responseString != null) {
            Charset encoding = Charset.forName(response.getCharacterEncoding());
            nettyResponse.content().writeBytes(Unpooled.copiedBuffer(responseString, encoding));
        }
        return nettyResponse;
    }
}
