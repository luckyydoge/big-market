# --- 第一阶段：构建应用 (使用 JDK 8) ---
# 选择一个基于 JDK 8 的镜像来编译你的应用
# eclipse-temurin:8-jdk-jammy 是一个很好的选择，因为它基于Debian/Ubuntu，且由Eclipse Adoptium维护
FROM eclipse-temurin:8-jdk-jammy AS builder

# 设置容器内的工作目录
WORKDIR /app

# 复制 Maven 的 pom.xml 和 src 目录
# 这种分步 COPY 可以有效利用 Docker 的构建缓存。
# 如果 pom.xml 不变，后续的 mvn clean package 就不需要重新下载依赖。
COPY pom.xml .
COPY src ./src

# 执行 Maven 构建命令
# -Dmaven.test.skip=true: 跳过测试，加快 Docker 构建速度（可选，根据你的需求决定）
# clean package: 清理并打包应用
RUN mvn clean package -Dmaven.test.skip=true

# --- 第二阶段：构建最终镜像 (使用 JRE 8) ---
# 选择一个基于 JRE 8 的轻量级镜像来运行应用
# eclipse-temurin:8-jre-jammy 只包含 JRE，比 JDK 小很多，适合生产环境
FROM eclipse-temurin:8-jre-jammy

# 定义环境变量，指定 Spring Boot 应用的可执行 JAR 包名称
# **请务必根据你实际项目构建出的 JAR 包名称进行修改！**
ENV ARTIFACT_NAME=big-market-0.0.1-SNAPSHOT.jar

# 设置容器内的工作目录，这里是应用程序的根目录
WORKDIR /app

# 从第一阶段 (builder) 复制构建好的可执行 JAR 包到当前阶段
# 这就是多阶段构建的精髓：只复制需要运行的文件，不包含构建工具和源码
COPY --from=builder /app/target/${ARTIFACT_NAME} ./

# (可选) 如果你的 Spring Boot 应用需要外部的配置文件（例如 application.yml），
# 确保这些文件在 Dockerfile 所在目录，并在 COPY 之前。
# 如果它们在 JAR 包内部，则不需要这一步。
# COPY src/main/resources/application.yml ./config/application.yml

# 暴露应用监听的端口。Spring Boot 默认在 8080 端口运行。
EXPOSE 28888

# 定义容器启动时执行的命令
# java -jar 命令用于运行 Spring Boot 可执行 JAR 包
# 你可以在这里添加 JVM 参数，例如 -Xmx256m 来限制内存使用。
ENTRYPOINT ["java", "-jar", "${ARTIFACT_NAME}"]

# (可选) 健康检查：确保容器内的应用程序确实在运行并响应请求
# HEALTHCHECK --interval=30s --timeout=10s --retries=3 CMD curl -f http://localhost:8080/actuator/health || exit 1