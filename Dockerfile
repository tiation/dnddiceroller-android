FROM openjdk:17-slim

# Install essential packages
RUN apt-get update && apt-get install -y \
    curl \
    unzip \
    git \
    && rm -rf /var/lib/apt/lists/*

# Install Android SDK
ENV ANDROID_HOME /opt/android-sdk
ENV ANDROID_SDK_ROOT $ANDROID_HOME
ENV PATH ${PATH}:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools

RUN mkdir -p ${ANDROID_HOME}/cmdline-tools && \
    curl -o cmdline-tools.zip https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip && \
    unzip cmdline-tools.zip -d ${ANDROID_HOME}/cmdline-tools && \
    mv ${ANDROID_HOME}/cmdline-tools/cmdline-tools ${ANDROID_HOME}/cmdline-tools/latest && \
    rm cmdline-tools.zip

# Accept licenses and install required components
RUN yes | sdkmanager --licenses && \
    sdkmanager "platform-tools" \
    "platforms;android-34" \
    "build-tools;34.0.0" \
    "extras;android;m2repository" \
    "extras;google;m2repository"

# Set up Gradle
ENV GRADLE_VERSION 8.4
ENV GRADLE_HOME /opt/gradle
ENV PATH ${PATH}:${GRADLE_HOME}/bin

RUN curl -L https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o gradle.zip && \
    mkdir -p ${GRADLE_HOME} && \
    unzip gradle.zip -d /opt && \
    mv /opt/gradle-${GRADLE_VERSION} ${GRADLE_HOME} && \
    rm gradle.zip

WORKDIR /app
