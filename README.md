# zio2-linux
This project provides a ZIO-based wrapper for Linux I/O and memory management functions using the Java Native Access (JNA) library. The project includes functionality for memory allocation, I/O operations, and various system-level tasks, making it easier to interact with low-level system calls in a type-safe and functional way using ZIO.

## Features

- **Memory operations**:
    - `malloc(size: Int): Memory`: Allocates memory of given size.
    - `calloc(num: Long, size: Long): Pointer`: Allocates memory for an array of elements, initializing them to zero.
    - `realloc(ptr: Pointer, newSize: Int): Pointer`: Reallocates memory to a new size.
    - `free(pointer: Pointer): Unit`: Frees allocated memory.
    - `memcpy(dest: Pointer, src: Pointer, n: Long): Pointer`: Copies memory from source to destination.
    - `memset(dest: Pointer, c: Int, n: Long): Pointer`: Fills memory with a constant byte.
    - `memcmp(s1: Pointer, s2: Pointer, n: Long): Int`: Compares memory areas.

- **I/O operations**:
    - `open(pathname: String, flags: Int): Int`: Opens a file.
    - `close(fd: Int): Int`: Closes a file descriptor.
    - `read(fd: Int, buf: Pointer, count: Long): Long`: Reads data from a file.
    - `write(fd: Int, buf: Pointer, count: Long): Long`: Writes data to a file.
    - `lseek(fd: Int, offset: Long, whence: Int): Long`: Repositions the file offset.
    - `ioctl(fd: Int, request: Long, arg: Long): Int`: Manipulates the underlying device parameters of special files.


## Prerequisites

- Java 8 or higher.
- SBT or Maven for building the project.
- Linux operating system for running the native I/O and memory operations.
- JNA library.

## Project structure

- **CLib.scala** defines the JNA interface to the C library functions.

- **LinuxIOService.scala** provides ZIO-based services for I/O operations.

- **LinuxMemoryService.scala** provides ZIO-based services for memory operations.

## Usage example
Here is an example of how to use the Linux I/O and Memory services in your ZIO application:

```scala
import org.cameron.cs.{LinuxIOService, LinuxMemoryService}
import zio._
import com.sun.jna.Pointer

object LinuxExampleApp extends ZIOAppDefault {

def run = for {
// Open a file
fd <- LinuxIOService.open("/tmp/testfile", LinuxIOService.Mode.O_RDWR).provideLayer(LinuxIOService.live)

    // allocate memory
    source <- LinuxMemoryService.malloc(8).provideLayer(LinuxMemoryService.live)
    _ = source.setLong(0, 12345678L)
    
    // write to file
    _ <- LinuxIOService.write(fd, source, 8).provideLayer(LinuxIOService.live)
    
    // seek to the start of the file
    _ <- LinuxIOService.lseek(fd, 0, LinuxIOService.Indicator.SEEK_SET).provideLayer(LinuxIOService.live)
    
    // read from file
    readBuffer <- LinuxMemoryService.malloc(8).provideLayer(LinuxMemoryService.live)
    _ <- LinuxIOService.read(fd, readBuffer, 8).provideLayer(LinuxIOService.live)
    readValue = readBuffer.getLong(0)
    
    // print read value
    _ <- Console.printLine(s"Read value: $readValue")
    
    // close the file
    _ <- LinuxIOService.close(fd).provideLayer(LinuxIOService.live)

} yield ()
}
```