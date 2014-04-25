## jersey-mvc-freemarker-extension
===============================

This is extension for jersey-mvc-freemarker

### jersey-mvc-freemarker-ext-common

This extension add availability of setting freemarker properties.

You can use maven to use this extension.
```
    <repositories>
        <repository>
            <id>kamegu-github</id>
            <url>http://kamegu.github.io/mvn-repo/</url>
        </repository>
    </repositories>
    <dependencies>
        <dependency>
            <groupId>com.github.kamegu</groupId>
            <artifactId>jersey-mvc-freemarker-ext-common</artifactId>
            <version>0.0.1</version>
        </dependency>
    </dependencies>
```

like this
```
@ApplicationPath("web")
public class WebConfig extends ResourceConfig {

    public WebConfig() {
        register(FreemarkerMvcFeature.class);
        property(FreemarkerMvcFeature.TEMPLATES_BASE_PATH, "WEB-INF/freemarker");
        property(FreemarkerMvcFeature.TEMPLATE_OBJECT_FACTORY, FlexibleConfiguration.class);
        property(ExtendedFreemarkerProperties.TEMPLATE_PROPERTIES, ExtendedFreemarkerProperties.extendedProperties("template_exception_handler", "html_debug"));

        ....
    }
}
```

### jersey-mvc-freemarker-ext-utf8

Before 2.7, freemarker process uses default encoding in outputting.
At windows-japanese env, default encoding is windows-31j and windows-31j-encoded file is outputted.
This extension use UTF-8 for outputting.

```
        <dependency>
            <groupId>com.github.kamegu</groupId>
            <artifactId>jersey-mvc-freemarker-ext-utf8</artifactId>
            <version>0.0.1-for2.5.1</version>
        </dependency>
```

You have to register Utf8FreemarkerFeature.class to Configuration.
Defaultly you can use FlexibleConfiguration@jersey-mvc-freemarker-ext-common.
