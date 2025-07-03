# --- 第一阶段：构建整个 Maven 多模块应用 ---
# 使用包含 OpenJDK 8 和 Maven 的基础镜像
FROM maven:3.8.6-openjdk-8-slim AS builder

# 设置容器内的工作目录为 /app
WORKDIR /app

# 复制整个项目到容器的 /app 目录
COPY . .

# 执行 Maven 构建命令，编译所有模块并生成可执行 JAR 包
RUN mvn clean install -Dmaven.test.skip=true

# --- 第二阶段：构建最终镜像 (使用 JRE 8) ---
FROM openjdk:8-jre-slim

# 定义环境变量，指定 Spring Boot 应用的可执行 JAR 包名称
# 请务必根据你实际项目构建出的 JAR 包名称进行修改！
ENV ARTIFACT_NAME="big-market-app.jar"
# 示例，请替换为你的实际名称

# 设置容器内的工作目录，这里是应用程序的根目录
WORKDIR /app

# 从第一阶段 (builder) 复制构建好的可执行 JAR 包到当前阶段
COPY --from=builder /app/big-market-app/target/${ARTIFACT_NAME} ./

# (可选) 如果你的 big-market-app 需要外部的 application.yml 等配置文件
# COPY --from=builder /app/big-market-app/src/main/resources/application.yml ./config/application.yml

# 暴露应用监听的端口 (Spring Boot 默认 8080)
EXPOSE 28888

# 定义环境变量，用于在启动时传递额外参数给 Java 应用
ENV PARAMS=""

# 设置时区，确保容器内部时间一致
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 定义容器启动时执行的命令
ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS ${ARTIFACT_NAME} $PARAMS"]

# (可选) 健康检查，用于 Kubernetes 等编排工具
# HEALTHCHECK --interval=30s --timeout=10s --retries=3 CMD curl -f http://localhost:8080/actuator/health || exit 1