# 在启动项目时加载一个 javaagent Jar 包
## 方法 1：通过编辑运行配置添加 -javaagent 参数
### 1.打开运行配置:
- 在 IDEA 中点击 Run 菜单，选择 Edit Configurations...。
### 2.找到你的运行配置：
- 在左侧选择你要启动的运行配置（比如你的 Spring Boot 项目）。
### 3.添加 VM Options：
- 在右侧找到 VM Options 输入框。
- 添加 -javaagent 参数，例如：
```text
-javaagent:/absolute/path/to/your-agent.jar
```
### 4.保存配置：
- 点击 Apply，然后点击 OK。
### 5.启动项目
- 使用运行按钮启动项目，IDEA 会加载指定的 Java Agent。

## 方法2： 通过 application.properties 或 application.yml 配置加载 Java Agent
如果你的项目使用 Spring Boot，并且你想通过配置文件动态加载 Agent，可以在 application.properties 或 application.yml 文件中添加以下内容： 
- 在 application.properties：
```properties
javaagent.path=target/my-agent-project-1.0-SNAPSHOT.jar
```
在运行配置中：
- 将以下参数添加到 VM Options：
```text
-javaagent=${javaagent.path}
```

## 方法3：通过 IDEA 的 Maven 配置加载 Java Agent
如果你的项目是一个 Maven 项目，可以将 Java Agent 配置到 Maven 的运行插件中。
### 配置 pom.xml
在 Maven 的 spring-boot-maven-plugin 或 exec-maven-plugin 中添加 VM 参数：
```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <jvmArguments>-javaagent:target/my-agent-project-1.0-SNAPSHOT.jar</jvmArguments>
            </configuration>
        </plugin>
    </plugins>
</build>
```
运行时，IDEA 会自动加载指定的 Java Agent。

## 方法4：通过命令行加载 Java Agent
你也可以直接在命令行启动项目时添加 -javaagent 参数。例如：
```bash
java -javaagent:target/my-agent-project-1.0-SNAPSHOT.jar -jar target/your-spring-boot-app.jar
```