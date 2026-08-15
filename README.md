# MineQRaft
MineQRaft is a lightweight library based on ZXing and QRGen for loading QRCodes onto maps.

# Installation
[![](https://jitpack.io/v/dev.reassembly/MineQRaft.svg)](https://jitpack.io/#dev.reassembly/MineQRaft)

Firstly, add the JitPack repository

```kt
repositories {
    maven("https://jitpack.io")
}
```

Or for Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Next, add the dependency into your project. See the badge at the top of this section for the latest verison

```kt
dependencies {
    implementation("dev.reassembly:MineQRaft:<Version Tag>")
}
```
Or for Maven

```xml
<dependency>
    <groupId>dev.reassembly</groupId>
    <artifactId>MineQRaft</artifactId>
    <version>Version Tag</version>
</dependency>
```

You will likely need to use [shadowJar](https://gradleup.com/shadow/) to shade this library into your main JAR.

# Using the API

The heart of the library is the QRBuilder file, Below is an example you could use:

```java
import java.awt.*;
import dev.reassembly.mineqraft.QRBuilder;

ItemStack mapQrCode = new QRBuilder("https://github.com/TrueReassembly")
        .setBackgroundColor(Color.WHITE)
        .setForegroundColor(Color.BLUE)
        .getMap();

for (Player player : Bukkit.getOnlinePlayers()) {
    player.give(mapQrCode);
}
```