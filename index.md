---
title: sbs4j
menu_item: true
menu_title: Home
layout: default
order: 1
---
# sbs4j (Simple Byte Serializer for Java)

[![Java CI](https://github.com/oak/sbs4j/actions/workflows/build.yml/badge.svg)](https://github.com/oak/sbs4j/actions/workflows/build.yml)
![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/oak/sbs4j?sort=semver)
[![Project license](https://img.shields.io/badge/license-Apache%202-blue)](https://www.apache.org/licenses/LICENSE-2.0.txt)

This project aims to be a simple but effective java primitives and object byte array serializer/deserializer.

## Dependencies

- Java 8+
- Maven (via wrapper)

## Build instructions

```
./mvnw package
```

## How to

The library provides two interfaces: [`SerializableObject`] and [`DeserializableObject`]. One for implementing object
serialization and other for object deserialization.

Also, it includes two classes: [`SerializerBuffer`] and [`DeserializerBuffer`] which are instantiated and passed through
the object hierarchy either serializing or deserializing its values.

### Including the library dependency

#### Using Maven

``` xml
<dependency>
    <groupId>io.github.oak</groupId>
    <artifactId>sbs4j</artifactId>
    <version>VERSION</version>
</dependency>
```

#### Using gradle

``` groovy
implementation 'io.github.oak:sbs4j:VERSION'
```

### Usage

You can find more examples on the [`tests`] folder

``` java
public final class Example {
  public static byte[] serializePoint() {
      SerializerBuffer serializerBuffer = new SerializerBuffer();
      
      Point point = new Point(4, 2, 9);
      point.serialize(serializerBuffer);
      
      byte[] serializedBytes = serializerBuffer.toByteArray();
      
      return serializedBytes;
  }
  
  public static Point deserializePoint() {
      ObjectDeserializer deserializerBuffer = new ObjectDeserializer();
      
      byte[] serializedBytes = new byte[]{4, 0, 0, 0, 2, 0, 0, 0, 9, 0, 0, 0};
      
      deserializerBuffer.deserialize(serializedBytes);    
  
      Point point = new Point();
      point.deserialize(deserializerBuffer);
      
      return point;
  }
}

public class Point implements SerializableObject, DeserializableObject {
    private int x;
    private int y;
    private int z;

    // ... getters, setters, constructors...

    @Override
    public void serialize(ObjectSerializer serializer) {
        // The written order...
        serializer.writeI32(x);
        serializer.writeI32(y);
        serializer.writeI32(z);
    }

    @Override
    public void deserialize(ObjectDeserializer deserializer) throws ValueDeserializationException {
        // ...defines the read order
        this.x = deserializer.readI32();
        this.y = deserializer.readI32();
        this.z = deserializer.readI32();
    }
}
```

## Primitive Type Mappings

| sbs4j                 | Java         | Status      |
|-----------------------|--------------|-------------|
| `boolean` boolean     | `byte`       | implemented |
| `u8` integer          | `byte`       | implemented |
| `u16` integer         | `short`      | implemented |
| `i32` integer         | `int`        | implemented |
| `u32` integer         | `long`       | implemented |
| `i64` integer         | `long`       | implemented |
| `u64` integer         | `BigInteger` | implemented |
| `u128` integer        | `BigInteger` | implemented |
| `u256` integer        | `BigInteger` | implemented |
| `u512` integer        | `BigInteger` | implemented |
| `u128` integer        | `BigInteger` | implemented |
| `f32` float           | `float`      | implemented |
| `f64` float           | `double`     | implemented |

## Roadmap

The following features are planned to be supported:

- Ensure Android support
- Improve support for customization
- Helper methods for serializing/deserializing more complex objects easier and with less code (e.g., List, Map,
  Optional, etc.).
- Alternative reflection-based serialization and deserialization (i.e., process an object and its fields without writing any custom
  code).

[`SerializerBuffer`]: https://github.com/oak/sbs4j/blob/master/src/main/java/dev/oak3/sbs4j/SerializerBuffer.java

[`DeserializerBuffer`]: https://github.com/oak/sbs4j/blob/master/src/main/java/dev/oak3/sbs4j/DeserializerBuffer.java

[`SerializableObject`]: https://github.com/oak/sbs4j/blob/master/src/main/java/dev/oak3/sbs4j/interfaces/SerializableObject.java

[`DeserializableObject`]: https://github.com/oak/sbs4j/blob/master/src/main/java/dev/oak3/sbs4j/interfaces/DeserializableObject.java

[`tests`]: https://github.com/oak/sbs4j/blob/master/src/test/java/dev/oak3/sbs4j/