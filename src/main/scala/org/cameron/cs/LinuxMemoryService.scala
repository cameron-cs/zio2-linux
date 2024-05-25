package org.cameron.cs

import com.sun.jna.{Memory, Pointer}
import zio.*

trait LinuxMemoryService {

  def malloc(size: Int): Task[Memory]

  def calloc(num: Long, size: Long): Task[Pointer]

  def realloc(ptr: Pointer, newSize: Int): Task[Pointer]

  def free(pointer: Pointer): Task[Unit]

  def memcpy(dest: Pointer, src: Pointer, n: Long): Task[Pointer]

  def memset(dest: Pointer, c: Int, n: Long): Task[Pointer]

  def memcmp(s1: Pointer, s2: Pointer, n: Long): Task[Int]
}

object LinuxMemoryService {

  def malloc(size: Int): ZIO[LinuxMemoryService, Throwable, Memory] =
    ZIO.serviceWithZIO[LinuxMemoryService](_.malloc(size))

  def calloc(num: Long, size: Long): ZIO[LinuxMemoryService, Throwable, Pointer] =
    ZIO.serviceWithZIO[LinuxMemoryService](_.calloc(num, size))

  def realloc(ptr: Pointer, newSize: Int): ZIO[LinuxMemoryService, Throwable, Pointer] =
    ZIO.serviceWithZIO[LinuxMemoryService](_.realloc(ptr, newSize))

  def free(ptr: Pointer): ZIO[LinuxMemoryService, Throwable, Unit] =
    ZIO.serviceWithZIO[LinuxMemoryService](_.free(ptr))

  def memcpy(dest: Pointer, src: Pointer, n: Long): ZIO[LinuxMemoryService, Throwable, Pointer] =
    ZIO.serviceWithZIO[LinuxMemoryService](_.memcpy(dest, src, n))

  def memset(dest: Pointer, c: Int, n: Long): ZIO[LinuxMemoryService, Throwable, Pointer] =
    ZIO.serviceWithZIO[LinuxMemoryService](_.memset(dest, c, n))

  def memcmp(s1: Pointer, s2: Pointer, n: Long): ZIO[LinuxMemoryService, Throwable, Int] =
    ZIO.serviceWithZIO[LinuxMemoryService](_.memcmp(s1, s2, n))

  val live: ULayer[LinuxMemoryService] = ZLayer.succeed {
    new LinuxMemoryService {

      override def malloc(size: Int): Task[Memory] =
        ZioLinuxMemoryJna.malloc(size)

      override def calloc(num: Long, size: Long): Task[Pointer] =
        ZioLinuxMemoryJna.calloc(num, size)

      override def realloc(ptr: Pointer, newSize: Int): Task[Pointer] =
        ZioLinuxMemoryJna.realloc(ptr, newSize)

      override def free(ptr: Pointer): Task[Unit] =
        ZioLinuxMemoryJna.free(ptr)

      override def memcpy(dest: Pointer, src: Pointer, n: Long): Task[Pointer] =
        ZioLinuxMemoryJna.memcpy(dest, src, n)

      override def memset(dest: Pointer, c: Int, n: Long): Task[Pointer] =
        ZioLinuxMemoryJna.memset(dest, c, n)

      override def memcmp(s1: Pointer, s2: Pointer, n: Long): Task[Int] =
        ZioLinuxMemoryJna.memcmp(s1, s2, n)
    }
  }
}