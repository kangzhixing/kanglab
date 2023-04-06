module kanglab.utils {
    requires spring.core;
    requires spring.expression;
    requires aether.api;
    requires maven.aether.provider;
    requires aether.connector.basic;
    requires aether.impl;
    requires aether.spi;
    requires aether.transport.file;
    requires aether.transport.http;
    requires aether.util;
    requires lombok;
    requires ant;
    requires slf4j.api;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.httpcomponents.httpclient;
    requires druid;
    requires org.mapstruct;
    requires commons.lang3;
    requires dom4j;
    requires com.google.common;
    requires java.sql;
    requires swagger.annotations;
    exports com.kang.lab.utils;
    opens com.kang.lab.utils;
}