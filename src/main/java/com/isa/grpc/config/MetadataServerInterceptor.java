package com.isa.grpc.config;

import io.grpc.*;
import io.grpc.stub.MetadataUtils;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@GrpcGlobalServerInterceptor
public class MetadataServerInterceptor implements ServerInterceptor{

	private static final Logger logger = Logger.getLogger(MetadataServerInterceptor.class.getName());
	private static final List<String> logLevels = Arrays.asList("debug,info,warn,error");
	private static final Metadata.Key<String> attribute=Metadata.Key.of("level",Metadata.ASCII_STRING_MARSHALLER);

	@Override
	public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
			ServerCall<ReqT, RespT> call,
			final Metadata requestHeaders,
			ServerCallHandler<ReqT, RespT> next) {
		logger.info("header received from client:" + requestHeaders.toString());

		String level = requestHeaders.get(attribute);
		if(level!=null && logLevels.get(0).contains(level.toLowerCase())){
			System.out.println("------------------------");
			MDC.put("X-Log-Level",level.toUpperCase());
		}

		return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(next.startCall(call, requestHeaders)) {
			@Override
			public void onComplete() {
				logger.info("onComplete:");
				if(level!=null && logLevels.get(0).contains(level.toLowerCase())){
					System.out.println("+++++++++++++++++++");
					MDC.remove("X-Log-Level");
				}
			}
		};
	}
}